package com.azmarzly.home.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
        RequestNotificationPermission()

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

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted.not()) {
            showSettingsDialog = shouldShowRequestPermissionRationale(context).not()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            // First, request the notification permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            Log.d("ANANAS", "Permission already granted")
            // Permission already granted - proceed with notifications
        }
    }

    // Dialog to inform the user about enabling permission in settings
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text(text = "Notification Permission Needed") },
            text = { Text(text = "To receive notifications, please enable the permission in settings.") },
            confirmButton = {
                Button(onClick = {
                    openAppSettings(context)
                    showSettingsDialog = false
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                Button(onClick = { showSettingsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun shouldShowRequestPermissionRationale(context: Context): Boolean {
    return androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
        context as androidx.activity.ComponentActivity,
        Manifest.permission.POST_NOTIFICATIONS
    )
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}
