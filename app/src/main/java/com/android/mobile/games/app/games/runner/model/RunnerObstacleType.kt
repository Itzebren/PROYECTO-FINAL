package com.android.mobile.games.app.games.runner.model

enum class RunnerObstacleType(
    val widthRatio: Float,
    val heightRatio: Float,
    val scoreBonus: Int
) {
    SMALL_CACTUS(
        widthRatio = 0.07f,
        heightRatio = 0.105f,
        scoreBonus = 5
    ),

    TALL_CACTUS(
        widthRatio = 0.085f,
        heightRatio = 0.145f,
        scoreBonus = 9
    ),

    DOUBLE_CACTUS(
        widthRatio = 0.125f,
        heightRatio = 0.12f,
        scoreBonus = 14
    )
}
