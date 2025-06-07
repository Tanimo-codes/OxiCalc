package com.example.oxicalc.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),         // purple_200
    onPrimary = Color.Black,
    secondary = Color(0xFF018786),       // teal_700
    onSecondary = Color.White,
    background = Color(0xFF121212),      // dark grey instead of black
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),         // less harsh surface
    onSurface = Color.White,
    primaryContainer = Color(0xFF3700B3),// purple_700
    secondaryContainer = Color(0xFF22577A)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),         // purple_500
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC5),       // teal_200
    onSecondary = Color.Black,
    background = Color(0xFFFFFFFF),      // white
    onBackground = Color.Black,
    surface = Color(0xFFC7F9CC),         // light_green
    onSurface = Color.Black,
    primaryContainer = Color(0xFFBB86FC),// purple_200
    secondaryContainer = Color(0xFF22577A),

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun OxiCalcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}