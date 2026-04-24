package com.inseong.dallyrun.core.data.auth

import app.cash.turbine.test
import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.Gender
import com.inseong.dallyrun.core.network.AuthApi
import com.inseong.dallyrun.core.network.AuthApiErrorParser
import com.inseong.dallyrun.core.network.AuthApiException
import com.inseong.dallyrun.core.network.SignupMultipartBuilder
import com.inseong.dallyrun.core.network.model.ApiResponse
import com.inseong.dallyrun.core.network.model.LoginRequest
import com.inseong.dallyrun.core.network.model.NetworkTokenResponse
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthRepositoryImplTest {

    private val authApi = mockk<AuthApi>(relaxUnitFun = true)
    private val tokenManager = mockk<TokenManagerImpl>(relaxUnitFun = true)
    private val signupMultipartBuilder = mockk<SignupMultipartBuilder>()
    private val imageMultipartFactory = mockk<ImageMultipartFactory>()
    private val errorParser = AuthApiErrorParser(Json { ignoreUnknownKeys = true })
    private val repository = AuthRepositoryImpl(
        authApi = authApi,
        tokenManager = tokenManager,
        signupMultipartBuilder = signupMultipartBuilder,
        imageMultipartFactory = imageMultipartFactory,
        errorParser = errorParser,
    )

    @Test
    fun `loginWithEmail should call api and save tokens on success`() = runTest {
        val response = ApiResponse(NetworkTokenResponse("access", "refresh"))
        coEvery { authApi.login(LoginRequest("a@b.c", "Aa1!Aa1!")) } returns response

        val result = repository.loginWithEmail("a@b.c", "Aa1!Aa1!")

        assertEquals("access", result.accessToken)
        assertEquals("refresh", result.refreshToken)
        coVerify { tokenManager.saveTokens(accessToken = "access", refreshToken = "refresh") }
    }

    @Test
    fun `loginWithEmail should throw AuthApiException with friendly fallback on 401`() = runTest {
        coEvery { authApi.login(any()) } throws httpException(401, body = null)

        val ex = assertThrows(AuthApiException::class.java) {
            kotlinx.coroutines.runBlocking {
                repository.loginWithEmail("a@b.c", "wrong")
            }
        }
        assertEquals(401, ex.statusCode)
        assertEquals("이메일 또는 비밀번호가 올바르지 않아요", ex.message)
        coVerify(exactly = 0) { tokenManager.saveTokens(any(), any()) }
    }

    @Test
    fun `loginWithEmail should surface server error message when present`() = runTest {
        coEvery { authApi.login(any()) } throws httpException(
            code = 401,
            body = """{"message":"이메일 또는 비밀번호 확인"}""",
        )

        val ex = assertThrows(AuthApiException::class.java) {
            kotlinx.coroutines.runBlocking { repository.loginWithEmail("a@b.c", "x") }
        }
        assertEquals("이메일 또는 비밀번호 확인", ex.message)
    }

    @Test
    fun `signup should build parts, call api, save tokens`() = runTest {
        val dataPart = stubPart("data")
        val imagePart = stubPart("image")
        every {
            signupMultipartBuilder.buildDataPart(
                email = "a@b.c",
                password = "Aa1!Aa1!",
                nickname = "runner",
                ageBracket = 30,
                gender = "MALE",
            )
        } returns dataPart
        every {
            imageMultipartFactory.create(partName = "image", uriString = "content://photo")
        } returns imagePart
        val response = ApiResponse(NetworkTokenResponse("a", "r"))
        coEvery { authApi.signup(dataPart, imagePart) } returns response

        val result = repository.signup(
            email = "a@b.c",
            password = "Aa1!Aa1!",
            nickname = "runner",
            profileImageUri = "content://photo",
            ageGroup = AgeGroup.THIRTIES,
            gender = Gender.MALE,
        )

        assertEquals("a", result.accessToken)
        assertEquals("r", result.refreshToken)
        coVerify { tokenManager.saveTokens(accessToken = "a", refreshToken = "r") }
    }

    @Test
    fun `signup should throw when profile image is null`() = runTest {
        assertThrows(IllegalArgumentException::class.java) {
            kotlinx.coroutines.runBlocking {
                repository.signup(
                    email = "a@b.c",
                    password = "Aa1!Aa1!",
                    nickname = "runner",
                    profileImageUri = null,
                    ageGroup = AgeGroup.TWENTIES,
                    gender = Gender.FEMALE,
                )
            }
        }
        coVerify(exactly = 0) { authApi.signup(any(), any()) }
    }

    @Test
    fun `signup should map 409 to duplicate-friendly fallback`() = runTest {
        every { signupMultipartBuilder.buildDataPart(any(), any(), any(), any(), any()) } returns stubPart("data")
        every { imageMultipartFactory.create(any(), any()) } returns stubPart("image")
        coEvery { authApi.signup(any(), any()) } throws httpException(409, body = null)

        val ex = assertThrows(AuthApiException::class.java) {
            kotlinx.coroutines.runBlocking {
                repository.signup(
                    email = "a@b.c",
                    password = "Aa1!Aa1!",
                    nickname = "runner",
                    profileImageUri = "content://photo",
                    ageGroup = AgeGroup.TWENTIES,
                    gender = Gender.FEMALE,
                )
            }
        }
        assertEquals(409, ex.statusCode)
        assertEquals("이미 사용 중인 이메일 또는 닉네임이에요", ex.message)
    }

    @Test
    fun `refreshToken should call api and save new tokens`() = runTest {
        coEvery { tokenManager.getRefreshToken() } returns "old-refresh"
        val response = ApiResponse(NetworkTokenResponse("new-access", "new-refresh"))
        coEvery { authApi.refreshToken(TokenRefreshRequest("old-refresh")) } returns response

        val result = repository.refreshToken()

        assertEquals("new-access", result.accessToken)
        assertEquals("new-refresh", result.refreshToken)
        coVerify { tokenManager.saveTokens(accessToken = "new-access", refreshToken = "new-refresh") }
    }

    @Test
    fun `logout should call api and clear tokens`() = runTest {
        repository.logout()

        coVerify { authApi.logout() }
        coVerify { tokenManager.clearTokens() }
    }

    @Test
    fun `logout should clear tokens even when api fails`() = runTest {
        coEvery { authApi.logout() } throws RuntimeException("Network error")

        try {
            repository.logout()
        } catch (_: RuntimeException) {
            // expected
        }

        coVerify { tokenManager.clearTokens() }
    }

    @Test
    fun `isLoggedIn should delegate to tokenManager`() = runTest {
        every { tokenManager.isLoggedIn() } returns flowOf(true)

        repository.isLoggedIn().test {
            assertTrue(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `isLoggedIn should emit false when not logged in`() = runTest {
        every { tokenManager.isLoggedIn() } returns flowOf(false)

        repository.isLoggedIn().test {
            assertFalse(awaitItem())
            awaitComplete()
        }
    }

    private fun stubPart(name: String): MultipartBody.Part = MultipartBody.Part.createFormData(
        name = name,
        filename = null,
        body = "stub".toRequestBody("text/plain".toMediaType()),
    )

    private fun httpException(code: Int, body: String?): HttpException {
        val errorBody = (body ?: "").toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, errorBody))
    }
}
