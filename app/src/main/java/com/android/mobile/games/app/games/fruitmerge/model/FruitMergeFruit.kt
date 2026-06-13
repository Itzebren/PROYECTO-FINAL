package com.android.mobile.games.app.games.fruitmerge.model

import androidx.compose.ui.geometry.Offset

data class FruitMergeFruit(
    val id: Long,
    val type: FruitMergeFruitType,
    val position: Offset,
    val velocity: Offset = Offset.Zero
)
