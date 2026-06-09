package com.android.mobile.games.app.games.fruitmerge.model

data class FruitMergeGameState(
    val fruits: List<FruitMergeFruit>,
    val currentFruitType: FruitMergeFruitType,
    val nextFruitType: FruitMergeFruitType,
    val dropXRatio: Float,
    val dropCooldownFrames: Int,
    val score: Int,
    val isGameOver: Boolean,
    val dangerFrames: Int
)
