package core.common_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.azmarzly.core.R
import core.ui.theme.bodySmall

@Composable
fun ResetPasswordDialog(onDismiss: () -> Unit, email: String, onConfirm: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.reset_password_body_with_placeholder).replace("PLACEHOLDER", email),
                    style = MaterialTheme.typography.bodySmall.copy(
                        MaterialTheme.colors.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                RoundedButtonWithContent(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                ) {
                    Text(text = stringResource(id = R.string.send))
                }
            }
        }
    }

}