package com.inseong.dallyrun.feature.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme

@Composable
internal fun MyScreen(
    uiState: MyUiState,
    onEvent: (MyUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.my_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.my_placeholder),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onEvent(MyUiEvent.OnLogoutClick) },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(text = stringResource(id = R.string.my_logout))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { onEvent(MyUiEvent.OnDeleteAccountClick) },
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            Text(text = stringResource(id = R.string.my_delete_account))
        }
    }

    if (uiState.isLogoutDialogVisible) {
        LogoutConfirmDialog(
            isLoading = uiState.isLoading,
            onConfirm = { onEvent(MyUiEvent.OnLogoutConfirm) },
            onDismiss = { onEvent(MyUiEvent.OnLogoutDismiss) },
        )
    }

    if (uiState.isDeleteDialogVisible) {
        DeleteAccountDialog(
            password = uiState.deletePasswordInput,
            errorMessage = uiState.errorMessage,
            isLoading = uiState.isLoading,
            canSubmit = uiState.canSubmitDelete,
            onPasswordChange = { onEvent(MyUiEvent.OnDeletePasswordChange(it)) },
            onConfirm = { onEvent(MyUiEvent.OnDeleteConfirm) },
            onDismiss = { onEvent(MyUiEvent.OnDeleteDismiss) },
        )
    }
}

@Composable
private fun LogoutConfirmDialog(
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.my_logout_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.my_logout_dialog_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isLoading) {
                Text(text = stringResource(id = R.string.my_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text(text = stringResource(id = R.string.my_dialog_cancel))
            }
        },
    )
}

@Composable
private fun DeleteAccountDialog(
    password: String,
    errorMessage: String?,
    isLoading: Boolean,
    canSubmit: Boolean,
    onPasswordChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.my_delete_dialog_title)) },
        text = {
            Column {
                Text(
                    text = stringResource(id = R.string.my_delete_dialog_message),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text(text = stringResource(id = R.string.my_delete_password_label)) },
                    singleLine = true,
                    enabled = !isLoading,
                    isError = errorMessage != null,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    supportingText = if (errorMessage != null) {
                        { Text(text = errorMessage) }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = canSubmit,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
            ) {
                Text(text = stringResource(id = R.string.my_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text(text = stringResource(id = R.string.my_dialog_cancel))
            }
        },
    )
}

@Preview
@Composable
private fun PreviewMyScreen() {
    DallyrunTheme {
        MyScreen(uiState = MyUiState(), onEvent = {})
    }
}

@Preview
@Composable
private fun PreviewMyScreenDeleteDialog() {
    DallyrunTheme {
        MyScreen(
            uiState = MyUiState(
                isDeleteDialogVisible = true,
                deletePasswordInput = "wrong",
                errorMessage = "비밀번호가 올바르지 않아요",
            ),
            onEvent = {},
        )
    }
}
