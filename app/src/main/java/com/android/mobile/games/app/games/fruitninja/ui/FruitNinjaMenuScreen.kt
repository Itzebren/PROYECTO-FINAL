package com.android.mobile.games.app.games.fruitninja.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty

@Composable
fun FruitNinjaMenuScreen(
    onStartGameClick: (FruitNinjaDifficulty) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedDifficulty by remember {
        mutableStateOf(FruitNinjaDifficulty.CLASSIC)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Code Slasher",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "¡Compila el código, elimina los bugs y salva el semestre!",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 12.dp, bottom = 28.dp)
        )

        Text(
            text = "Elije un modo de juego",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        DifficultySelector(
            selectedDifficulty = selectedDifficulty,
            onDifficultySelected = { difficulty ->
                selectedDifficulty = difficulty
            }
        )

        Button(
            onClick = {
                onStartGameClick(selectedDifficulty)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
        ) {
            Text(text = "Comenzar")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(text = "Menú inicial")
        }
    }
}

@Composable
private fun DifficultySelector(
    selectedDifficulty: FruitNinjaDifficulty,
    onDifficultySelected: (FruitNinjaDifficulty) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FruitNinjaDifficulty.entries.forEach { difficulty ->
            FilterChip(
                selected = selectedDifficulty == difficulty,
                onClick = {
                    onDifficultySelected(difficulty)
                },
                label = {
                    Text(text = difficulty.label)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}