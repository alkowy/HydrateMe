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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.azmarzly.authentication.domain.AuthenticationRepository
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
            HydrateMeTheme {
                Column() {
                    Text(text = "test")
                    Button(onClick = {
                        authRepository.loginWithEmailAndPassword(
                            "test@gmail.com",
                            "12345qwerty"
                        )
                    }) {
                        Text(text = "btn login")
                    }
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
                Greeting("Android")

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

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HydrateMeTheme {
        Greeting("Android")
    }
}