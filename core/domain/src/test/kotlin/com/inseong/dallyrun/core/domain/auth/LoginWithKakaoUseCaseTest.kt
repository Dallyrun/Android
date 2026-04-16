package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginWithKakaoUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = LoginWithKakaoUseCase(authRepository)

    @Test
    fun `should delegate to repository with given auth code`() = runTest {
        val expected = AuthToken(accessToken = "access", refreshToken = "refresh")
        coEvery { authRepository.loginWithKakao("kakao-code") } returns expected

        val result = useCase("kakao-code")

        assertEquals(expected, result)
        coVerify { authRepository.loginWithKakao("kakao-code") }
    }

    @Test
    fun `should propagate repository exception`() = runTest {
        coEvery { authRepository.loginWithKakao(any()) } throws RuntimeException("boom")

        val thrown = runCatching { useCase("code") }.exceptionOrNull()

        assertEquals("boom", thrown?.message)
    }
}
