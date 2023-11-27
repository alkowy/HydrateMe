package core.common_components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
private fun LoadingBlurredSurface() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(25.dp)
    ) {
        drawRect(Color.Gray.copy(alpha = 0.7f))

    }
    CircularProgressIndicator()
}