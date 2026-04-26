package com.android.mobile.games.app.navigation

sealed class AppRoute(val route: String) {

    data object MainMenu : AppRoute("main_menu")

    data object FruitNinjaMenu : AppRoute("fruit_ninja_menu")

    data object FruitNinjaGame : AppRoute("fruit_ninja_game/{difficulty}") {
        fun createRoute(difficulty: String): String {
            return "fruit_ninja_game/$difficulty"
        }
    }
}