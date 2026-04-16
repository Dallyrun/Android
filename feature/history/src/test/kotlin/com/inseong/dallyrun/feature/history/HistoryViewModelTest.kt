package com.inseong.dallyrun.feature.history

import app.cash.turbine.test
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should set isLoading true when LoadHistory event`() {
        val viewModel = HistoryViewModel()

        viewModel.onEvent(HistoryUiEvent.LoadHistory)

        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `should emit NavigateToRunDetail with run id when SelectRun`() = runTest {
        val viewModel = HistoryViewModel()

        viewModel.sideEffect.test {
            viewModel.onEvent(HistoryUiEvent.SelectRun(runId = 42L))

            assertEquals(HistorySideEffect.NavigateToRunDetail(runId = 42L), awaitItem())
        }
    }
}
