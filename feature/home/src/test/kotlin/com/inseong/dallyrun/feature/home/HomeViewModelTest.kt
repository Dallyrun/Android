package com.inseong.dallyrun.feature.home

import app.cash.turbine.test
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should keep initial state on creation`() {
        val vm = HomeViewModel()
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should emit NavigateToRun when start run clicked`() = runTest {
        val vm = HomeViewModel()
        vm.sideEffect.test {
            vm.onEvent(HomeUiEvent.OnStartRunClick)
            assertEquals(HomeSideEffect.NavigateToRun, awaitItem())
        }
    }
}
