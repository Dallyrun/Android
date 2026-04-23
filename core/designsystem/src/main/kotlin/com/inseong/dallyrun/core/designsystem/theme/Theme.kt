package com.inseong.dallyrun.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DallyrunLightColorScheme = lightColorScheme(
    primary = Brand40,
    onPrimary = Color.White,
    primaryContainer = Brand90,
    onPrimaryContainer = Brand10,

    secondary = Neutral40,
    onSecondary = Color.White,
    secondaryContainer = Neutral90,
    onSecondaryContainer = Neutral10,

    tertiary = Neutral40,
    onTertiary = Color.White,
    tertiaryContainer = Neutral90,
    onTertiaryContainer = Neutral10,

    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Neutral90,
    onSurfaceVariant = Neutral30,

    outline = Neutral50,
    outlineVariant = Neutral80,

    error = Error40,
    onError = Color.White,
    errorContainer = Error90,
    onErrorContainer = Error10,
)

private val DallyrunDarkColorScheme = darkColorScheme(
    primary = Brand80,
    onPrimary = Brand20,
    primaryContainer = Brand30,
    onPrimaryContainer = Brand90,

    secondary = Neutral80,
    onSecondary = Neutral20,
    secondaryContainer = Neutral30,
    onSecondaryContainer = Neutral90,

    tertiary = Neutral80,
    onTertiary = Neutral20,
    tertiaryContainer = Neutral30,
    onTertiaryContainer = Neutral90,

    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = Neutral30,
    onSurfaceVariant = Neutral80,

    outline = Neutral60,
    outlineVariant = Neutral30,

    error = Error80,
    onError = Error20,
    errorContainer = Error30,
    onErrorContainer = Error90,
)

@Composable
fun DallyrunTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 기본값 false: Android 12+ 월페이퍼 채색이 브랜드 색을 덮지 않게 함.
    // 호출부에서 명시적으로 true 로 켜면 시스템 채색을 따른다.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DallyrunDarkColorScheme
        else -> DallyrunLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
