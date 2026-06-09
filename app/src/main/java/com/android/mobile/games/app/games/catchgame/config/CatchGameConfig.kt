package com.android.mobile.games.app.games.catchgame.config

import com.android.mobile.games.app.R

object CatchGameConfig {

    const val INITIAL_LIVES = 3
    const val PLAYER_SMOOTHING = 10f
    const val TRIVIA_TIME_SECONDS = 10

    val foodDrawables = listOf(
        R.drawable.food_apple,
        R.drawable.food_banana,
        R.drawable.food_burger,
        R.drawable.food_cake
    )

    val badDrawables = listOf(
        R.drawable.bad_bomb,
        R.drawable.bad_poison,
        R.drawable.bad_rock
    )
}
