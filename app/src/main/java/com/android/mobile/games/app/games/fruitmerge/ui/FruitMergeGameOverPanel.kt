package com.android.mobile.games.app.games.fruitmerge.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FruitMergeGameOverPanel(
    score: Int,
    bestScore: Int,
    onRestartClick: () -> Unit,
    onBackToMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.86f)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Over",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Best: $bestScore",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = onRestartClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Restart")
            }

            OutlinedButton(
                onClick = onBackToMenuClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Menu")
            }
        }
    }
}
