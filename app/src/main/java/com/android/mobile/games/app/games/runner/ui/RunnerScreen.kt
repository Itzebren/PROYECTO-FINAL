package com.android.mobile.games.app.games.runner.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.android.mobile.games.app.games.runner.data.RunnerScoreRepository
import com.android.mobile.games.app.games.runner.engine.RunnerGameEngine
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun RunnerScreen(
    onBackToMenuClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scoreRepository = remember {
        RunnerScoreRepository(context = context)
    }
    val bestScore by scoreRepository
        .getBestScore()
        .collectAsState(initial = 0)
    val gameEngine = remember {
        RunnerGameEngine()
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
        gameEngine,
        canvasWidth,
        canvasHeight
    ) {
        var lastFrameTimeNanos = 0L

        while (true) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos != 0L) {
                    val deltaSeconds = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f

                    gameState = gameEngine.updateFrame(
                        state = gameState,
                        width = canvasWidth,
                        height = canvasHeight,
                        deltaSeconds = deltaSeconds.coerceAtMost(0.04f)
                    )
                }

                lastFrameTimeNanos = frameTimeNanos
            }
        }
    }

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver) {
            scoreRepository.saveBestScoreIfNeeded(score = gameState.score)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        RunnerCanvas(
            gameState = gameState,
            gameEngine = gameEngine,
            onCanvasSizeChanged = { width, height ->
                canvasWidth = width
                canvasHeight = height

                gameState = gameEngine.initializeLayout(
                    state = gameState,
                    width = width,
                    height = height
                )
            },
            onJump = {
                if (gameState.isGameOver) {
                    coroutineScope.launch {
                        scoreRepository.saveBestScoreIfNeeded(score = gameState.score)
                        gameState = gameEngine.createInitialState()
                    }
                } else {
                    gameState = gameEngine.jump(
                        state = gameState,
                        height = canvasHeight
                    )
                }
            }
        )

        RunnerHud(
            score = gameState.score,
            bestScore = maxOf(bestScore, gameState.score),
            speed = (gameState.speedPxPerSecond / 10f).toInt(),
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Text(
            text = "Tap to jump",
            color = Color(0xFF232323),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (gameState.isGameOver) {
            RunnerGameOverPanel(
                score = gameState.score,
                bestScore = maxOf(bestScore, gameState.score),
                onRestartClick = {
                    coroutineScope.launch {
                        scoreRepository.saveBestScoreIfNeeded(score = gameState.score)
                        gameState = gameEngine.createInitialState()
                    }
                },
                onBackToMenuClick = {
                    coroutineScope.launch {
                        scoreRepository.saveBestScoreIfNeeded(score = gameState.score)
                        onBackToMenuClick()
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
