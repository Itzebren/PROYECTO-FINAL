package com.android.mobile.games.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.mobile.games.app.games.fruitninja.FruitNinjaMenuScreen
import com.android.mobile.games.app.games.fruitninja.FruitNinjaScreen
import com.android.mobile.games.app.ui.screens.MainMenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.MainMenu.route
    ) {
        composable(AppRoute.MainMenu.route) {
            MainMenuScreen(
                onFruitNinjaClick = {
                    navController.navigate(AppRoute.FruitNinjaMenu.route)
                }
            )
        }

        composable(AppRoute.FruitNinjaMenu.route) {
            FruitNinjaMenuScreen(
                onStartGameClick = {
                    navController.navigate(AppRoute.FruitNinjaGame.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.FruitNinjaGame.route) {
            FruitNinjaScreen(
                onBackToMenuClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}