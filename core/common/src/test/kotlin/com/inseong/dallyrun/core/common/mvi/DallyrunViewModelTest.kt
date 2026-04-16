package com.inseong.dallyrun.core.common.mvi

import app.cash.turbine.test
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DallyrunViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private data class FakeState(val counter: Int = 0, val label: String = "init") : UiState

    private sealed interface FakeEvent : UiEvent {
        data object Increment : FakeEvent
        data class SetLabel(val value: String) : FakeEvent
        data object Emit : FakeEvent
    }

    private sealed interface FakeSideEffect : SideEffect {
        data class Message(val text: String) : FakeSideEffect
    }

    private class FakeViewModel : DallyrunViewModel<FakeState, FakeEvent, FakeSideEffect>() {

        override fun createInitialState(): FakeState = FakeState()

        override fun handleEvent(event: FakeEvent) {
            when (event) {
                FakeEvent.Increment -> updateState { copy(counter = counter + 1) }
                is FakeEvent.SetLabel -> updateState { copy(label = event.value) }
                FakeEvent.Emit -> sendSideEffect(FakeSideEffect.Message("hello"))
            }
        }
    }

    @Test
    fun `should expose initial state from createInitialState`() {
        val viewModel = FakeViewModel()

        assertEquals(FakeState(counter = 0, label = "init"), viewModel.uiState.value)
    }

    @Test
    fun `should update state through reducer when event handled`() {
        val viewModel = FakeViewModel()

        viewModel.onEvent(FakeEvent.Increment)
        viewModel.onEvent(FakeEvent.Increment)
        viewModel.onEvent(FakeEvent.SetLabel("done"))

        assertEquals(FakeState(counter = 2, label = "done"), viewModel.uiState.value)
    }

    @Test
    fun `should emit side effect through channel`() = runTest {
        val viewModel = FakeViewModel()

        viewModel.sideEffect.test {
            viewModel.onEvent(FakeEvent.Emit)
            assertEquals(FakeSideEffect.Message("hello"), awaitItem())
        }
    }
}
