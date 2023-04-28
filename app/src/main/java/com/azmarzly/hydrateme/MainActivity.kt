package com.azmarzly.hydrateme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.azmarzly.authentication.domain.AuthenticationRepository
import com.azmarzly.authentication.presentation.AuthViewModel
import core.ui.theme.HydrateMeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthenticationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val authState = authViewModel.authState.collectAsStateWithLifecycle()
            HydrateMeTheme {
                Column() {
                    Text(text = "test")
                    Button(onClick = {
                        authViewModel.loginWithEmailAndPassword(
                            "test2@gmail.com",
                            "12345qwerty"
                        )
                    }) {
                        Text(text = "btn login")
                    }
                    Text(text = "Auth state: ${authState.value}")
                    AppNavHost()

                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "landing_page"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("landing_page") {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {

            }
            //LandingPage(onNavigateToHome?? = navController.navigate("home_screen")
        }
        accountCreationGraph(navController)
    }

}

fun NavGraphBuilder.accountCreationGraph(navController: NavController) {
    navigation(startDestination = "email_password_name", route = "account_creation") {
        composable("email_password_name") {
            //EmailPasswordNameSignUp()
        }
        composable("user_data") {
            //UserDataSingUp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HydrateMeTheme {
    }
}