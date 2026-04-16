package com.inseong.dallyrun.feature.run

import app.cash.turbine.test
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RunViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should set isRunning true when StartRun event`() {
        val viewModel = RunViewModel()

        viewModel.onEvent(RunUiEvent.StartRun)

        assertTrue(viewModel.uiState.value.isRunning)
    }

    @Test
    fun `should set isRunning false when PauseRun event`() {
        val viewModel = RunViewModel()
        viewModel.onEvent(RunUiEvent.StartRun)

        viewModel.onEvent(RunUiEvent.PauseRun)

        assertFalse(viewModel.uiState.value.isRunning)
    }

    @Test
    fun `should reset state and emit NavigateToHistory when StopRun`() = runTest {
        val viewModel = RunViewModel()
        viewModel.onEvent(RunUiEvent.StartRun)

        viewModel.sideEffect.test {
            viewModel.onEvent(RunUiEvent.StopRun)

            assertEquals(RunSideEffect.NavigateToHistory, awaitItem())
        }

        val state = viewModel.uiState.value
        assertFalse(state.isRunning)
        assertEquals(0.0, state.distanceMeters, 0.0)
        assertEquals(0L, state.durationMillis)
    }
}
