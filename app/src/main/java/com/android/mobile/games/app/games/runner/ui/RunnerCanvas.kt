package com.android.mobile.games.app.games.runner.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import com.android.mobile.games.app.games.runner.engine.RunnerGameEngine
import com.android.mobile.games.app.games.runner.model.RunnerGameState
import com.android.mobile.games.app.games.runner.model.RunnerObstacle
import com.android.mobile.games.app.games.runner.model.RunnerObstacleType

@Composable
fun RunnerCanvas(
    gameState: RunnerGameState,
    gameEngine: RunnerGameEngine,
    onCanvasSizeChanged: (Float, Float) -> Unit,
    onJump: () -> Unit
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(gameState.isGameOver) {
                detectTapGestures(
                    onTap = {
                        onJump()
                    }
                )
            }
    ) {
        onCanvasSizeChanged(size.width, size.height)

        val groundLineY = gameEngine.getGroundY(size.height)
        val playerSize = gameEngine.getPlayerSize(size.width)

        drawRunnerBackground(
            state = gameState,
            groundLineY = groundLineY
        )

        gameState.obstacles.forEach { obstacle ->
            drawObstacle(obstacle)
        }

        drawDino(
            topLeft = Offset(
                x = gameState.playerX,
                y = gameState.playerY
            ),
            size = playerSize,
            isJumping = gameState.isJumping
        )
    }
}

private fun DrawScope.drawRunnerBackground(
    state: RunnerGameState,
    groundLineY: Float
) {
    val skyColor = if (state.score >= 450) {
        Color(0xFFD6C7FF) // Cute night lavender
    } else {
        Color(0xFFFFF0F5) // Cute day pink
    }
    val groundColor = if (state.score >= 450) {
        Color(0xFFFFB4D6) // Pastel pink ground at night
    } else {
        Color(0xFFB5EAD7) // Pastel mint green ground at day
    }

    drawRect(
        color = skyColor,
        size = size
    )

    state.cloudOffsets.forEachIndexed { index, offset ->
        drawCloud(
            center = Offset(
                x = size.width * offset,
                y = size.height * (0.16f + index * 0.07f)
            ),
            color = Color.White.copy(alpha = 0.7f)
        )
    }

    drawLine(
        color = groundColor.copy(alpha = 0.8f),
        start = Offset(0f, groundLineY),
        end = Offset(size.width, groundLineY),
        strokeWidth = 6f
    )

    val groundOffset = (state.distance % 80f)
    repeat((size.width / 80f).toInt() + 2) { index ->
        val x = index * 80f - groundOffset
        drawLine(
            color = Color.White.copy(alpha = 0.8f),
            start = Offset(x, groundLineY + 14f),
            end = Offset(x + 22f, groundLineY + 14f),
            strokeWidth = 3f
        )
    }
}

private fun DrawScope.drawCloud(
    center: Offset,
    color: Color
) {
    drawRoundRect(
        color = color,
        topLeft = center + Offset(-42f, -7f),
        size = Size(84f, 14f),
        cornerRadius = CornerRadius(8f, 8f)
    )
    drawCircle(
        color = color,
        radius = 14f,
        center = center + Offset(-22f, -8f)
    )
    drawCircle(
        color = color,
        radius = 18f,
        center = center + Offset(4f, -12f)
    )
    drawCircle(
        color = color,
        radius = 12f,
        center = center + Offset(28f, -6f)
    )
}

