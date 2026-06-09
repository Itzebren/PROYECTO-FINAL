package com.android.mobile.games.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.mobile.games.app.games.catchgame.model.CatchGameDifficulty
import com.android.mobile.games.app.games.catchgame.ui.CatchGameMenuScreen
import com.android.mobile.games.app.games.catchgame.ui.CatchGameScreen
import com.android.mobile.games.app.games.fruitmerge.ui.FruitMergeScreen
import com.android.mobile.games.app.games.runner.ui.RunnerScreen
import com.android.mobile.games.app.ui.screens.MainMenuScreen

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
    }
}
