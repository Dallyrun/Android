package com.inseong.dallyrun.feature.signup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun SignupProfileRoute(
    viewModel: SignupViewModel,
    onSignupComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        viewModel.onEvent(SignupUiEvent.OnProfileImageSelected(uri?.toString()))
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SignupSideEffect.NavigateToHome -> onSignupComplete()
            }
        }
    }

    SignupProfileScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onPickImage = {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        onSubmit = { viewModel.onEvent(SignupUiEvent.OnSubmit) },
        modifier = modifier,
    )
}
