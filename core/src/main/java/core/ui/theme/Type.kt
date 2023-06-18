package core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.azmarzly.core.R

val Poppins = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_regular, FontWeight.Normal)
)

val displayTextStyle = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.Normal,
    fontSize = 34.sp,
    letterSpacing = (-0.03).sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

val buttonLabelLinkTextStyle = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = 0.02.sp,
    lineHeight = 18.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 29.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.03).sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    h2 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 25.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 30.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    h3 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 25.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    h4 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        letterSpacing = (-0.03).sp,
        lineHeight = 22.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    body1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    body2 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    button = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.01.sp,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    caption = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )
)