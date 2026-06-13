package com.android.mobile.games.app.games.razarun.engine

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.android.mobile.games.app.games.razarun.model.*
import kotlinx.coroutines.*
import kotlin.random.Random

class RazaGameEngine(
    private val scope: CoroutineScope
) {
    private val _gameState = mutableStateOf(RazaGameState())
    val gameState: State<RazaGameState> = _gameState

    private var lastFrameTime = 0L
    private var gameJob: Job? = null
    
    private val GOAL_DISTANCE = 600f
    private val INITIAL_SPEED = 5.0f
    private val SPEED_INCREMENT_INTERVAL = 100f
    private val SPEED_INCREMENT_PERCENT = 0.03f

    fun startGame() {
        _gameState.value = RazaGameState(currentSpeed = INITIAL_SPEED)
        lastFrameTime = System.currentTimeMillis()
        gameJob?.cancel()
        gameJob = scope.launch {
            while (isActive && !_gameState.value.isGameOver && !_gameState.value.isVictory) {
                val currentTime = System.currentTimeMillis()
                val deltaTime = (currentTime - lastFrameTime) / 1000f
                lastFrameTime = currentTime
                update(deltaTime)
                delay(16) // ~60 FPS
            }
        }
    }

    private fun update(deltaTime: Float) {
        val currentState = _gameState.value
        
        // Update distance
        val newDistance = currentState.distance + currentState.currentSpeed * deltaTime
        
        // Update speed every 100m
        val speedMultiplier = 1.0f + (newDistance / SPEED_INCREMENT_INTERVAL).toInt() * SPEED_INCREMENT_PERCENT
        val newSpeed = INITIAL_SPEED * speedMultiplier

        // Update timer
        val newTimeRemaining = currentState.timeRemaining - (deltaTime * 1000).toLong()
        
        if (newTimeRemaining <= 0) {
            _gameState.value = currentState.copy(
                distance = newDistance,
                timeRemaining = 0,
                isGameOver = true,
                playerAction = RazaPlayerAction.CRASH
            )
            return
        }

        if (newDistance >= GOAL_DISTANCE) {
            _gameState.value = currentState.copy(
                distance = GOAL_DISTANCE,
                isVictory = true,
                playerAction = RazaPlayerAction.WIN
            )
            return
        }

        // Update animation frame
        val frameRate = 0.1f // 100ms per frame
        val newAnimationFrame = (newDistance / (currentState.currentSpeed * frameRate)).toInt()

        // Update obstacles
        val updatedObstacles = currentState.obstacles.map { 
            it.copy(x = it.x - currentState.currentSpeed * deltaTime * 100) // 100 pixels per meter roughly
        }.filter { it.x + it.width > 0 }

        // Spawn obstacles
        val finalObstacles = if (updatedObstacles.size < 3 && Random.nextFloat() < 0.02f) {
            val type = RazaObstacleType.entries.random()
            updatedObstacles + RazaObstacle(
                x = 1000f, // Start off-screen
                y = if (type == RazaObstacleType.CARRETO) 150f else 350f,
                type = type
            )
        } else {
            updatedObstacles
        }

        // Check collisions
        val hasCollision = checkCollisions(currentState.playerAction, finalObstacles)

        if (hasCollision) {
            _gameState.value = currentState.copy(
                distance = newDistance,
                isGameOver = true,
                playerAction = RazaPlayerAction.CRASH
            )
        } else {
            _gameState.value = currentState.copy(
                distance = newDistance,
                currentSpeed = newSpeed,
                timeRemaining = newTimeRemaining,
                animationFrame = newAnimationFrame,
                obstacles = finalObstacles
            )
        }
    }

    private fun checkCollisions(playerAction: RazaPlayerAction, obstacles: List<RazaObstacle>): Boolean {
        val playerX = 100f
        val playerY = 350f
        val playerWidth = 80f
        val playerHeight = if (playerAction == RazaPlayerAction.SLIDE) 40f else 80f
        val effectivePlayerY = if (playerAction == RazaPlayerAction.SLIDE) 390f else if (playerAction == RazaPlayerAction.JUMP) 250f else 350f

        return obstacles.any { obs ->
            val obsX = obs.x
            val obsY = obs.y
            val obsWidth = obs.width
            val obsHeight = obs.height

            val collisionX = playerX < obsX + obsWidth && playerX + playerWidth > obsX
            val collisionY = effectivePlayerY < obsY + obsHeight && effectivePlayerY + playerHeight > obsY

            collisionX && collisionY
        }
    }

    fun jump() {
        if (_gameState.value.playerAction == RazaPlayerAction.RUN) {
            scope.launch {
                _gameState.value = _gameState.value.copy(playerAction = RazaPlayerAction.JUMP)
                delay(800) // Jump duration
                if (_gameState.value.playerAction == RazaPlayerAction.JUMP) {
                    _gameState.value = _gameState.value.copy(playerAction = RazaPlayerAction.RUN)
                }
            }
        }
    }

    fun slide() {
        if (_gameState.value.playerAction == RazaPlayerAction.RUN) {
            scope.launch {
                _gameState.value = _gameState.value.copy(playerAction = RazaPlayerAction.SLIDE)
                delay(800) // Slide duration
                if (_gameState.value.playerAction == RazaPlayerAction.SLIDE) {
                    _gameState.value = _gameState.value.copy(playerAction = RazaPlayerAction.RUN)
                }
            }
        }
    }
}
