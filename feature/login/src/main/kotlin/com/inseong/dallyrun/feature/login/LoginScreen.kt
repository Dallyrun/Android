package com.inseong.dallyrun.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.dallyrun_logo),
            contentDescription = stringResource(id = R.string.login_logo_description),
            modifier = Modifier.size(96.dp),
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onEvent(LoginUiEvent.OnEmailChange(it)) },
            label = { Text(text = stringResource(id = R.string.login_email_label)) },
            placeholder = { Text(text = stringResource(id = R.string.login_email_placeholder)) },
            singleLine = true,
            enabled = !uiState.isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { onEvent(LoginUiEvent.OnPasswordChange(it)) },
            label = { Text(text = stringResource(id = R.string.login_password_label)) },
            placeholder = { Text(text = stringResource(id = R.string.login_password_placeholder)) },
            singleLine = true,
            enabled = !uiState.isLoading,
            visualTransformation = if (uiState.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            trailingIcon = {
                TextButton(onClick = { onEvent(LoginUiEvent.OnPasswordVisibilityToggle) }) {
                    Text(
                        text = stringResource(
                            id = if (uiState.isPasswordVisible) {
                                R.string.login_password_hide
                            } else {
                                R.string.login_password_show
                            },
                        ),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEvent(LoginUiEvent.OnLoginClick) },
            enabled = uiState.canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(text = stringResource(id = R.string.login_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.login_signup_question),
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(
                onClick = { onEvent(LoginUiEvent.OnSignupClick) },
                enabled = !uiState.isLoading,
            ) {
                Text(text = stringResource(id = R.string.login_signup_link))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoginScreenEmpty() {
    DallyrunTheme {
        LoginScreen(uiState = LoginUiState(), onEvent = {})
    }
}

@Preview
@Composable
private fun PreviewLoginScreenFilled() {
    DallyrunTheme {
        LoginScreen(
            uiState = LoginUiState(
                email = "runner@dallyrun.com",
                password = "12345678",
                errorMessage = "이메일 또는 비밀번호를 다시 확인해주세요",
            ),
            onEvent = {},
        )
    }
}
