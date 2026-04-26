package com.android.mobile.games.app.games.fruitninja

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class FruitNinjaItemType {
    APPLE,
    BANANA,
    WATERMELON,
    ORANGE,
    BOMB
}

data class FruitNinjaItem(
    val id: Long,
    val type: FruitNinjaItemType,
    val position: Offset,
    val velocity: Offset,
    val radius: Float,
    val isSliced: Boolean = false
)

data class FruitNinjaGameState(
    val items: List<FruitNinjaItem> = emptyList(),
    val score: Int = 0,
    val lives: Int = 3,
    val isGameOver: Boolean = false
)

fun FruitNinjaItemType.color(): Color {
    return when (this) {
        FruitNinjaItemType.APPLE -> Color.Red
        FruitNinjaItemType.BANANA -> Color.Yellow
        FruitNinjaItemType.WATERMELON -> Color.Green
        FruitNinjaItemType.ORANGE -> Color(0xFFFF9800)
        FruitNinjaItemType.BOMB -> Color.DarkGray
    }
}

fun FruitNinjaItemType.label(): String {
    return when (this) {
        FruitNinjaItemType.APPLE -> "🍎"
        FruitNinjaItemType.BANANA -> "🍌"
        FruitNinjaItemType.WATERMELON -> "🍉"
        FruitNinjaItemType.ORANGE -> "🍊"
        FruitNinjaItemType.BOMB -> "💣"
    }
}