package com.inseong.dallyrun.feature.run

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme

@Composable
internal fun RunScreen(
    uiState: RunUiState,
    onEvent: (RunUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement run tracking UI
}

@Preview
@Composable
private fun PreviewRunScreen() {
    DallyrunTheme {
        RunScreen(
            uiState = RunUiState(),
            onEvent = {},
        )
    }
}
