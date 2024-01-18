package core.common_components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.azmarzly.core.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, datePickerState: DatePickerState) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background,
            )
        ) {
            androidx.compose.material3.DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    RoundedButtonWithContent(onClick = onConfirm) {
                        Text(text = stringResource(R.string.confirm))
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colors.background,
                    subheadContentColor = MaterialTheme.colors.onBackground,
                )
            ) {
                DatePicker(
                    state = datePickerState, showModeToggle = false, title = null,
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colors.background,
                        selectedDayContainerColor = MaterialTheme.colors.primary,
                        selectedDayContentColor = MaterialTheme.colors.onPrimary,
                        todayDateBorderColor = MaterialTheme.colors.primary,
                        dayContentColor = MaterialTheme.colors.onBackground,
                        todayContentColor = MaterialTheme.colors.onBackground,
                        dayInSelectionRangeContentColor = MaterialTheme.colors.onBackground,
                        yearContentColor = MaterialTheme.colors.onBackground,
                        titleContentColor = MaterialTheme.colors.onBackground,
                        headlineContentColor = MaterialTheme.colors.onBackground,
                        currentYearContentColor = MaterialTheme.colors.onBackground,
                        selectedYearContainerColor = MaterialTheme.colors.primary,
                        selectedYearContentColor = MaterialTheme.colors.onPrimary,
                        subheadContentColor = MaterialTheme.colors.onBackground,
                    )
                )
            }
        }
    }
}