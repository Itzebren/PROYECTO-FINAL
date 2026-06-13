package com.android.mobile.games.app.games.catchgame.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mobile.games.app.R
import com.android.mobile.games.app.games.catchgame.model.CatchGameDifficulty
import com.android.mobile.games.app.ui.theme.*

@Composable
fun CatchGameMenuScreen(
    selectedDifficulty: CatchGameDifficulty,
    onDifficultySelected: (CatchGameDifficulty) -> Unit,
    onStartGameClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay transparent pink to make it cuter
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CutePink.copy(alpha = 0.15f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CuteCream.copy(alpha = 0.9f),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(3.dp, CuteLavender),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🐛 BUG CATCHER 🍦",
                        color = TextDark,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Elige tu dificultad para atrapar bugs",
                        color = TextDark.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CatchDifficultyCard(
                            difficulty = CatchGameDifficulty.EASY,
                            selectedDifficulty = selectedDifficulty,
                            onClick = onDifficultySelected,
                            color = CuteMint,
                            modifier = Modifier.weight(1f)
                        )

                        CatchDifficultyCard(
                            difficulty = CatchGameDifficulty.MEDIUM,
                            selectedDifficulty = selectedDifficulty,
                            onClick = onDifficultySelected,
                            color = CuteYellow,
                            modifier = Modifier.weight(1f)
                        )

                        CatchDifficultyCard(
                            difficulty = CatchGameDifficulty.HARD,
                            selectedDifficulty = selectedDifficulty,
                            onClick = onDifficultySelected,
                            color = CutePink,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = onStartGameClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CutePink,
                            contentColor = TextDark
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(text = "✨ ¡Comenzar! ✨", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onBackClick,
                        border = BorderStroke(2.dp, CuteLavender),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "Volver al Hub 🎀", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CatchDifficultyCard(
    difficulty: CatchGameDifficulty,
    selectedDifficulty: CatchGameDifficulty,
    onClick: (CatchGameDifficulty) -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    val isSelected = difficulty == selectedDifficulty

    Card(
        onClick = { onClick(difficulty) },
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = if (isSelected) 3.dp else 1.5.dp,
                color = if (isSelected) color else color.copy(alpha = 0.5f),
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.4f) else Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val emoji = when (difficulty) {
                CatchGameDifficulty.EASY -> "😊"
                CatchGameDifficulty.MEDIUM -> "😐"
                CatchGameDifficulty.HARD -> "💀"
            }
            Text(emoji, fontSize = 24.sp)
            Text(
                text = difficulty.label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }
}
