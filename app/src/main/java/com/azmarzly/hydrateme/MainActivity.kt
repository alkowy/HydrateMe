package com.azmarzly.hydrateme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.azmarzly.hydrateme.navigation.AppNavHost
import core.ui.theme.HydrateMeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HydrateMeTheme {
                AppNavHost(
//                    startDestination = HomeRoute.HOME_ROOT,// remove,
                )
            }
        }
    }
}