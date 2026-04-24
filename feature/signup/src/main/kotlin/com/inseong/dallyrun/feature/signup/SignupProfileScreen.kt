package com.inseong.dallyrun.feature.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme
import com.inseong.dallyrun.core.domain.auth.isValidNickname
import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.Gender
import com.inseong.dallyrun.feature.signup.components.SignupProgressBar

@Composable
internal fun SignupProfileScreen(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit,
    onPickImage: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nicknameInvalid = uiState.nickname.isNotEmpty() && !isValidNickname(uiState.nickname)

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        SignupProgressBar(currentStep = 3)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.signup_profile_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.signup_profile_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileImagePicker(
            imageUri = uiState.profileImageUri,
            onClick = onPickImage,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(8.dp))
        if (uiState.profileImageUri != null) {
            TextButton(
                onClick = onPickImage,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(text = stringResource(id = R.string.signup_profile_image_change))
            }
        } else {
            Text(
                text = stringResource(id = R.string.signup_profile_image_required),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.nickname,
            onValueChange = { onEvent(SignupUiEvent.OnNicknameChange(it)) },
            label = { Text(text = stringResource(id = R.string.signup_nickname_label)) },
            placeholder = { Text(text = stringResource(id = R.string.signup_nickname_placeholder)) },
            singleLine = true,
            isError = nicknameInvalid,
            enabled = !uiState.isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            supportingText = if (nicknameInvalid) {
                { Text(text = stringResource(id = R.string.signup_nickname_invalid)) }
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AgeGroupDropdown(
            selected = uiState.ageGroup,
            expanded = uiState.isAgeDropdownExpanded,
            enabled = !uiState.isLoading,
            onExpandRequest = { onEvent(SignupUiEvent.OnAgeDropdownExpand) },
            onDismissRequest = { onEvent(SignupUiEvent.OnAgeDropdownDismiss) },
            onSelect = { onEvent(SignupUiEvent.OnAgeGroupSelect(it)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        GenderSelector(
            selected = uiState.gender,
            onSelect = { onEvent(SignupUiEvent.OnGenderSelect(it)) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSubmit,
            enabled = uiState.canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(text = stringResource(id = R.string.signup_submit))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgeGroupDropdown(
    selected: AgeGroup?,
    expanded: Boolean,
    enabled: Boolean,
    onExpandRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    onSelect: (AgeGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayText = selected?.let { stringResource(id = it.labelRes()) }
        ?: stringResource(id = R.string.signup_age_placeholder)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (it) onExpandRequest() else onDismissRequest() },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = { /* read only */ },
            readOnly = true,
            enabled = enabled,
            label = { Text(text = stringResource(id = R.string.signup_age_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            AgeGroup.entries.forEach { ageGroup ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = ageGroup.labelRes())) },
                    onClick = { onSelect(ageGroup) },
                )
            }
        }
    }
}

@Composable
private fun GenderSelector(
    selected: Gender?,
    onSelect: (Gender) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.signup_gender_label),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))

        val options = Gender.entries
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, gender ->
                SegmentedButton(
                    selected = selected == gender,
                    onClick = { onSelect(gender) },
                    enabled = enabled,
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                ) {
                    Text(text = stringResource(id = gender.labelRes()))
                }
            }
        }
    }
}

@Composable
private fun ProfileImagePicker(
    imageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = stringResource(id = R.string.signup_profile_image_pick),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(id = R.string.signup_profile_image_placeholder),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSignupProfileEmpty() {
    DallyrunTheme {
        SignupProfileScreen(
            uiState = SignupUiState(),
            onEvent = {},
            onPickImage = {},
            onSubmit = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSignupProfileFilled() {
    DallyrunTheme {
        SignupProfileScreen(
            uiState = SignupUiState(
                nickname = "달리는인성",
                ageGroup = AgeGroup.THIRTIES,
                gender = Gender.MALE,
            ),
            onEvent = {},
            onPickImage = {},
            onSubmit = {},
        )
    }
}
