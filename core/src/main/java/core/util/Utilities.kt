package core.util

import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.util.MeasureUnit
import java.util.Locale

fun doNothing() = Unit

fun Number.toPercent() = NumberFormatter.withLocale(Locale.getDefault())
    .unit(MeasureUnit.PERCENT)
    .precision(Precision.maxFraction(0))
    .format(this)
    .toString()