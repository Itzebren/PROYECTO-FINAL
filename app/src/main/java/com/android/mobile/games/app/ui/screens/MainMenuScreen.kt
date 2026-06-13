package com.android.mobile.games.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mobile.games.app.ui.theme.*
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun MainMenuScreen(
    onCodeSlasherClick: () -> Unit,
    onCatchGameClick: () -> Unit,
    onRunnerGameClick: () -> Unit,
    onFruitMergeClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Floating bubble state for cute background
    val floatItems = remember {
        List(25) {
            FloatItem(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 16f + 8f,
                speed = Random.nextFloat() * 0.4f + 0.1f,
                isHeart = Random.nextBoolean(),
                color = when (Random.nextInt(4)) {
                    0 -> CutePink.copy(alpha = 0.35f)
                    1 -> CuteLavender.copy(alpha = 0.35f)
                    2 -> CuteBlue.copy(alpha = 0.35f)
                    else -> CuteYellow.copy(alpha = 0.35f)
                }
            )
        }
    }

    val time by rememberInfiniteTransition(label = "kawaii_bg")
        .animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(tween(40_000, easing = LinearEasing)),
            label = "time"
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF0F5), // LavenderBlush
                        Color(0xFFFFE4E1), // MistyRose
                        Color(0xFFE6E6FA)  // Lavender
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // Cute floating background canvas (Hearts and bubbles)
        val heartPath = remember { Path() }
        Canvas(Modifier.fillMaxSize()) {
            floatItems.forEach { item ->
                val yy = ((item.y - time * item.speed * 0.001f) % 1.0f + 1.0f) % 1.0f
                val xx = item.x * size.width
                val yPos = yy * size.height
                val bounce = sin(time * 0.02f + xx) * 6f

                if (item.isHeart) {
                    drawHeart(
                        path = heartPath,
                        center = Offset(xx, yPos + bounce),
                        size = item.size * 1.5f,
                        color = item.color
                    )
                } else {
                    drawCircle(
                        color = item.color,
                        radius = item.size,
                        center = Offset(xx, yPos + bounce)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Kawaii Header
            Text(
                text = "🌸 KAWAII ARCADE 🎀",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark,
                textAlign = TextAlign.Center
            )

            Text(
                text = "¡Elige un minijuego súper lindo!",
                color = TextDark.copy(alpha = 0.7f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // Game Cards
            GameMenuButton(
                title = "🗡️ Code Slasher 🎀",
                description = "¡Corta los bugs molestos con estilo mágico!",
                color = CutePink,
                onClick = onCodeSlasherClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton(
                title = "📜 The Last Dictamen ⚖️",
                description = "¡Sobrevive las 18 semanas en la ESCOM recolectando café y útiles de ingeniería!",
                color = CuteLavender,
                onClick = onCatchGameClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton(
                title = "🦖 Code Runner ☁️",
                description = "¡Salta las nubecitas y cactus del desierto pastel!",
                color = CuteSkyBlue,
                onClick = onRunnerGameClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton(
                title = "🍉 Suika Merge 🍓",
                description = "¡Combina las frutas tiernas para crear una sandía gigante!",
                color = CutePeach,
                onClick = onFruitMergeClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Custom Draw Helper for Hearts on Canvas
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHeart(
    path: Path,
    center: Offset,
    size: Float,
    color: Color
) {
    path.reset()
    val width = size
    val height = size
    val x = center.x - width / 2
    val y = center.y - height / 2

    path.moveTo(x + width / 2, y + height / 5)
    path.cubicTo(x + width * 5 / 6, y - height / 10, x + width * 5 / 4, y + height / 3, x + width / 2, y + height * 9 / 10)
    path.cubicTo(x - width / 4, y + height / 3, x + width / 6, y - height / 10, x + width / 2, y + height / 5)
    path.close()

    drawPath(path = path, color = color)
}

@Composable
private fun GameMenuButton(
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = color.copy(alpha = 0.5f),
                spotColor = color
            )
            .border(2.dp, Color.White, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = CuteCream.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = TextDark
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¡Jugar! ✨",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

private data class FloatItem(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val isHeart: Boolean,
    val color: Color
)

