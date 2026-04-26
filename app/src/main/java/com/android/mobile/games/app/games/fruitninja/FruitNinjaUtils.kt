package com.android.mobile.games.app.games.fruitninja

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

fun randomFruitNinjaItem(
    id: Long,
    screenWidth: Float,
    screenHeight: Float
): FruitNinjaItem {
    val type = randomItemType()
    val radius = if (type == FruitNinjaItemType.BOMB) 34f else 38f

    val startX = Random.nextFloat() * screenWidth
    val startY = screenHeight + radius

    val velocityX = Random.nextFloat() * 8f - 4f
    val velocityY = -(Random.nextFloat() * 16f + 18f)

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

private fun randomItemType(): FruitNinjaItemType {
    val value = Random.nextInt(100)

    return when {
        value < 22 -> FruitNinjaItemType.APPLE
        value < 44 -> FruitNinjaItemType.BANANA
        value < 66 -> FruitNinjaItemType.WATERMELON
        value < 88 -> FruitNinjaItemType.ORANGE
        else -> FruitNinjaItemType.BOMB
    }
}