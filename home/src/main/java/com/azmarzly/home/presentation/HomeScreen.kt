package com.azmarzly.home.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.azmarzly.home.components.AddWaterDialog
import com.azmarzly.home.components.DropletButtonNavBar
import com.azmarzly.home.domain.toBottomBarItemIndex
import com.azmarzly.home.presentation.HomeViewModel.Companion.homeScreens
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
    val bottomBarVisibility by homeViewModel.bottomBarVisibilityState.collectAsStateWithLifecycle()
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        bottomBar = {
            DropletButtonNavBar(
                dropletButtons = homeScreens,
                shouldShowBottomBar = bottomBarVisibility,
                barColor = Brush.verticalGradient(listOf(MaterialTheme.colors.background,MaterialTheme.colors.background)),
                selectedScreenIndex = currentRoute?.destination?.route.toBottomBarItemIndex(),
                onNavigateToScreen = { route ->
                    navController.popBackStack()
                    navController.navigateTo(route) {
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
    ) { paddingValues ->
        HomeNavGraph(
            navController = navController,
            navigateToSignIn = navigateToSignIn,
            homeViewModel = homeViewModel,
            bottomBarPadding = paddingValues.calculateBottomPadding(),
            setBottomBarVisibility = homeViewModel::setBottomBarVisibilityState,
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