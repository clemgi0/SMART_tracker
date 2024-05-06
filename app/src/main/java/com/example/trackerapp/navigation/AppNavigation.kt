package com.example.trackerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trackerapp.LoginScreen
import com.example.trackerapp.MainScreen
import com.example.trackerapp.SplashScreen
import com.example.trackerapp.location.LocationScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route
    ) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(AppScreens.MainScreen.route) {
            MainScreen(navController)
        }
        composable(AppScreens.LocationScreen.route){
            LocationScreen(navController)
        }
    }
}