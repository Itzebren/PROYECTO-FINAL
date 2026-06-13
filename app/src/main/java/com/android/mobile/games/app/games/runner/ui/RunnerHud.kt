package com.android.mobile.games.app.games.runner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mobile.games.app.ui.theme.*

@Composable
fun RunnerHud(
    score: Int,
    bestScore: Int,
    speed: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CuteCream.copy(alpha = 0.9f))
            .border(2.dp, CutePink, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RunnerHudText(text = "⭐ Score: $score")
        RunnerHudText(text = "👑 Best: $bestScore")
        RunnerHudText(text = "⚡ Speed: $speed")
    }
}

@Composable
private fun RunnerHudText(text: String) {
    Text(
        text = text,
        color = TextDark,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}
