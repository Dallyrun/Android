package com.inseong.dallyrun.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme
import com.inseong.dallyrun.feature.signup.components.SignupProgressBar

@Composable
internal fun SignupEmailScreen(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showInvalidEmailHint = uiState.email.isNotBlank() && !uiState.canProceedFromEmail

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        SignupProgressBar(currentStep = 1)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.signup_email_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.signup_email_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onEvent(SignupUiEvent.OnEmailChange(it)) },
            label = { Text(text = stringResource(id = R.string.signup_email_label)) },
            placeholder = { Text(text = stringResource(id = R.string.signup_email_placeholder)) },
            singleLine = true,
            isError = showInvalidEmailHint,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            supportingText = if (showInvalidEmailHint) {
                { Text(text = stringResource(id = R.string.signup_email_invalid)) }
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            enabled = uiState.canProceedFromEmail,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(text = stringResource(id = R.string.signup_next))
        }
    }
}

@Preview
@Composable
private fun PreviewSignupEmailEmpty() {
    DallyrunTheme {
        SignupEmailScreen(uiState = SignupUiState(), onEvent = {}, onNext = {})
    }
}

@Preview
@Composable
private fun PreviewSignupEmailValid() {
    DallyrunTheme {
        SignupEmailScreen(
            uiState = SignupUiState(email = "runner@dallyrun.com"),
            onEvent = {},
            onNext = {},
        )
    }
}
