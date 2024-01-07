package core.util

fun Double.toStringWithUnit(unit: String): String {
    return "$this $unit"
}

fun Double.roundAndAddUnit(decimals: Int = 2, unit: String): String {
    return "${this.roundToString(decimals)} $unit"
}


private fun Double.roundToString(decimals: Int = 2): String = "%.${decimals}f".format(this)
