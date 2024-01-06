package com.azmarzly.home.components

import android.provider.Settings.Global.getString
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.azmarzly.core.R.*
import core.common_components.PlaneValidatedTextField
import core.common_components.RoundedButtonWithContent
import core.input_validators.ValidationState
import core.model.DrinkType


@Composable
fun AddWaterDialog(
    onDismissRequest: () -> Unit,
    addHydrationAction: (Int) -> Unit,
    validateCustomAmount: (String) -> ValidationState,
) {
    val customAmountState = remember {
        mutableStateOf("")
    }
    var hydrationAmountToAdd by remember {
        mutableIntStateOf(0)
    }
    var drinkTypeSelected by remember {
        mutableStateOf<DrinkType?>(null)
    }
    Dialog(
        onDismissRequest = { onDismissRequest() },
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
            Column(
                modifier = Modifier.padding(
                    top = 32.dp,
                    bottom = 40.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textFocusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                Text(
                    text = stringResource(string.add_drink),
                    style = MaterialTheme.typography.h3
                )

                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DrinkType.entries.filterNot { it == DrinkType.CUSTOM }.forEach { drinkType ->
                        AddWaterItem(
                            drinkType = drinkType,
                            onSelected = {
                                drinkTypeSelected = drinkType
                                customAmountState.value = ""
                                hydrationAmountToAdd = drinkType.amountOfWater
                                focusManager.clearFocus()
                            },
                            isSelected = drinkTypeSelected == drinkType
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(string.or))
                Spacer(modifier = Modifier.height(16.dp))

                PlaneValidatedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(textFocusRequester),
                    value = customAmountState,
                    onValueChange = {
                        drinkTypeSelected = null
                        hydrationAmountToAdd = if (validateCustomAmount(it) == ValidationState.Valid) {
                            customAmountState.value.toInt()
                        } else {
                            0
                        }
                    },
                    label = "Wpisz pojemność",
                    style = MaterialTheme.typography.body1,
                    trailingIcon = {
                        Text(text = "ml", style = MaterialTheme.typography.caption)
                    },
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(32.dp))

                RoundedButtonWithContent(
                    onClick = { addHydrationAction(hydrationAmountToAdd) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = hydrationAmountToAdd > 0,
                ) {
                    Text(
                        text = stringResource(string.confirm),
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier.clickable { onDismissRequest() },
                    text = stringResource(string.cancel),
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                )
            }
        }

    }
}