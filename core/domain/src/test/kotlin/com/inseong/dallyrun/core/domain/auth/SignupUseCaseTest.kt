package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SignupUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = SignupUseCase(authRepository)

    @Test
    fun `should delegate to repository with all fields`() = runTest {
        val expected = AuthToken(accessToken = "access", refreshToken = "refresh")
        coEvery {
            authRepository.signup("a@b.c", "12345678", "runner", "content://uri")
        } returns expected

        val result = useCase("a@b.c", "12345678", "runner", "content://uri")

        assertEquals(expected, result)
        coVerify { authRepository.signup("a@b.c", "12345678", "runner", "content://uri") }
    }

    @Test
    fun `should pass null profileImageUri through`() = runTest {
        val expected = AuthToken(accessToken = "a", refreshToken = "r")
        coEvery {
            authRepository.signup("a@b.c", "12345678", "runner", null)
        } returns expected

        val result = useCase("a@b.c", "12345678", "runner", null)

        assertEquals(expected, result)
    }
}
