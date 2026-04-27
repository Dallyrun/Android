package com.inseong.dallyrun.feature.run

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme

@Composable
internal fun RunScreen(
    uiState: RunUiState,
    onEvent: (RunUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState.locationPermissionGranted) {
            RunReadyContent()
        } else {
            PermissionRequestContent(
                hasRequestedPermission = uiState.hasRequestedPermission,
                onRequestPermission = { onEvent(RunUiEvent.RequestPermission) },
                onOpenSettings = { onEvent(RunUiEvent.OpenAppSettings) },
            )
        }
    }
}

@Composable
private fun ColumnScope.RunReadyContent() {
    Spacer(modifier = Modifier.weight(1f))
    Text(
        text = stringResource(id = R.string.run_ready_title),
        style = MaterialTheme.typography.headlineMedium,
    )
    Text(
        text = stringResource(id = R.string.run_ready_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.weight(1f))
}

@Composable
private fun ColumnScope.PermissionRequestContent(
    hasRequestedPermission: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Spacer(modifier = Modifier.height(24.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(id = R.string.run_permission_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(id = R.string.run_permission_rationale),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (hasRequestedPermission) {
                Text(
                    text = stringResource(id = R.string.run_permission_denied_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }

    Spacer(modifier = Modifier.weight(1f))

    Button(
        onClick = onRequestPermission,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.run_permission_grant),
            style = MaterialTheme.typography.titleMedium,
        )
    }

    if (hasRequestedPermission) {
        OutlinedButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.run_permission_open_settings),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRunScreenPermissionInitial() {
    DallyrunTheme {
        RunScreen(
            uiState = RunUiState(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewRunScreenPermissionDenied() {
    DallyrunTheme {
        RunScreen(
            uiState = RunUiState(hasRequestedPermission = true),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewRunScreenReady() {
    DallyrunTheme {
        RunScreen(
            uiState = RunUiState(locationPermissionGranted = true, hasRequestedPermission = true),
            onEvent = {},
        )
    }
}
