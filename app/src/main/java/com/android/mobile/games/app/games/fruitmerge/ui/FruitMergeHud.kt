package com.android.mobile.games.app.games.fruitmerge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeFruitType

@Composable
fun FruitMergeHud(
    score: Int,
    bestScore: Int,
    nextFruitType: FruitMergeFruitType,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        color = Color.Black.copy(alpha = 0.34f)
    ) {
        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreText(
                label = "Score",
                value = score.toString()
            )

            ScoreText(
                label = "Best",
                value = bestScore.toString()
            )

            ScoreText(
                label = "Next",
                value = nextFruitType.label
            )
        }
    }
}

@Composable
private fun ScoreText(
    label: String,
    value: String
) {
    Text(
        text = "$label: $value",
        color = Color.White,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
}
