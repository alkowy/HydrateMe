package core.common_components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButtonWithContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primary,
    enabled: Boolean = true,
    content: @Composable() (RowScope.() -> Unit),
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = backgroundColor.copy(alpha = 0.36f)
        ),
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        content()
    }
}