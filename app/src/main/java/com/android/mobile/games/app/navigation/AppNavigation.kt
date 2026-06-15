package com.android.mobile.games.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.mobile.games.app.games.catchgame.model.CatchGameDifficulty
import com.android.mobile.games.app.games.catchgame.ui.CatchGameMenuScreen
import com.android.mobile.games.app.games.catchgame.ui.CatchGameScreen
import com.android.mobile.games.app.games.fruitmerge.ui.FruitMergeScreen
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty
import com.android.mobile.games.app.games.fruitninja.ui.FruitNinjaMenuScreen
import com.android.mobile.games.app.games.fruitninja.ui.FruitNinjaScreen
import com.android.mobile.games.app.games.razarun.ui.RazaMenuScreen
import com.android.mobile.games.app.games.razarun.ui.RazaScreen
import com.android.mobile.games.app.games.runner.ui.RunnerScreen
import com.android.mobile.games.app.ui.screens.MainMenuScreen

private const val DIFFICULTY_ARGUMENT = "difficulty"
private const val USERNAME_ARGUMENT = "username"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var catchGameDifficulty by remember {
        mutableStateOf(CatchGameDifficulty.EASY)
    }

    NavHost(
        navController = navController,
        startDestination = AppRoute.MainMenu.route
    ) {
        composable(AppRoute.MainMenu.route) {
            MainMenuScreen(
                onCodeSlasherClick = {
                    navController.navigate(AppRoute.FruitNinjaMenu.route)
                },
                onLaRazaRunClick = {
                    navController.navigate(AppRoute.LaRazaRunMenu.route)
                }, // <- Aquí faltaba cerrar
                onCatchGameClick = {
                    navController.navigate(AppRoute.CatchGameMenu.route)
                },
                onRunnerGameClick = {
                    navController.navigate(AppRoute.RunnerGame.route)
                },
                onFruitMergeClick = {
                    navController.navigate(AppRoute.FruitMergeGame.route)
                }
            )
        }

        // Bloque del menú de La Raza Run reparado
        composable(AppRoute.LaRazaRunMenu.route) {
            RazaMenuScreen(
                onStartGameClick = {
                    navController.navigate(AppRoute.LaRazaRunGame.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.CatchGameMenu.route) {
            CatchGameMenuScreen(
                selectedDifficulty = catchGameDifficulty,
                onDifficultySelected = { difficulty ->
                    catchGameDifficulty = difficulty
                },
                onStartGameClick = {
                    navController.navigate(AppRoute.CatchGame.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Bloque del juego La Raza Run reparado
        composable(AppRoute.LaRazaRunGame.route) {
            RazaScreen(
                onBackToMenuClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.CatchGame.route) {
            CatchGameScreen(
                difficulty = catchGameDifficulty,
                onBackToMenuClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.RunnerGame.route) {
            RunnerScreen(
                onBackToMenuClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.FruitMergeGame.route) {
            FruitMergeScreen(
                onBackToMenuClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.FruitNinjaMenu.route) {
            FruitNinjaMenuScreen(
                onStartGameClick = { difficulty, username ->
                    navController.navigate(
                        AppRoute.FruitNinjaGame.createRoute(
                            difficulty = difficulty.name,
                            username = username
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
                },
                navArgument(USERNAME_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val difficultyName = backStackEntry.arguments
                ?.getString(DIFFICULTY_ARGUMENT)
                ?: FruitNinjaDifficulty.CLASSIC.name

            val username = backStackEntry.arguments
                ?.getString(USERNAME_ARGUMENT)
                ?: "Anonymous"

            val difficulty = runCatching {
                FruitNinjaDifficulty.valueOf(difficultyName)
            }.getOrDefault(FruitNinjaDifficulty.CLASSIC)

            FruitNinjaScreen(
                difficulty = difficulty,
                username = username,
                onBackToMenuClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}