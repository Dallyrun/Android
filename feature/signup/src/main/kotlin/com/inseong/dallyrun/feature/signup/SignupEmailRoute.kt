package com.inseong.dallyrun.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun SignupEmailRoute(
    viewModel: SignupViewModel,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignupEmailScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNext = onNext,
        modifier = modifier,
    )
}
