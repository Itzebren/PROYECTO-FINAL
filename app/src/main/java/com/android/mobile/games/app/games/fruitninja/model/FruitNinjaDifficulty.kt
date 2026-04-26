package com.android.mobile.games.app.games.fruitninja.model

enum class FruitNinjaDifficulty(
    val label: String,
    val spawnIntervalFrames: Int,
    val initialSpeedMultiplier: Float,
    val gravity: Float,
    val bombProbability: Int,
    val maxItemsOnScreen: Int,
    val pointsPerFruit: Int
) {

    EASY(
        label = "Easy",
        spawnIntervalFrames = 42,
        initialSpeedMultiplier = 0.9f,
        gravity = 0.45f,
        bombProbability = 8,
        maxItemsOnScreen = 5,
        pointsPerFruit = 1
    ),

    MEDIUM(
        label = "Medium",
        spawnIntervalFrames = 30,
        initialSpeedMultiplier = 1.2f,
        gravity = 0.55f,
        bombProbability = 15,
        maxItemsOnScreen = 7,
        pointsPerFruit = 2
    ),

    HARD(
        label = "Hard",
        spawnIntervalFrames = 18,
        initialSpeedMultiplier = 1.6f,
        gravity = 0.70f,
        bombProbability = 25,
        maxItemsOnScreen = 10,
        pointsPerFruit = 3
    )
}