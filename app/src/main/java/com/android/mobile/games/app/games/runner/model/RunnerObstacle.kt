package com.android.mobile.games.app.games.runner.model

data class RunnerObstacle(
    val id: Long,
    val type: RunnerObstacleType,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val isScored: Boolean = false
)
