package com.android.mobile.games.app.games.runner.engine

import com.android.mobile.games.app.games.runner.model.RunnerGameState
import com.android.mobile.games.app.games.runner.model.RunnerObstacle
import com.android.mobile.games.app.games.runner.model.RunnerObstacleType
import kotlin.math.max
import kotlin.random.Random

private const val INITIAL_SPEED_RATIO = 0.62f
private const val MAX_SPEED_RATIO = 1.05f
private const val SPEED_GAIN_RATIO = 0.011f
private const val GRAVITY_RATIO = 2.45f
private const val JUMP_FORCE_RATIO = -1.18f
private const val MIN_SPAWN_SECONDS = 1.04f
private const val MAX_SPAWN_SECONDS = 1.72f

class RunnerGameEngine {

    private var nextObstacleId = 0L
    private var spawnTimerSeconds = 0f
    private var nextSpawnSeconds = 1.1f
    private val random = Random(System.currentTimeMillis())

    fun createInitialState(): RunnerGameState {
        nextObstacleId = 0L
        spawnTimerSeconds = 0f
        nextSpawnSeconds = 1.1f

        return RunnerGameState()
    }

    fun initializeLayout(
        state: RunnerGameState,
        width: Float,
        height: Float
    ): RunnerGameState {
        if (width <= 0f || height <= 0f) {
            return state
        }

        val playerSize = getPlayerSize(width)
        val groundY = getGroundY(height) - playerSize

        return state.copy(
            playerX = width * 0.16f,
            playerY = if (state.groundY == 0f) groundY else state.playerY.coerceAtMost(groundY),
            groundY = groundY,
            speedPxPerSecond = if (state.speedPxPerSecond == 0f) {
                height * INITIAL_SPEED_RATIO
            } else {
                state.speedPxPerSecond
            }
        )
    }

    fun updateFrame(
        state: RunnerGameState,
        width: Float,
        height: Float,
        deltaSeconds: Float
    ): RunnerGameState {
        if (state.isGameOver || width <= 0f || height <= 0f) {
            return state
        }

        var updatedState = initializeLayout(
            state = state,
            width = width,
            height = height
        )

        val maxSpeed = height * MAX_SPEED_RATIO
        val speed = (updatedState.speedPxPerSecond + height * SPEED_GAIN_RATIO * deltaSeconds)
            .coerceAtMost(maxSpeed)
        val distance = updatedState.distance + speed * deltaSeconds
        val scoreFromDistance = (distance / 12f).toInt()

        updatedState = updatedState.copy(
            speedPxPerSecond = speed,
            distance = distance
        )

        updatedState = updatePlayer(
            state = updatedState,
            height = height,
            deltaSeconds = deltaSeconds
        )

        updatedState = updateObstacles(
            state = updatedState,
            speed = speed,
            deltaSeconds = deltaSeconds
        )

        updatedState = spawnObstacleIfNeeded(
            state = updatedState,
            width = width,
            height = height,
            deltaSeconds = deltaSeconds
        )

        updatedState = updateClouds(
            state = updatedState,
            speed = speed,
            width = width,
            deltaSeconds = deltaSeconds
        )

        val scoredResult = scorePassedObstacles(
            state = updatedState,
            playerRight = updatedState.playerX + getPlayerSize(width) * 0.7f
        )

        updatedState = scoredResult.state.copy(
            score = max(scoreFromDistance, scoredResult.state.score + scoredResult.scoreBonus)
        )

        return updatedState.copy(
            isGameOver = hasCollision(
                state = updatedState,
                width = width
            )
        )
    }

    fun jump(state: RunnerGameState, height: Float): RunnerGameState {
        if (state.isGameOver || state.isJumping) {
            return state
        }

        return state.copy(
            playerVelocityY = height * JUMP_FORCE_RATIO,
            isJumping = true,
            isDucking = false
        )
    }

    fun setDucking(
        state: RunnerGameState,
        isDucking: Boolean
    ): RunnerGameState {
        if (state.isGameOver || state.isJumping) {
            return state.copy(isDucking = false)
        }

        return state.copy(isDucking = isDucking)
    }

    fun getPlayerSize(width: Float): Float {
        return (width * 0.155f).coerceIn(54f, 78f)
    }

