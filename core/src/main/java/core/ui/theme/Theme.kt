package core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Blue500,
    secondary = Blue400,
    primaryVariant = Blue400,
    surface = VeryDarkBlue,
    background = Grey800,
    onBackground = Blue100,
    onSurface = Blue100

)

private val LightColorPalette = lightColors(
    primary = Blue600,
    primaryVariant = Blue300,
    secondary = Blue300,
    surface = Grey300,
    background = Blue100,
    onBackground = Blue900,
    onSurface = Blue900


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val Colors.blue200: Color
    get() = if (isLight) Color.Blue else Color.Red
val Colors.emptyProgressColor: Color
    get() = if (isLight) Grey300 else Grey500

val Colors.caption: Color
    get() = if (isLight) Blue900 else Blue100

@Composable
fun HydrateMeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}