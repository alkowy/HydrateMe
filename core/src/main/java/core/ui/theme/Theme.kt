package core.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Blue500,
    secondary = Blue400,
    primaryVariant = Blue400,
    surface = VeryDarkBlue,
    background = Grey900,
    onBackground = VeryWhite,
    onSurface = Blue100,
    onPrimary = Blue900

)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Blue600,
    primaryVariant = Blue300,
    secondary = Blue300,
    surface = Grey300,
    background = VeryWhite,
    onBackground = Grey900,
    onSurface = Blue900,
    onPrimary = Grey100,


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
    get() = if (isLight) Grey200 else Grey600

val Colors.verticalFilledProgressColor: Color
    get() = if (isLight) Grey300 else Grey500

val Colors.compactCalendarProgressText: Color
    get() = if (isLight) Grey400 else Grey400

val Colors.caption: Color
    get() = if (isLight) Blue900 else Blue100

val Colors.textFieldLabel: Color
    get() = if (isLight) Grey500 else Grey200

val Colors.backgroundContainer: Color
    get() = if (isLight) Grey200 else Grey600

val Colors.registrationStepDot: Color
    get() = if (isLight) Grey300 else Grey600

val Colors.registrationTextColor: Color
    get() = if (isLight) Grey900 else Grey100

val Colors.remainingOutOfTextColor: Color
    get() = if (isLight) Grey400 else Grey600

//val Colors.registrationSurface: Color
//    get() = if (isLight) VeryWhite else Grey800

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