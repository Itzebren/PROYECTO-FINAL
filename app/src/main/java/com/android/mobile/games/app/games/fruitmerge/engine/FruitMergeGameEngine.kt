package com.android.mobile.games.app.games.fruitmerge.engine

import androidx.compose.ui.geometry.Offset
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeFruit
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeFruitType
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeGameState
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

private const val GRAVITY = 0.72f
private const val WALL_BOUNCE = 0.46f
private const val FLOOR_BOUNCE = 0.28f
private const val FRICTION = 0.985f
private const val COLLISION_ITERATIONS = 3
private const val GAME_OVER_FRAMES = 90
private const val DROP_COOLDOWN_FRAMES = 26

class FruitMergeGameEngine {

    private var nextFruitId = 0L

    fun createInitialState(): FruitMergeGameState {
        nextFruitId = 0L

        return FruitMergeGameState(
            fruits = emptyList(),
            currentFruitType = createRandomDropFruitType(),
            nextFruitType = createRandomDropFruitType(),
            dropXRatio = 0.5f,
            dropCooldownFrames = 0,
            score = 0,
            isGameOver = false,
            dangerFrames = 0
        )
    }

    fun updateFrame(
        state: FruitMergeGameState,
        width: Float,
        height: Float
    ): FruitMergeGameState {
        if (state.isGameOver || width <= 0f || height <= 0f) {
            return state
        }

        val bounds = FruitMergeBounds(
            left = width * 0.07f,
            right = width * 0.93f,
            bottom = height * 0.95f,
            dangerLine = height * 0.2f
        )

        val movedFruits = state.fruits.map { fruit ->
            moveFruit(
                fruit = fruit,
                bounds = bounds,
                radius = getFruitRadius(
                    type = fruit.type,
                    width = width
                )
            )
        }

        val separatedFruits = resolveCollisions(
            fruits = movedFruits,
            width = width
        )

        val mergeResult = mergeTouchingFruits(
            fruits = separatedFruits,
            width = width
        )

        val dangerFrames = if (
            mergeResult.fruits.any { fruit ->
                val radius = getFruitRadius(
                    type = fruit.type,
                    width = width
                )

                fruit.position.y - radius < bounds.dangerLine &&
                        abs(fruit.velocity.y) < 1.2f
            }
        ) {
            state.dangerFrames + 1
        } else {
            0
        }

        return state.copy(
            fruits = mergeResult.fruits,
            score = state.score + mergeResult.scoreAdded,
            dropCooldownFrames = (state.dropCooldownFrames - 1).coerceAtLeast(0),
            dangerFrames = dangerFrames,
            isGameOver = dangerFrames >= GAME_OVER_FRAMES
        )
    }

    fun moveDropX(
        state: FruitMergeGameState,
        x: Float,
        width: Float
    ): FruitMergeGameState {
        if (state.isGameOver || width <= 0f) {
            return state
        }

        return state.copy(
            dropXRatio = (x / width).coerceIn(0.14f, 0.86f)
        )
    }

    fun dropFruit(
        state: FruitMergeGameState,
        width: Float,
        height: Float
    ): FruitMergeGameState {
        if (
            state.isGameOver ||
            state.dropCooldownFrames > 0 ||
            width <= 0f ||
            height <= 0f
        ) {
            return state
        }

        val radius = getFruitRadius(
            type = state.currentFruitType,
            width = width
        )

        val fruit = FruitMergeFruit(
            id = nextFruitId++,
            type = state.currentFruitType,
            position = Offset(
                x = width * state.dropXRatio,
                y = height * 0.13f - radius
            )
        )

        return state.copy(
            fruits = state.fruits + fruit,
            currentFruitType = state.nextFruitType,
            nextFruitType = createRandomDropFruitType(),
            dropCooldownFrames = DROP_COOLDOWN_FRAMES,
            dangerFrames = 0
        )
    }

    fun getFruitRadius(
        type: FruitMergeFruitType,
        width: Float
    ): Float {
        return (width * type.radiusRatio).coerceIn(22f, 76f)
    }

    private fun moveFruit(
        fruit: FruitMergeFruit,
        bounds: FruitMergeBounds,
        radius: Float
    ): FruitMergeFruit {
        var velocity = fruit.velocity.copy(
            x = fruit.velocity.x * FRICTION,
            y = fruit.velocity.y + GRAVITY
        )
        var position = fruit.position + velocity

        if (position.x - radius < bounds.left) {
            position = position.copy(x = bounds.left + radius)
            velocity = velocity.copy(x = -velocity.x * WALL_BOUNCE)
        }

        if (position.x + radius > bounds.right) {
            position = position.copy(x = bounds.right - radius)
            velocity = velocity.copy(x = -velocity.x * WALL_BOUNCE)
        }

        if (position.y + radius > bounds.bottom) {
            position = position.copy(y = bounds.bottom - radius)
            velocity = velocity.copy(
                x = velocity.x * 0.94f,
                y = -velocity.y * FLOOR_BOUNCE
            )
        }

        return fruit.copy(
            position = position,
            velocity = velocity
        )
    }