private fun DrawScope.drawDino(
    topLeft: Offset,
    size: Float,
    isJumping: Boolean
) {
    val bodyColor = Color(0xFFFF94B8) // Super cute pink Dino!
    val legOffset = if (isJumping) size * 0.04f else 0f

    drawRoundRect(
        color = bodyColor,
        topLeft = topLeft + Offset(size * 0.18f, size * 0.34f),
        size = Size(size * 0.46f, size * 0.42f),
        cornerRadius = CornerRadius(size * 0.07f, size * 0.07f)
    )

    drawRoundRect(
        color = bodyColor,
        topLeft = topLeft + Offset(size * 0.48f, size * 0.12f),
        size = Size(size * 0.34f, size * 0.28f),
        cornerRadius = CornerRadius(size * 0.05f, size * 0.05f)
    )

    drawRect(
        color = bodyColor,
        topLeft = topLeft + Offset(size * 0.08f, size * 0.44f),
        size = Size(size * 0.18f, size * 0.1f)
    )

    drawRect(
        color = Color.White,
        topLeft = topLeft + Offset(size * 0.7f, size * 0.19f),
        size = Size(size * 0.04f, size * 0.04f)
    )

    drawRect(
        color = bodyColor,
        topLeft = topLeft + Offset(size * 0.31f, size * 0.72f),
        size = Size(size * 0.1f, size * 0.22f + legOffset)
    )
    drawRect(
        color = bodyColor,
        topLeft = topLeft + Offset(size * 0.52f, size * 0.72f + legOffset),
        size = Size(size * 0.1f, size * 0.22f - legOffset)
    )

    drawRect(
        color = bodyColor,
        topLeft = topLeft + Offset(size * 0.62f, size * 0.43f),
        size = Size(size * 0.12f, size * 0.06f)
    )
}

private fun DrawScope.drawObstacle(
    obstacle: RunnerObstacle
) {
    when (obstacle.type) {
        RunnerObstacleType.SMALL_CACTUS -> drawCactus(
            obstacle = obstacle,
            arms = 1
        )

        RunnerObstacleType.TALL_CACTUS -> drawCactus(
            obstacle = obstacle,
            arms = 2
        )

        RunnerObstacleType.DOUBLE_CACTUS -> {
            drawCactus(
                obstacle = obstacle.copy(width = obstacle.width * 0.48f),
                arms = 1
            )
            drawCactus(
                obstacle = obstacle.copy(
                    x = obstacle.x + obstacle.width * 0.48f,
                    width = obstacle.width * 0.48f,
                    height = obstacle.height * 0.92f,
                    y = obstacle.y + obstacle.height * 0.08f
                ),
                arms = 2
            )
        }
    }
}

private fun DrawScope.drawCactus(
    obstacle: RunnerObstacle,
    arms: Int
) {
    val cactusColor = Color(0xFF9DE0AD) // Pastel green cactus
    val bodyPath = Path().apply {
        moveTo(obstacle.x + obstacle.width * 0.34f, obstacle.y + obstacle.height)
        lineTo(obstacle.x + obstacle.width * 0.34f, obstacle.y + obstacle.height * 0.12f)
        quadraticTo(
            obstacle.x + obstacle.width * 0.5f,
            obstacle.y,
            obstacle.x + obstacle.width * 0.66f,
            obstacle.y + obstacle.height * 0.12f
        )
        lineTo(obstacle.x + obstacle.width * 0.66f, obstacle.y + obstacle.height)
        close()
    }

    drawPath(
        path = bodyPath,
        color = cactusColor
    )

    if (arms >= 1) {
        drawRoundRect(
            color = cactusColor,
            topLeft = Offset(
                x = obstacle.x + obstacle.width * 0.08f,
                y = obstacle.y + obstacle.height * 0.42f
            ),
            size = Size(
                width = obstacle.width * 0.34f,
                height = obstacle.height * 0.14f
            ),
            cornerRadius = CornerRadius(8f, 8f)
        )
    }

    if (arms >= 2) {
        drawRoundRect(
            color = cactusColor,
            topLeft = Offset(
                x = obstacle.x + obstacle.width * 0.56f,
                y = obstacle.y + obstacle.height * 0.28f
            ),
            size = Size(
                width = obstacle.width * 0.34f,
                height = obstacle.height * 0.13f
            ),
            cornerRadius = CornerRadius(8f, 8f)
        )
    }
}
