package com.android.mobile.games.app.games.fruitninja.util

import androidx.compose.ui.geometry.Offset
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaItem
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaItemType
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

fun createRandomFruitNinjaItem(
    id: Long,
    screenWidth: Float,
    screenHeight: Float,
    difficulty: FruitNinjaDifficulty
): FruitNinjaItem {
    val type = createRandomItemType(difficulty)
    val radius = getItemRadius(type)

    val startX = Random.nextFloat() * screenWidth
    val startY = screenHeight + radius

    val velocityX = Random.nextFloat() * 8f - 4f
    val baseVelocityY = -(Random.nextFloat() * 16f + 18f)
    val velocityY = baseVelocityY * difficulty.initialSpeedMultiplier

    return FruitNinjaItem(
        id = id,
        type = type,
        position = Offset(startX, startY),
        velocity = Offset(velocityX, velocityY),
        radius = radius
    )
}

fun isPointInsideItem(
    point: Offset,
    item: FruitNinjaItem
): Boolean {
    val distance = sqrt(
        (point.x - item.position.x).pow(2) +
                (point.y - item.position.y).pow(2)
    )

    return distance <= item.radius
}

private fun createRandomItemType(
    difficulty: FruitNinjaDifficulty
): FruitNinjaItemType {
    val bombRoll = Random.nextInt(100)

    if (bombRoll < difficulty.bombProbability) {
        return FruitNinjaItemType.BOMB
    }

    val fruits = listOf(
        FruitNinjaItemType.APPLE,
        FruitNinjaItemType.BANANA,
        FruitNinjaItemType.WATERMELON,
        FruitNinjaItemType.ORANGE,
        FruitNinjaItemType.PINEAPPLE,
        FruitNinjaItemType.COCONUT
    )

    return fruits.random()
}

private fun getItemRadius(
    type: FruitNinjaItemType
): Float {
    return when (type) {
        FruitNinjaItemType.BOMB -> 34f
        FruitNinjaItemType.BANANA -> 42f
        FruitNinjaItemType.PINEAPPLE -> 44f
        FruitNinjaItemType.WATERMELON -> 46f
        FruitNinjaItemType.COCONUT -> 40f
        FruitNinjaItemType.APPLE,
        FruitNinjaItemType.ORANGE -> 38f
    }
}