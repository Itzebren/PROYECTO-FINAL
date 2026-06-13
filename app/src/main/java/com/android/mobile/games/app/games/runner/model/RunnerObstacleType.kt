package com.android.mobile.games.app.games.runner.model

enum class RunnerObstacleType(
    val widthRatio: Float,
    val heightRatio: Float,
    val scoreBonus: Int
) {
    SMALL_CACTUS(
        widthRatio = 0.14f,
        heightRatio = 0.21f,
        scoreBonus = 5
    ),

    TALL_CACTUS(
        widthRatio = 0.17f,
        heightRatio = 0.29f,
        scoreBonus = 9
    ),

    DOUBLE_CACTUS(
        widthRatio = 0.25f,
        heightRatio = 0.24f,
        scoreBonus = 14
    )
}
