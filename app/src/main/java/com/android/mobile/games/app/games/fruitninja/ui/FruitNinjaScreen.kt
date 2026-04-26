package com.android.mobile.games.app.games.fruitninja.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.mobile.games.app.games.fruitninja.engine.FruitNinjaGameEngine
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty
import kotlinx.coroutines.delay

@Composable
fun FruitNinjaScreen(
    difficulty: FruitNinjaDifficulty,
    onBackToMenuClick: () -> Unit
) {
    val gameEngine = remember(difficulty) {
        FruitNinjaGameEngine(difficulty = difficulty)
    }

    var gameState by remember(difficulty) {
        mutableStateOf(gameEngine.createInitialState())
    }

    var screenWidth by remember {
        mutableFloatStateOf(0f)
    }

    var screenHeight by remember {
        mutableFloatStateOf(0f)
    }

    var bestScore by remember(difficulty) {
        mutableIntStateOf(0)
    }

    LaunchedEffect(
        screenWidth,
        screenHeight,
        gameState.isGameOver,
        difficulty
    ) {
        while (
            screenWidth > 0f &&
            screenHeight > 0f &&
            !gameState.isGameOver
        ) {
            gameState = gameEngine.updateFrame(
                state = gameState,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )

            delay(16L)
        }
    }

    LaunchedEffect(
        key1 = gameState.isGameOver,
        key2 = difficulty
    ) {
        while (!gameState.isGameOver) {
            delay(1_000L)

            gameState = gameEngine.updateTimer(
                state = gameState
            )
        }
    }

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver && gameState.score > bestScore) {
            bestScore = gameState.score
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FruitNinjaCanvas(
            gameState = gameState,
            onCanvasSizeChanged = { width, height ->
                screenWidth = width
                screenHeight = height
            },
            onSlice = { touchPoint ->
                gameState = gameEngine.sliceAt(
                    state = gameState,
                    touchPoint = touchPoint
                )
            }
        )

        FruitNinjaHud(
            score = gameState.score,
            bestScore = bestScore,
            lives = gameState.lives,
            timeRemainingSeconds = gameState.timeRemainingSeconds,
            difficulty = difficulty,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (gameState.isGameOver) {
            FruitNinjaGameOverPanel(
                score = gameState.score,
                bestScore = bestScore,
                difficulty = difficulty,
                onRestartClick = {
                    gameState = gameEngine.createInitialState()
                },
                onBackToMenuClick = onBackToMenuClick,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}