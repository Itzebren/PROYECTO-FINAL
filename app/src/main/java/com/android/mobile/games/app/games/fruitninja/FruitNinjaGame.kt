package com.android.mobile.games.app.games.fruitninja

import androidx.compose.ui.geometry.Offset

private const val GRAVITY = 0.55f
private const val MAX_LIVES = 3

class FruitNinjaGame {

    private var nextItemId: Long = 0L
    private var frameCounter: Int = 0

    fun createInitialState(): FruitNinjaGameState {
        nextItemId = 0L
        frameCounter = 0

        return FruitNinjaGameState(
            items = emptyList(),
            score = 0,
            lives = MAX_LIVES,
            isGameOver = false
        )
    }

    fun updateGame(
        state: FruitNinjaGameState,
        screenWidth: Float,
        screenHeight: Float
    ): FruitNinjaGameState {
        if (state.isGameOver) return state

        frameCounter++

        val movedItems = moveItems(state.items)

        val visibleItems = movedItems.filter { item ->
            item.position.y < screenHeight + item.radius * 2
        }

        val missedFruits = movedItems.count { item ->
            item.type != FruitNinjaItemType.BOMB &&
                    item.position.y >= screenHeight + item.radius * 2
        }

        val updatedLives = (state.lives - missedFruits).coerceAtLeast(0)

        val itemsWithNewSpawn = spawnItemIfNeeded(
            items = visibleItems,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        )

        return state.copy(
            items = itemsWithNewSpawn,
            lives = updatedLives,
            isGameOver = updatedLives <= 0
        )
    }

    fun sliceAt(
        state: FruitNinjaGameState,
        touchPoint: Offset
    ): FruitNinjaGameState {
        if (state.isGameOver) return state

        val touchedItems = state.items.filter { item ->
            isPointInsideItem(touchPoint, item)
        }

        if (touchedItems.isEmpty()) {
            return state
        }

        val touchedBomb = touchedItems.any { item ->
            item.type == FruitNinjaItemType.BOMB
        }

        if (touchedBomb) {
            return state.copy(
                lives = 0,
                isGameOver = true
            )
        }

        val slicedItemIds = touchedItems.map { it.id }.toSet()

        val remainingItems = state.items.filterNot { item ->
            item.id in slicedItemIds
        }

        return state.copy(
            items = remainingItems,
            score = state.score + touchedItems.size
        )
    }

    private fun moveItems(items: List<FruitNinjaItem>): List<FruitNinjaItem> {
        return items.map { item ->
            val newVelocity = item.velocity.copy(
                y = item.velocity.y + GRAVITY
            )

            val newPosition = item.position + newVelocity

            item.copy(
                position = newPosition,
                velocity = newVelocity
            )
        }
    }

    private fun spawnItemIfNeeded(
        items: List<FruitNinjaItem>,
        screenWidth: Float,
        screenHeight: Float
    ): List<FruitNinjaItem> {
        val shouldSpawn = frameCounter % 35 == 0

        if (!shouldSpawn) {
            return items
        }

        val newItem = randomFruitNinjaItem(
            id = nextItemId++,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        )

        return items + newItem
    }
}