package com.android.mobile.games.app.games.catchgame.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.mobile.games.app.R
import com.android.mobile.games.app.games.catchgame.model.CatchGameDifficulty

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState())
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Catch Game",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CatchDifficultyCard(
                    difficulty = CatchGameDifficulty.EASY,
                    selectedDifficulty = selectedDifficulty,
                    onClick = onDifficultySelected,
                    modifier = Modifier.weight(1f)
                )

                CatchDifficultyCard(
                    difficulty = CatchGameDifficulty.MEDIUM,
                    selectedDifficulty = selectedDifficulty,
                    onClick = onDifficultySelected,
                    modifier = Modifier.weight(1f)
                )

                CatchDifficultyCard(
                    difficulty = CatchGameDifficulty.HARD,
                    selectedDifficulty = selectedDifficulty,
                    onClick = onDifficultySelected,
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = onStartGameClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Start")
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Back")
            }
        }
    }
}

@Composable
private fun CatchDifficultyCard(
    difficulty: CatchGameDifficulty,
    selectedDifficulty: CatchGameDifficulty,
    onClick: (CatchGameDifficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = difficulty == selectedDifficulty

    Card(
        onClick = {
            onClick(difficulty)
        },
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = difficulty.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = difficulty.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
