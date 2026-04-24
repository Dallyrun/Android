package com.inseong.dallyrun.feature.splash

import app.cash.turbine.test
import com.inseong.dallyrun.core.domain.auth.GetLoginStateUseCase
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getLoginState = mockk<GetLoginStateUseCase>()

    @Test
    fun `should emit NavigateToHome after delay when logged in`() = runTest {
        every { getLoginState() } returns flowOf(true)

        val vm = SplashViewModel(getLoginState)

        vm.sideEffect.test {
            advanceTimeBy(1500)
            runCurrent()
            assertEquals(SplashSideEffect.NavigateToHome, awaitItem())
        }
    }

    @Test
    fun `should emit NavigateToLogin after delay when not logged in`() = runTest {
        every { getLoginState() } returns flowOf(false)

        val vm = SplashViewModel(getLoginState)

        vm.sideEffect.test {
            advanceTimeBy(1500)
            runCurrent()
            assertEquals(SplashSideEffect.NavigateToLogin, awaitItem())
        }
    }

    @Test
    fun `should not emit before minimum 1500ms even if token check is instant`() = runTest {
        every { getLoginState() } returns flowOf(true)

        val vm = SplashViewModel(getLoginState)

        vm.sideEffect.test {
            advanceTimeBy(1000)
            runCurrent()
            expectNoEvents()
            advanceTimeBy(500)
            runCurrent()
            assertEquals(SplashSideEffect.NavigateToHome, awaitItem())
        }
    }
}
