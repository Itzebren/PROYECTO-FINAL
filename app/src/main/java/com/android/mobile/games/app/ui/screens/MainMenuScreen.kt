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
    onLaRazaRunClick: () -> Unit,
    onCatchGameClick: () -> Unit,
    onCodeMergeClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CuteCream),
        contentAlignment = Alignment.TopCenter
    ) {
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

            GameMenuButton(
                title = "🗡️ Code Slasher 🎀",
                description = "¡Corta los bugs molestos con estilo mágico!",
                color = CutePink,
                onClick = onCodeSlasherClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton(
                title = "🚇 La Raza Run 🏃‍♀️",
                description = "¡Corre por el Transbordo de la Ciencia y llega a clase antes de que el tiempo se agote!",
                color = CuteYellow,
                onClick = onLaRazaRunClick
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
                title = "💻 Code Merge 🚀",
                description = "¡Fusiona el código hasta compilar tu proyecto final!",
                color = CutePeach,
                onClick = onCodeMergeClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

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