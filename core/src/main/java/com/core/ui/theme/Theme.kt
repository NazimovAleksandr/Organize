package com.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.core.BuildConfig

private val darkColorScheme = darkColorScheme(

)

private val lightColorScheme = lightColorScheme(
    background = SecondaryWhite,
    onBackground = PrimaryText,
    surface = PrimaryBlue,
    onSurface = SecondaryWhite,
    primary = PrimaryBlue,
    outline = PriorityNotAssigned,
    outlineVariant = SecondaryBeige,
    scrim = PrimaryLight,
)

@Composable
fun OrganizeTheme(
    darkTheme: Boolean = false, // isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.apply {
                statusBarColor = Color.Transparent.toArgb()
                navigationBarColor = Color.Transparent.toArgb()

                WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = darkTheme
            }
        }
    }

    when (BuildConfig.DEBUG) {
        true -> MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = {
                Scaffold {
                    it.calculateBottomPadding()
                    content.invoke()
                }
            }
        )

        false -> MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}