    private fun resolveCollisions(
        fruits: List<FruitMergeFruit>,
        width: Float
    ): List<FruitMergeFruit> {
        var updatedFruits = fruits

        repeat(COLLISION_ITERATIONS) {
            val mutableFruits = updatedFruits.toMutableList()

            for (firstIndex in 0 until mutableFruits.lastIndex) {
                for (secondIndex in firstIndex + 1 until mutableFruits.size) {
                    val firstFruit = mutableFruits[firstIndex]
                    val secondFruit = mutableFruits[secondIndex]
                    val firstRadius = getFruitRadius(firstFruit.type, width)
                    val secondRadius = getFruitRadius(secondFruit.type, width)
                    val delta = secondFruit.position - firstFruit.position
                    val distance = max(
                        sqrt(delta.x * delta.x + delta.y * delta.y),
                        0.001f
                    )
                    val overlap = firstRadius + secondRadius - distance

                    if (overlap > 0f) {
                        val normal = Offset(
                            x = delta.x / distance,
                            y = delta.y / distance
                        )
                        val correction = normal * (overlap / 2f)

                        mutableFruits[firstIndex] = firstFruit.copy(
                            position = firstFruit.position - correction,
                            velocity = firstFruit.velocity - correction * 0.028f
                        )
                        mutableFruits[secondIndex] = secondFruit.copy(
                            position = secondFruit.position + correction,
                            velocity = secondFruit.velocity + correction * 0.028f
                        )
                    }
                }
            }

            updatedFruits = mutableFruits
        }

        return updatedFruits
    }

    private fun mergeTouchingFruits(
        fruits: List<FruitMergeFruit>,
        width: Float
    ): FruitMergeResult {
        val consumedIds = mutableSetOf<Long>()
        val newFruits = mutableListOf<FruitMergeFruit>()
        var scoreAdded = 0

        for (firstIndex in 0 until fruits.lastIndex) {
            val firstFruit = fruits[firstIndex]

            if (firstFruit.id in consumedIds) continue

            for (secondIndex in firstIndex + 1 until fruits.size) {
                val secondFruit = fruits[secondIndex]
                val nextType = firstFruit.type.next()

                if (
                    secondFruit.id in consumedIds ||
                    nextType == null ||
                    firstFruit.type != secondFruit.type
                ) {
                    continue
                }

                val firstRadius = getFruitRadius(firstFruit.type, width)
                val secondRadius = getFruitRadius(secondFruit.type, width)
                val delta = secondFruit.position - firstFruit.position
                val distance = sqrt(delta.x * delta.x + delta.y * delta.y)

                if (distance <= (firstRadius + secondRadius) * 0.82f) {
                    val mergedPosition = Offset(
                        x = (firstFruit.position.x + secondFruit.position.x) / 2f,
                        y = (firstFruit.position.y + secondFruit.position.y) / 2f
                    )
                    val mergedVelocity = Offset(
                        x = (firstFruit.velocity.x + secondFruit.velocity.x) * 0.24f,
                        y = min(
                            (firstFruit.velocity.y + secondFruit.velocity.y) * 0.24f,
                            3.5f
                        )
                    )

                    consumedIds += firstFruit.id
                    consumedIds += secondFruit.id
                    scoreAdded += nextType.points
                    newFruits += FruitMergeFruit(
                        id = nextFruitId++,
                        type = nextType,
                        position = mergedPosition,
                        velocity = mergedVelocity
                    )
                    break
                }
            }
        }

        return FruitMergeResult(
            fruits = fruits.filterNot { fruit ->
                fruit.id in consumedIds
            } + newFruits,
            scoreAdded = scoreAdded
        )
    }

    private fun createRandomDropFruitType(): FruitMergeFruitType {
        return FruitMergeFruitType.entries
            .take(3)
            .random(Random)
    }
}

private data class FruitMergeBounds(
    val left: Float,
    val right: Float,
    val bottom: Float,
    val dangerLine: Float
)

private data class FruitMergeResult(
    val fruits: List<FruitMergeFruit>,
    val scoreAdded: Int
)
