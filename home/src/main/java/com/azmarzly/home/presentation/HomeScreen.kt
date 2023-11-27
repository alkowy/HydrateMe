package com.azmarzly.home.presentation

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.azmarzly.core.R
import com.azmarzly.home.components.DropletButtonNavBar
import com.azmarzly.home.presentation.HomeScreenParentViewModel.Companion.homeScreens
import core.common_components.PlaneValidatedTextField
import core.common_components.RoundedButtonWithContent
import core.input_validators.ValidationState
import core.ui.theme.VeryWhite
import core.ui.theme.backgroundContainer
import core.util.HomeRoute
import core.util.navigateTo
import java.time.LocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ParentHomeScreen(
    navController: NavHostController = rememberNavController(),
    navigateToSignIn: () -> Unit,
) {

    val homeViewModel: HomeViewModel = hiltViewModel()
    var showAddWaterCard by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            bottomBar = {
                DropletButtonNavBar(
                    dropletButtons = homeScreens,
                    barColor = Color.White.copy(alpha = 0.9F),
                    onNavigateToHome = {
                        navController.popBackStack()
                        navController.navigateTo(HomeRoute.HOME_ROOT) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToCalendar = {
                        navController.navigateTo(HomeRoute.CALENDAR) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigateTo(HomeRoute.PROFILE) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToNews = {
                        navController.navigateTo(HomeRoute.NEWS) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onFloatingActionButtonAction = { showAddWaterCard = true }
                )
            }
        ) {
            HomeNavGraph(
                navController = navController,
                navigateToSignIn = navigateToSignIn,
                homeViewModel = homeViewModel,
            )

            if (showAddWaterCard) {
                AddWaterDialog(
                    onDismissRequest = { showAddWaterCard = false },
                    addHydrationAction = { amount ->
                        homeViewModel.addHydration(LocalDate.now(), amount)
                        showAddWaterCard = false
                    },
                    validateCustomAmount = homeViewModel::validateNumber,
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(25.dp)
    ) {
        drawRect(Color.Gray.copy(alpha = 0.7f))

    }
    CircularProgressIndicator()
}


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
                    text = "Dodaj napój",
                    style = MaterialTheme.typography.h3
                )

                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DrinkType.entries.forEach { drinkType ->
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
                Text(text = "lub")
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
                    style = MaterialTheme.typography.caption,
                    suffix = {
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
                        text = "Potwierdź",
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier.clickable { onDismissRequest() },
                    text = "Anuluj",
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                )
            }
        }

    }
}

@Composable
fun RowScope.AddWaterItem(
    drinkType: DrinkType,
    onSelected: (DrinkType) -> Unit,
    isSelected: Boolean = false,
) {
    Box(
        modifier = Modifier
            .height(125.dp)
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.backgroundContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onSelected(drinkType) },
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = if (isSelected) drinkType.resIdSelected else drinkType.resIdUnselected),
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colors.background else VeryWhite
            )
            Text(
                text = drinkType.amountOfWater.toString() + " ml"
            )

        }
    }
}

enum class DrinkType(
    @DrawableRes val resIdSelected: Int,
    @DrawableRes val resIdUnselected: Int,
    val amountOfWater: Int,
) {
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
    ),
}