package com.booksy.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.booksy.data.local.AppDatabase
import com.booksy.data.local.UserEntity
import com.booksy.ui.screens.LoginScreen
import com.booksy.ui.screens.RegisterScreen
import com.booksy.ui.screens.HomeScreen
import com.booksy.ui.screens.ProfileScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    startDestination: String = Screen.Login.route,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun BooksyApp(appDatabase: AppDatabase) {
    val scope = rememberCoroutineScope()
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            val user = appDatabase.userDao().getUserOnce()

            // TEMPORAL: Si no hay usuario, crear uno fake
            if (user == null) {
                appDatabase.userDao().insertUser(
                    UserEntity(
                        id = 1,
                        email = "vichin@gmail.com",
                        name = "Vichin",
                        token = "fake-token-vichin-123",
                        profileImagePath = null
                    )
                )
            }

            startDestination = Screen.Home.route  // Ir directo a Home
        }
    }

    startDestination?.let { destination ->
        NavGraph(startDestination = destination)
    }
}