package com.android.mobile.games.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty
import com.android.mobile.games.app.games.fruitninja.ui.FruitNinjaMenuScreen
import com.android.mobile.games.app.games.fruitninja.ui.FruitNinjaScreen
import com.android.mobile.games.app.ui.screens.MainMenuScreen

private const val DIFFICULTY_ARGUMENT = "difficulty"

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
                onStartGameClick = { difficulty ->
                    navController.navigate(
                        AppRoute.FruitNinjaGame.createRoute(
                            difficulty = difficulty.name
                        )
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoute.FruitNinjaGame.route,
            arguments = listOf(
                navArgument(DIFFICULTY_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val difficultyName = backStackEntry.arguments
                ?.getString(DIFFICULTY_ARGUMENT)
                ?: FruitNinjaDifficulty.EASY.name

            val difficulty = runCatching {
                FruitNinjaDifficulty.valueOf(difficultyName)
            }.getOrDefault(FruitNinjaDifficulty.EASY)

            FruitNinjaScreen(
                difficulty = difficulty,
                onBackToMenuClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}