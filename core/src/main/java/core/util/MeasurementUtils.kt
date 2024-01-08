package core.util

import java.text.DecimalFormat

fun Double.toStringWithUnit(unit: String): String {
    val format = DecimalFormat("0.#") // remove trailing zeroes
    val formatted = format.format(this)
    return "$formatted $unit"
}

fun Double.roundAndAddUnit(decimals: Int = 2, unit: String): String {
    return "${this.roundToString(decimals)} $unit"
}


private fun Double.roundToString(decimals: Int = 2): String = "%.${decimals}f".format(this)
