package com.booksy.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.booksy.data.local.AppDatabase
import com.booksy.ui.screens.LoginScreen
import com.booksy.ui.screens.HomeScreen
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
                    // por ahora no hace nada
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProfile = {
                    // por ahora no hace nada
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
            startDestination = if (user != null) {
                Screen.Home.route
            } else {
                Screen.Login.route
            }
        }
    }

    startDestination?.let { destination ->
        NavGraph(startDestination = destination)
    }
}