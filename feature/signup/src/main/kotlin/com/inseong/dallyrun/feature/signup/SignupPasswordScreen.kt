package com.inseong.dallyrun.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme
import com.inseong.dallyrun.core.domain.auth.isValidPassword
import com.inseong.dallyrun.feature.signup.components.SignupProgressBar

@Composable
internal fun SignupPasswordScreen(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val passwordInvalid = uiState.password.isNotBlank() && !isValidPassword(uiState.password)
    val confirmMismatch = uiState.passwordConfirm.isNotBlank() &&
        uiState.password != uiState.passwordConfirm

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        SignupProgressBar(currentStep = 2)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.signup_password_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.signup_password_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { onEvent(SignupUiEvent.OnPasswordChange(it)) },
            label = { Text(text = stringResource(id = R.string.signup_password_label)) },
            singleLine = true,
            isError = passwordInvalid,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            ),
            supportingText = if (passwordInvalid) {
                { Text(text = stringResource(id = R.string.signup_password_invalid)) }
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.passwordConfirm,
            onValueChange = { onEvent(SignupUiEvent.OnPasswordConfirmChange(it)) },
            label = { Text(text = stringResource(id = R.string.signup_password_confirm_label)) },
            singleLine = true,
            isError = confirmMismatch,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            supportingText = if (confirmMismatch) {
                { Text(text = stringResource(id = R.string.signup_password_mismatch)) }
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            enabled = uiState.canProceedFromPassword,
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
private fun PreviewSignupPasswordValid() {
    DallyrunTheme {
        SignupPasswordScreen(
            uiState = SignupUiState(password = "12345678", passwordConfirm = "12345678"),
            onEvent = {},
            onNext = {},
        )
    }
}
