package com.android.mobile.games.app.games.runner.model

data class RunnerGameState(
    val score: Int = 0,
    val distance: Float = 0f,
    val speedPxPerSecond: Float = 0f,
    val playerX: Float = 0f,
    val playerY: Float = 0f,
    val playerVelocityY: Float = 0f,
    val groundY: Float = 0f,
    val isJumping: Boolean = false,
    val hasDoubleJumped: Boolean = false,
    val isDucking: Boolean = false,
    val obstacles: List<RunnerObstacle> = emptyList(),
    val cloudOffsets: List<Float> = listOf(0.12f, 0.48f, 0.78f),
    val isGameOver: Boolean = false
)
