package com.inseong.dallyrun.core.data.auth

import app.cash.turbine.test
import com.inseong.dallyrun.core.network.AuthApi
import com.inseong.dallyrun.core.network.model.ApiResponse
import com.inseong.dallyrun.core.network.model.NetworkTokenResponse
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryImplTest {

    private val authApi = mockk<AuthApi>(relaxUnitFun = true)
    private val tokenManager = mockk<TokenManagerImpl>(relaxUnitFun = true)
    private val repository = AuthRepositoryImpl(authApi, tokenManager)

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
}
