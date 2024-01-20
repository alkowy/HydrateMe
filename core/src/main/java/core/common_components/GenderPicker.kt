package core.common_components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import core.model.Gender
import core.ui.theme.backgroundContainer
import core.util.clickableOnce
import java.util.Locale

@Composable
fun GenderPicker(
    modifier: Modifier = Modifier,
    genderName: String,
    @DrawableRes imageId: Int,
    onClick: () -> Unit,
    isSelected: Boolean = false,
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .size(165.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.backgroundContainer
        )
    ) {
        Column(
            modifier = modifier
                .clickableOnce { onClick() }
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(painter = painterResource(id = imageId), contentDescription = genderName)
            Text(
                text = genderName,
                style = MaterialTheme.typography.h4,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground
            )
        }
    }
}