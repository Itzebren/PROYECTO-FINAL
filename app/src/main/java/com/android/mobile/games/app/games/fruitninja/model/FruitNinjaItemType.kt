package com.android.mobile.games.app.games.fruitninja.model

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