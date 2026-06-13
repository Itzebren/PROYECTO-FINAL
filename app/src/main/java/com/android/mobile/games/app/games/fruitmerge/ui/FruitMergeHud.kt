package com.android.mobile.games.app.games.fruitmerge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeFruitType
import com.android.mobile.games.app.ui.theme.*

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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, CutePink, RoundedCornerShape(20.dp)),
        color = CuteCream.copy(alpha = 0.9f)
    ) {
        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreText(
                label = "Score 🍉",
                value = score.toString()
            )

            ScoreText(
                label = "Best 👑",
                value = bestScore.toString()
            )

            ScoreText(
                label = "Next ✨",
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
        color = TextDark,
        fontSize = 14.sp,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
}
