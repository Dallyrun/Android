package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = LoginUseCase(authRepository)

    @Test
    fun `should delegate to repository with email and password`() = runTest {
        val expected = AuthToken(accessToken = "access", refreshToken = "refresh")
        coEvery { authRepository.loginWithEmail("a@b.c", "pw") } returns expected

        val result = useCase("a@b.c", "pw")

        assertEquals(expected, result)
        coVerify { authRepository.loginWithEmail("a@b.c", "pw") }
    }

    @Test
    fun `should propagate repository exception`() = runTest {
        coEvery { authRepository.loginWithEmail(any(), any()) } throws RuntimeException("boom")

        val thrown = runCatching { useCase("a@b.c", "pw") }.exceptionOrNull()

        assertEquals("boom", thrown?.message)
    }
}
