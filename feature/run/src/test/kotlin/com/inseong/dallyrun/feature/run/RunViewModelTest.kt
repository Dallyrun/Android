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

    @Test
    fun `should emit LaunchPermissionRequest when RequestPermission event`() = runTest {
        val viewModel = RunViewModel()

        viewModel.sideEffect.test {
            viewModel.onEvent(RunUiEvent.RequestPermission)

            assertEquals(RunSideEffect.LaunchPermissionRequest, awaitItem())
        }
    }

    @Test
    fun `should set granted true and hasRequestedPermission true when OnPermissionResult granted`() {
        val viewModel = RunViewModel()

        viewModel.onEvent(RunUiEvent.OnPermissionResult(granted = true))

        val state = viewModel.uiState.value
        assertTrue(state.locationPermissionGranted)
        assertTrue(state.hasRequestedPermission)
    }

    @Test
    fun `should set granted false and hasRequestedPermission true when OnPermissionResult denied`() {
        val viewModel = RunViewModel()

        viewModel.onEvent(RunUiEvent.OnPermissionResult(granted = false))

        val state = viewModel.uiState.value
        assertFalse(state.locationPermissionGranted)
        assertTrue(state.hasRequestedPermission)
    }

    @Test
    fun `should update granted but keep hasRequestedPermission unchanged when OnPermissionStateChanged`() {
        val viewModel = RunViewModel()

        viewModel.onEvent(RunUiEvent.OnPermissionStateChanged(granted = true))

        val state = viewModel.uiState.value
        assertTrue(state.locationPermissionGranted)
        assertFalse(state.hasRequestedPermission)
    }

    @Test
    fun `should emit OpenAppSettings side effect when OpenAppSettings event`() = runTest {
        val viewModel = RunViewModel()

        viewModel.sideEffect.test {
            viewModel.onEvent(RunUiEvent.OpenAppSettings)

            assertEquals(RunSideEffect.OpenAppSettings, awaitItem())
        }
    }
}
