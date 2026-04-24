package com.inseong.dallyrun.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.dallyrun.core.designsystem.R as DesignSystemR
import com.inseong.dallyrun.core.designsystem.theme.DallyrunTheme

@Composable
internal fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = DesignSystemR.drawable.dallyrun_logo),
            contentDescription = stringResource(id = R.string.splash_logo_description),
            modifier = Modifier
                .size(120.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(28.dp), clip = false),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.splash_tagline),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview
@Composable
private fun PreviewSplashScreen() {
    DallyrunTheme {
        SplashScreen()
    }
}
