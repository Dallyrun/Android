package com.inseong.dallyrun.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme

private val KakaoYellow = Color(0xFFFEE500)
private val KakaoLabelBlack = Color(0xFF191919)

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.dallyrun_logo),
            contentDescription = stringResource(id = R.string.login_logo_description),
            modifier = Modifier.size(120.dp),
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onEvent(LoginUiEvent.OnKakaoLoginClick) },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KakaoYellow,
                contentColor = KakaoLabelBlack,
            ),
        ) {
            Text(text = stringResource(id = R.string.login_kakao))
        }
    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    DallyrunTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onEvent = {},
        )
    }
}
