package com.android.mobile.games.app.games.fruitninja

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun FruitNinjaScreen(
    onBackToMenuClick: () -> Unit
) {
    val game = remember { FruitNinjaGame() }

    var gameState by remember {
        mutableStateOf(game.createInitialState())
    }

    var screenWidth by remember {
        mutableStateOf(0f)
    }

    var screenHeight by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(screenWidth, screenHeight, gameState.isGameOver) {
        while (screenWidth > 0f && screenHeight > 0f && !gameState.isGameOver) {
            gameState = game.updateGame(
                state = gameState,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )

            delay(16L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
    ) {
        FruitNinjaGameCanvas(
            gameState = gameState,
            onCanvasSizeChanged = { width, height ->
                screenWidth = width
                screenHeight = height
            },
            onSlice = { touchPoint ->
                gameState = game.sliceAt(
                    state = gameState,
                    touchPoint = touchPoint
                )
            }
        )

        FruitNinjaHud(
            score = gameState.score,
            lives = gameState.lives,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
        )

        if (gameState.isGameOver) {
            FruitNinjaGameOverPanel(
                score = gameState.score,
                onRestartClick = {
                    gameState = game.createInitialState()
                },
                onBackToMenuClick = onBackToMenuClick,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun FruitNinjaGameCanvas(
    gameState: FruitNinjaGameState,
    onCanvasSizeChanged: (Float, Float) -> Unit,
    onSlice: (Offset) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        onSlice(change.position)
                    }
                )
            }
    ) {
        onCanvasSizeChanged(size.width, size.height)

        gameState.items.forEach { item ->
            drawCircle(
                color = item.type.color(),
                radius = item.radius,
                center = item.position
            )

            val textLayoutResult = textMeasurer.measure(
                text = item.type.label(),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp
                )
            )

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = item.position.x - textLayoutResult.size.width / 2,
                    y = item.position.y - textLayoutResult.size.height / 2
                )
            )
        }
    }
}

@Composable
private fun FruitNinjaHud(
    score: Int,
    lives: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Puntos: $score",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Vidas: $lives",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FruitNinjaGameOverPanel(
    score: Int,
    onRestartClick: () -> Unit,
    onBackToMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.92f),
                shape = MaterialTheme.shapes.large
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Juego terminado",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Puntaje final: $score",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reiniciar")
        }

        Button(
            onClick = onBackToMenuClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(text = "Volver al menú")
        }
    }
}