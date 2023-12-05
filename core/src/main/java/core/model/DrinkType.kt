package core.model

import androidx.annotation.DrawableRes
import com.azmarzly.core.R

enum class DrinkType(
    @DrawableRes val resIdSelected: Int,
    @DrawableRes val resIdUnselected: Int,
    val amountOfWater: Int,
) {
    CUSTOM(
        amountOfWater = -1,
        resIdUnselected = R.drawable.ic_water_custom_outlined,
        resIdSelected = R.drawable.ic_water_custom_outlined
    ),
    CUP(
        amountOfWater = 250,
        resIdSelected = R.drawable.ic_cup_filled,
        resIdUnselected = R.drawable.ic_cup_outlined
    ),
    BIG_CUP(
        amountOfWater = 330,
        resIdSelected = R.drawable.ic_big_cup_filled,
        resIdUnselected = R.drawable.ic_big_cup_outlined
    ),
    BOTTLE(
        amountOfWater = 500,
        resIdSelected = R.drawable.ic_bottle_filled,
        resIdUnselected = R.drawable.ic_bottle_outlined
    );
}