package com.android.mobile.games.app.games.fruitninja.model

import androidx.compose.ui.graphics.Color

enum class FruitNinjaItemType {
    APPLE,
    BANANA,
    WATERMELON,
    ORANGE,
    PINEAPPLE,
    COCONUT,
    BOMB
}

fun FruitNinjaItemType.isBomb(): Boolean {
    return this == FruitNinjaItemType.BOMB
}

fun FruitNinjaItemType.isFruit(): Boolean {
    return this != FruitNinjaItemType.BOMB
}

fun FruitNinjaItemType.color(): Color {
    return when (this) {
        FruitNinjaItemType.APPLE -> Color.Red
        FruitNinjaItemType.BANANA -> Color.Yellow
        FruitNinjaItemType.WATERMELON -> Color.Green
        FruitNinjaItemType.ORANGE -> Color(0xFFFF9800)
        FruitNinjaItemType.PINEAPPLE -> Color(0xFFFFD54F)
        FruitNinjaItemType.COCONUT -> Color(0xFF8D6E63)
        FruitNinjaItemType.BOMB -> Color.DarkGray
    }
}