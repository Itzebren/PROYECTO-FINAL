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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import com.android.mobile.games.app.games.runner.engine.RunnerGameEngine
import com.android.mobile.games.app.games.runner.model.RunnerGameState
import com.android.mobile.games.app.games.runner.model.RunnerObstacle
import com.android.mobile.games.app.games.runner.model.RunnerObstacleType

@Composable
fun RunnerCanvas(
    gameState: RunnerGameState,
    gameEngine: RunnerGameEngine,
    backgroundImage: ImageBitmap,
    playerImage: ImageBitmap,
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
            groundLineY = groundLineY,
            backgroundImage = backgroundImage
        )

        gameState.obstacles.forEach { obstacle ->
            drawObstacle(obstacle)
        }

        drawPlayer(
            image = playerImage,
            topLeft = Offset(
                x = gameState.playerX,
                y = gameState.playerY
            ),
            size = playerSize,
            isJumping = gameState.isJumping,
            distance = gameState.distance
        )
    }
}

private fun DrawScope.drawRunnerBackground(
    state: RunnerGameState,
    groundLineY: Float,
    backgroundImage: ImageBitmap
) {
    val scaleFactor = size.height / backgroundImage.height.toFloat()
    val scaledWidth = backgroundImage.width * scaleFactor

    val bgOffset = (state.distance * 0.3f) % scaledWidth

    var currentX = -bgOffset
    while (currentX < size.width) {
        translate(left = currentX, top = 0f) {
            scale(scaleX = scaleFactor, scaleY = scaleFactor, pivot = Offset.Zero) {
                drawImage(image = backgroundImage)
            }
        }
        currentX += scaledWidth
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

private fun DrawScope.drawPlayer(
    image: ImageBitmap,
    topLeft: Offset,
    size: Float,
    isJumping: Boolean,
    distance: Float
) {
    val scaleX = size / image.width.toFloat()
    val scaleY = size / image.height.toFloat()

    // Animación de carrera (saltitos y rotación)
    val runningPhase = distance * 0.05f
    val bobbingY = if (isJumping) 0f else kotlin.math.abs(kotlin.math.sin(runningPhase)) * size * 0.15f
    val rotation = if (isJumping) 0f else kotlin.math.sin(runningPhase) * 12f

    translate(left = topLeft.x, top = topLeft.y - bobbingY) {
        rotate(degrees = rotation, pivot = Offset(size / 2f, size)) {
            scale(scaleX = scaleX, scaleY = scaleY, pivot = Offset.Zero) {
                drawImage(image = image)
            }
        }
    }
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