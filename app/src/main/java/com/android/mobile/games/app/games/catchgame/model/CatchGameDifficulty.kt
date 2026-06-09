package com.android.mobile.games.app.games.catchgame.model

enum class CatchGameDifficulty(
    val label: String,
    val description: String,
    val spawnIntervalMs: Long,
    val badObjectProbability: Float,
    val minFallSpeedHeightRatio: Float,
    val maxFallSpeedHeightRatio: Float
) {

    EASY(
        label = "Easy",
        description = "More food, fewer hazards.",
        spawnIntervalMs = 900L,
        badObjectProbability = 0.24f,
        minFallSpeedHeightRatio = 0.32f,
        maxFallSpeedHeightRatio = 0.46f
    ),

    MEDIUM(
        label = "Medium",
        description = "Balanced speed and danger.",
        spawnIntervalMs = 700L,
        badObjectProbability = 0.42f,
        minFallSpeedHeightRatio = 0.39f,
        maxFallSpeedHeightRatio = 0.58f
    ),

    HARD(
        label = "Hard",
        description = "Fast drops and more hazards.",
        spawnIntervalMs = 520L,
        badObjectProbability = 0.60f,
        minFallSpeedHeightRatio = 0.48f,
        maxFallSpeedHeightRatio = 0.74f
    )
}
