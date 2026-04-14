package com.inseong.dallyrun.feature.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun RunScreen(
    modifier: Modifier = Modifier,
    viewModel: RunViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RunScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
internal fun RunScreen(
    uiState: RunUiState,
    onEvent: (RunUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement run tracking UI
}
