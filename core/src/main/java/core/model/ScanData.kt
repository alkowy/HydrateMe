package core.model

import androidx.compose.ui.graphics.Color
import core.ui.theme.Additional200
import core.ui.theme.Additional400
import core.ui.theme.Additional700

data class ScanData(
    val color: UrineColor,
    val message: String,
    val hour: String,
)

enum class UrineColor {
    BAD, OK, GOOD, SOMETHING; // todo adjust the scale to use

    fun toColor() : Color {
       return when(this){
           BAD -> Additional700
           OK -> Additional400
           GOOD -> Additional200
           SOMETHING -> Additional400
       }
    }
}

