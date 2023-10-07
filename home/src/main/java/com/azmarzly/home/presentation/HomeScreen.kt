package com.azmarzly.home.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.azmarzly.home.components.DropletButtonNavBar
import com.azmarzly.home.presentation.HomeScreenParentViewModel.Companion.homeScreens
import core.util.HomeRoute
import core.util.navigateTo

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ParentHomeScreen(
    navController: NavHostController = rememberNavController(),
    navigateToSignIn: () -> Unit,
) {

    val homeParentViewModel: HomeScreenParentViewModel = hiltViewModel()
    var showAddWaterCard by remember { mutableStateOf(false) }

    val state by homeParentViewModel.state.collectAsStateWithLifecycle()

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
                navigateToSignIn = navigateToSignIn
            )

            if (showAddWaterCard) {
                AddWaterDialog(onDismissRequest = { showAddWaterCard = false })
            }


            if (state is HomeScreenRootState.Loading) {
                LoadingContent()
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
fun AddWaterDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(width = 350.dp, height = 450.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background.copy(alpha = 0.9f),
            )
        ) {
            Text(text = "ADD SOM WATER")
        }

    }
}