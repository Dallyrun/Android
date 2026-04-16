package com.inseong.dallyrun.core.domain.auth

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GetLoginStateUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = GetLoginStateUseCase(authRepository)

    @Test
    fun `should emit true when repository reports logged in`() = runTest {
        every { authRepository.isLoggedIn() } returns flowOf(true)

        useCase().test {
            assertTrue(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `should emit false when repository reports not logged in`() = runTest {
        every { authRepository.isLoggedIn() } returns flowOf(false)

        useCase().test {
            assertFalse(awaitItem())
            awaitComplete()
        }
    }
}
