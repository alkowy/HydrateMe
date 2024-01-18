package core.common_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.model.UserActivity
import core.ui.theme.Grey400
import core.ui.theme.backgroundContainer
import core.ui.theme.bodySmall
import core.ui.theme.registrationTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCard(
    activity: UserActivity,
    isSelected: Boolean = false,
    onClick: (UserActivity) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.backgroundContainer
        ),
        onClick = { onClick(activity) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = activity.name,
                style = MaterialTheme.typography.h4,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.registrationTextColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodySmall,
                color = Grey400
            )
        }
    }
}