    fun getGroundY(height: Float): Float {
        return height * 0.74f
    }

    private fun updatePlayer(
        state: RunnerGameState,
        height: Float,
        deltaSeconds: Float
    ): RunnerGameState {
        val nextVelocity = state.playerVelocityY + height * GRAVITY_RATIO * deltaSeconds
        val nextY = state.playerY + nextVelocity * deltaSeconds

        return if (nextY >= state.groundY) {
            state.copy(
                playerY = state.groundY,
                playerVelocityY = 0f,
                isJumping = false
            )
        } else {
            state.copy(
                playerY = nextY,
                playerVelocityY = nextVelocity,
                isJumping = true
            )
        }
    }

    private fun updateObstacles(
        state: RunnerGameState,
        speed: Float,
        deltaSeconds: Float
    ): RunnerGameState {
        return state.copy(
            obstacles = state.obstacles
                .map { obstacle ->
                    obstacle.copy(x = obstacle.x - speed * deltaSeconds)
                }
                .filter { obstacle ->
                    obstacle.x + obstacle.width > 0f
                }
        )
    }

    private fun spawnObstacleIfNeeded(
        state: RunnerGameState,
        width: Float,
        height: Float,
        deltaSeconds: Float
    ): RunnerGameState {
        spawnTimerSeconds += deltaSeconds

        if (spawnTimerSeconds < nextSpawnSeconds) {
            return state
        }

        spawnTimerSeconds = 0f
        nextSpawnSeconds = random.nextFloat() *
                (MAX_SPAWN_SECONDS - MIN_SPAWN_SECONDS) +
                MIN_SPAWN_SECONDS

        val type = RunnerObstacleType.entries.random(random)
        val obstacleWidth = (width * type.widthRatio).coerceAtLeast(26f)
        val obstacleHeight = (height * type.heightRatio).coerceAtLeast(42f)
        val obstacle = RunnerObstacle(
            id = nextObstacleId++,
            type = type,
            x = width + obstacleWidth,
            y = getGroundY(height) - obstacleHeight,
            width = obstacleWidth,
            height = obstacleHeight
        )

        return state.copy(obstacles = state.obstacles + obstacle)
    }

    private fun updateClouds(
        state: RunnerGameState,
        speed: Float,
        width: Float,
        deltaSeconds: Float
    ): RunnerGameState {
        val cloudSpeed = speed * 0.16f / width

        return state.copy(
            cloudOffsets = state.cloudOffsets.map { offset ->
                val updatedOffset = offset - cloudSpeed * deltaSeconds

                if (updatedOffset < -0.18f) {
                    1.08f + random.nextFloat() * 0.2f
                } else {
                    updatedOffset
                }
            }
        )
    }

    private fun scorePassedObstacles(
        state: RunnerGameState,
        playerRight: Float
    ): ScoredRunnerState {
        var scoreBonus = 0
        val obstacles = state.obstacles.map { obstacle ->
            if (!obstacle.isScored && obstacle.x + obstacle.width < playerRight) {
                scoreBonus += obstacle.type.scoreBonus
                obstacle.copy(isScored = true)
            } else {
                obstacle
            }
        }

        return ScoredRunnerState(
            state = state.copy(obstacles = obstacles),
            scoreBonus = scoreBonus
        )
    }

    private fun hasCollision(
        state: RunnerGameState,
        width: Float
    ): Boolean {
        val playerSize = getPlayerSize(width)
        val playerLeft = state.playerX + playerSize * 0.16f
        val playerRight = state.playerX + playerSize * 0.82f
        val playerTop = state.playerY + playerSize * if (state.isDucking) 0.42f else 0.1f
        val playerBottom = state.playerY + playerSize * 0.94f

        return state.obstacles.any { obstacle ->
            val obstacleLeft = obstacle.x + obstacle.width * 0.12f
            val obstacleRight = obstacle.x + obstacle.width * 0.88f
            val obstacleTop = obstacle.y + obstacle.height * 0.06f
            val obstacleBottom = obstacle.y + obstacle.height

            obstacleRight > playerLeft &&
                    obstacleLeft < playerRight &&
                    obstacleBottom > playerTop &&
                    obstacleTop < playerBottom
        }
    }
}

private data class ScoredRunnerState(
    val state: RunnerGameState,
    val scoreBonus: Int
)
