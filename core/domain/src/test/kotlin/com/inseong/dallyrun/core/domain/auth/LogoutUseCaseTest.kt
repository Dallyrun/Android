package com.inseong.dallyrun.core.domain.auth

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LogoutUseCaseTest {

    private val authRepository = mockk<AuthRepository>(relaxUnitFun = true)
    private val useCase = LogoutUseCase(authRepository)

    @Test
    fun `should delegate to repository logout`() = runTest {
        useCase()

        coVerify(exactly = 1) { authRepository.logout() }
    }
}
