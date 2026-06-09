package com.android.mobile.games.app.games.fruitmerge.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.mobile.games.app.games.fruitmerge.data.FruitMergeScoreRepository
import com.android.mobile.games.app.games.fruitmerge.engine.FruitMergeGameEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FruitMergeScreen(
    onBackToMenuClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val scoreRepository = remember {
        FruitMergeScoreRepository(context = context)
    }

    val bestScore by scoreRepository
        .getBestScore()
        .collectAsState(initial = 0)

    val gameEngine = remember {
        FruitMergeGameEngine()
    }

    var gameState by remember {
        mutableStateOf(gameEngine.createInitialState())
    }

    var canvasWidth by remember {
        mutableFloatStateOf(0f)
    }

    var canvasHeight by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(
        canvasWidth,
        canvasHeight,
        gameState.isGameOver
    ) {
        while (
            canvasWidth > 0f &&
            canvasHeight > 0f &&
            !gameState.isGameOver
        ) {
            gameState = gameEngine.updateFrame(
                state = gameState,
                width = canvasWidth,
                height = canvasHeight
            )

            delay(16L)
        }
    }

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver) {
            scoreRepository.saveBestScoreIfNeeded(
                score = gameState.score
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FruitMergeCanvas(
            gameState = gameState,
            gameEngine = gameEngine,
            onCanvasSizeChanged = { width, height ->
                canvasWidth = width
                canvasHeight = height
            },
            onMoveDropX = { x ->
                gameState = gameEngine.moveDropX(
                    state = gameState,
                    x = x,
                    width = canvasWidth
                )
            },
            onDrop = {
                gameState = gameEngine.dropFruit(
                    state = gameState,
                    width = canvasWidth,
                    height = canvasHeight
                )
            }
        )

        FruitMergeHud(
            score = gameState.score,
            bestScore = maxOf(bestScore, gameState.score),
            nextFruitType = gameState.nextFruitType,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (gameState.isGameOver) {
            FruitMergeGameOverPanel(
                score = gameState.score,
                bestScore = maxOf(bestScore, gameState.score),
                onRestartClick = {
                    coroutineScope.launch {
                        scoreRepository.saveBestScoreIfNeeded(
                            score = gameState.score
                        )

                        gameState = gameEngine.createInitialState()
                    }
                },
                onBackToMenuClick = {
                    coroutineScope.launch {
                        scoreRepository.saveBestScoreIfNeeded(
                            score = gameState.score
                        )

                        onBackToMenuClick()
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
