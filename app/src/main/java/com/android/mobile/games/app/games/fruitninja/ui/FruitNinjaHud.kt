package com.android.mobile.games.app.games.fruitninja.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mobile.games.app.R // Asegúrate de que esta línea esté
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty

@Composable
fun FruitNinjaHud(
    score: Int,
    bestScore: Int, // <--- AÑADIDO AQUÍ
    lives: Int,
    timeRemainingSeconds: Int,
    difficulty: FruitNinjaDifficulty,
    modifier: Modifier = Modifier
) {
    val pixelStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White,
        shadow = Shadow(color = Color(0xFFD100FF), blurRadius = 8f)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF6A1B9A).copy(alpha = 0.7f)) // Lila Code Slasher
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "PUNTAJE: ${score.toString().padStart(6, '0')}", style = pixelStyle)
                Text(
                    text = "RECORD: ${bestScore.toString().padStart(6, '0')}",
                    style = pixelStyle.copy(fontSize = 12.sp, shadow = null),
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Vasos de Café
            if (difficulty.hasLives) {
                Row {
                    repeat(3) { i ->
                        Image(
                            painter = painterResource(if (i < lives) R.drawable.cafe_con_vida else R.drawable.cafe_menos_vida),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp) // <--- Aquí no debe haber error ahora
                                .padding(horizontal = 2.dp)
                        )
                    }
                }
            }
        }

        if (difficulty.hasTimer) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { timeRemainingSeconds / 60f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFFFF00FF), // Rosa neón
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}


@Composable
fun FruitNinjaHud(
    score: Int,
    lives: Int,
    timeRemainingSeconds: Int,
    difficulty: FruitNinjaDifficulty,
    modifier: Modifier = Modifier
) {
    val pixelStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White,
        shadow = Shadow(color = Color(0xFFD100FF), blurRadius = 8f)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF6A1B9A).copy(alpha = 0.7f)) // Lila Code Slasher
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "PUNTAJE: ${score.toString().padStart(6, '0')}", style = pixelStyle)
            Spacer(modifier = Modifier.weight(1f))

            // Vasos de Café
            if (difficulty.hasLives) {
                Row {
                    repeat(3) { i ->
                        Image(
                            painter = painterResource(if (i < lives) R.drawable.cafe_con_vida else R.drawable.cafe_menos_vida),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(horizontal = 2.dp)
                        )
                    }
                }
            }
        }

        if (difficulty.hasTimer) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { timeRemainingSeconds / 60f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFFFF00FF), // Rosa neón
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}
