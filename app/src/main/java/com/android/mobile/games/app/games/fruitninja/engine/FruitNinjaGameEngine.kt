package com.android.mobile.games.app.games.fruitninja.engine

import androidx.compose.ui.geometry.Offset
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaGameState
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaItem
import com.android.mobile.games.app.games.fruitninja.model.isBomb
import com.android.mobile.games.app.games.fruitninja.model.isFruit
import com.android.mobile.games.app.games.fruitninja.util.createRandomFruitNinjaItem
import com.android.mobile.games.app.games.fruitninja.util.isPointInsideItem

private const val INITIAL_LIVES = 3
private const val INITIAL_TIME_SECONDS = 60

class FruitNinjaGameEngine(
    private val difficulty: FruitNinjaDifficulty
) {

    private var nextItemId: Long = 0L
    private var frameCounter: Int = 0

    fun createInitialState(): FruitNinjaGameState {
        resetEngine()

        return FruitNinjaGameState(
            items = emptyList(),
            score = 0,
            lives = INITIAL_LIVES,
            timeRemainingSeconds = INITIAL_TIME_SECONDS,
            difficulty = difficulty,
            isRunning = true,
            isPaused = false,
            isGameOver = false
        )
    }

    fun updateFrame(
        state: FruitNinjaGameState,
        screenWidth: Float,
        screenHeight: Float
    ): FruitNinjaGameState {
        if (!state.isRunning || state.isPaused || state.isGameOver) {
            return state
        }

        frameCounter++

        val movedItems = moveItems(state.items)
        val visibleItems = getVisibleItems(
            items = movedItems,
            screenHeight = screenHeight
        )

        val missedFruits = countMissedFruits(
            items = movedItems,
            screenHeight = screenHeight
        )

        val updatedLives = (state.lives - missedFruits).coerceAtLeast(0)

        val itemsWithSpawn = spawnItemIfNeeded(
            items = visibleItems,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        )

        return state.copy(
            items = itemsWithSpawn,
            lives = updatedLives,
            isGameOver = updatedLives <= 0
        )
    }

    fun updateTimer(
        state: FruitNinjaGameState
    ): FruitNinjaGameState {
        if (!state.isRunning || state.isPaused || state.isGameOver) {
            return state
        }

        val updatedTime = (state.timeRemainingSeconds - 1).coerceAtLeast(0)

        return state.copy(
            timeRemainingSeconds = updatedTime,
            isGameOver = updatedTime <= 0
        )
    }

    fun sliceAt(
        state: FruitNinjaGameState,
        touchPoint: Offset
    ): FruitNinjaGameState {
        if (!state.isRunning || state.isPaused || state.isGameOver) {
            return state
        }

        val touchedItems = state.items.filter { item ->
            isPointInsideItem(
                point = touchPoint,
                item = item
            )
        }

        if (touchedItems.isEmpty()) {
            return state
        }

        if (touchedItems.any { item -> item.type.isBomb() }) {
            return state.copy(
                lives = 0,
                isGameOver = true
            )
        }

        val slicedItemIds = touchedItems.map { item ->
            item.id
        }.toSet()

        val remainingItems = state.items.filterNot { item ->
            item.id in slicedItemIds
        }

        val slicedFruitsCount = touchedItems.count { item ->
            item.type.isFruit()
        }

        return state.copy(
            items = remainingItems,
            score = state.score + slicedFruitsCount * difficulty.pointsPerFruit
        )
    }

    private fun resetEngine() {
        nextItemId = 0L
        frameCounter = 0
    }

    private fun moveItems(
        items: List<FruitNinjaItem>
    ): List<FruitNinjaItem> {
        return items.map { item ->
            val updatedVelocity = item.velocity.copy(
                y = item.velocity.y + difficulty.gravity
            )

            val updatedPosition = item.position + updatedVelocity

            item.copy(
                position = updatedPosition,
                velocity = updatedVelocity
            )
        }
    }

    private fun getVisibleItems(
        items: List<FruitNinjaItem>,
        screenHeight: Float
    ): List<FruitNinjaItem> {
        return items.filter { item ->
            item.position.y < screenHeight + item.radius * 2
        }
    }

    private fun countMissedFruits(
        items: List<FruitNinjaItem>,
        screenHeight: Float
    ): Int {
        return items.count { item ->
            item.type.isFruit() &&
                    item.position.y >= screenHeight + item.radius * 2
        }
    }

    private fun spawnItemIfNeeded(
        items: List<FruitNinjaItem>,
        screenWidth: Float,
        screenHeight: Float
    ): List<FruitNinjaItem> {
        val canSpawn = items.size < difficulty.maxItemsOnScreen
        val shouldSpawn = frameCounter % difficulty.spawnIntervalFrames == 0

        if (!canSpawn || !shouldSpawn) {
            return items
        }

        val newItem = createRandomFruitNinjaItem(
            id = nextItemId++,
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            difficulty = difficulty
        )

        return items + newItem
    }
}