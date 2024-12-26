package com.azmarzly.home.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.azmarzly.home.components.AddWaterDialog
import com.azmarzly.home.components.DropletButtonNavBar
import com.azmarzly.home.domain.toBottomBarItemIndex
import com.azmarzly.home.presentation.HomeViewModel.Companion.homeScreens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import core.util.doNothing
import core.util.findActivity
import core.util.navigateTo

@OptIn(ExperimentalPermissionsApi::class)
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
                barColor = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)),
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermission()
        }

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
                    homeViewModel.addHydration(amountOfWaterAdded = amount)
                    showAddWaterCard = false
                },
                validateCustomAmount = homeViewModel::validateNumber,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermission() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    if (permissionState.status.isGranted.not()) {
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}