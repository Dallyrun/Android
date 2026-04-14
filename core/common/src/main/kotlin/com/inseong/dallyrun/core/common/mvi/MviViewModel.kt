package com.inseong.dallyrun.core.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<S : UiState, E : UiEvent, F : SideEffect> : ViewModel() {

    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(createInitialState()) }
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _sideEffect = Channel<F>(Channel.BUFFERED)
    val sideEffect: Flow<F> = _sideEffect.receiveAsFlow()

    abstract fun createInitialState(): S

    abstract fun handleEvent(event: E)

    fun onEvent(event: E) {
        handleEvent(event)
    }

    protected fun updateState(reducer: S.() -> S) {
        _uiState.update { it.reducer() }
    }

    protected fun sendSideEffect(effect: F) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
