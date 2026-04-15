package com.inseong.dallyrun.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun HistoryScreen(
    uiState: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement history list UI
}

@Preview
@Composable
private fun PreviewHistoryScreen() {
    HistoryScreen(
        uiState = HistoryUiState(),
        onEvent = {},
    )
}
