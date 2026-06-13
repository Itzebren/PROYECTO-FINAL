package com.android.mobile.games.app.games.fruitmerge.model

enum class FruitMergeFruitType(
    val label: String,
    val radiusRatio: Float,
    val points: Int
) {

    APPLE(
        label = "Apple",
        radiusRatio = 0.058f,
        points = 2
    ),

    ORANGE(
        label = "Orange",
        radiusRatio = 0.068f,
        points = 5
    ),

    BANANA(
        label = "Banana",
        radiusRatio = 0.08f,
        points = 11
    ),

    COCONUT(
        label = "Coconut",
        radiusRatio = 0.094f,
        points = 23
    ),

    PINEAPPLE(
        label = "Pineapple",
        radiusRatio = 0.112f,
        points = 47
    ),

    WATERMELON(
        label = "Watermelon",
        radiusRatio = 0.136f,
        points = 95
    );

    fun next(): FruitMergeFruitType? {
        val nextIndex = ordinal + 1

        return entries.getOrNull(nextIndex)
    }
}
