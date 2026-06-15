package com.android.mobile.games.app.games.fruitmerge.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.android.mobile.games.app.games.fruitmerge.assets.imageRes
import com.android.mobile.games.app.games.fruitmerge.engine.FruitMergeGameEngine
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeFruit
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeFruitType
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeGameState
import kotlin.math.roundToInt

@Composable
fun FruitMergeCanvas(
    gameState: FruitMergeGameState,
    gameEngine: FruitMergeGameEngine,
    onCanvasSizeChanged: (Float, Float) -> Unit,
    onMoveDropX: (Float) -> Unit,
    onDrop: () -> Unit
) {
    val fruitImages = rememberFruitMergeImages()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onMoveDropX(offset.x)
                    onDrop()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onMoveDropX(offset.x)
                    },
                    onDrag = { change, _ ->
                        onMoveDropX(change.position.x)
                    },
                    onDragEnd = {
                        onDrop()
                    }
                )
            }
    ) {
        onCanvasSizeChanged(
            size.width,
            size.height
        )

        drawFruitMergePlayfield()

        drawDangerLine()

        drawDropPreview(
            gameState = gameState,
            gameEngine = gameEngine,
            fruitImages = fruitImages
        )

        gameState.fruits.forEach { fruit ->
            val image = fruitImages[fruit.type]

            if (image != null) {
                drawFruit(
                    fruit = fruit,
                    radius = gameEngine.getFruitRadius(
                        type = fruit.type,
                        width = size.width
                    ),
                    image = image
                )
            }
        }
    }
}

@Composable
private fun rememberFruitMergeImages(): Map<FruitMergeFruitType, ImageBitmap> {
    return FruitMergeFruitType.entries.associateWith { type ->
        ImageBitmap.imageResource(
            id = type.imageRes()
        )
    }
}

private fun DrawScope.drawFruitMergePlayfield() {
    // Outside background
    drawRect(
        color = Color(0xFFE6E6FA), // Pastel Lavender
        size = size
    )

    val wallColor = Color(0xFFFFB4D6) // Cute pastel pink walls
    val wallWidth = size.width * 0.035f
    val left = size.width * 0.07f
    val right = size.width * 0.93f
    val bottom = size.height * 0.95f

    // Playfield inside background
    drawRect(
        color = Color(0xFFFFF9FA), // Off-white cream inside playfield
        topLeft = Offset(
            x = left,
            y = size.height * 0.15f
        ),
        size = Size(
            width = right - left,
            height = bottom - size.height * 0.15f
        )
    )

    drawLine(
        color = wallColor,
        start = Offset(left, size.height * 0.15f),
        end = Offset(left, bottom),
        strokeWidth = wallWidth,
        cap = StrokeCap.Round
    )
    drawLine(
        color = wallColor,
        start = Offset(right, size.height * 0.15f),
        end = Offset(right, bottom),
        strokeWidth = wallWidth,
        cap = StrokeCap.Round
    )
    drawLine(
        color = wallColor,
        start = Offset(left, bottom),
        end = Offset(right, bottom),
        strokeWidth = wallWidth,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawDangerLine() {
    val lineY = size.height * 0.2f

    drawLine(
        color = Color(0xFFFF69B4), // Kawaii hot pink danger line
        start = Offset(size.width * 0.09f, lineY),
        end = Offset(size.width * 0.91f, lineY),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawDropPreview(
    gameState: FruitMergeGameState,
    gameEngine: FruitMergeGameEngine,
    fruitImages: Map<FruitMergeFruitType, ImageBitmap>
) {
    if (gameState.isGameOver) return

    val image = fruitImages[gameState.currentFruitType] ?: return
    val radius = gameEngine.getFruitRadius(
        type = gameState.currentFruitType,
        width = size.width
    )
    val x = size.width * gameState.dropXRatio
    val center = Offset(
        x = x,
        y = size.height * 0.1f
    )

    drawLine(
        color = Color.White.copy(alpha = 0.32f),
        start = Offset(x, size.height * 0.15f),
        end = Offset(x, size.height * 0.95f),
        strokeWidth = 3f
    )

    drawCircle(
        color = Color.White.copy(alpha = 0.22f),
        radius = radius,
        center = center,
        style = Stroke(width = 3f)
    )

    drawCenteredImage(
        image = image,
        center = center,
        imageSize = (radius * 2.05f).roundToInt()
    )
}

private fun DrawScope.drawFruit(
    fruit: FruitMergeFruit,
    radius: Float,
    image: ImageBitmap
) {
    drawCircle(
        color = Color.Black.copy(alpha = 0.22f),
        radius = radius * 0.96f,
        center = fruit.position + Offset(
            x = 0f,
            y = radius * 0.08f
        )
    )

    drawCenteredImage(
        image = image,
        center = fruit.position,
        imageSize = (radius * 2.18f).roundToInt()
    )
}

private fun DrawScope.drawCenteredImage(
    image: ImageBitmap,
    center: Offset,
    imageSize: Int
) {
    drawImage(
        image = image,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(
            width = image.width,
            height = image.height
        ),
        dstOffset = IntOffset(
            x = (center.x - imageSize / 2f).roundToInt(),
            y = (center.y - imageSize / 2f).roundToInt()
        ),
        dstSize = IntSize(
            width = imageSize,
            height = imageSize
        )
    )
}
