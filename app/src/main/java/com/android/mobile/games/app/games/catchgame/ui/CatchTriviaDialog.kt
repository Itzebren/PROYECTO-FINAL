package com.android.mobile.games.app.games.catchgame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.mobile.games.app.games.catchgame.model.TriviaQuestion

@Composable
fun CatchTriviaDialog(
    question: TriviaQuestion,
    timeLeftSeconds: Int,
    feedbackMessage: String?,
    isAnswerLocked: Boolean,
    onAnswerSelected: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = "Last Chance",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Category: ${question.category.displayName}",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Time left: ${timeLeftSeconds}s",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = question.question,
                    style = MaterialTheme.typography.bodyLarge
                )

                question.options.forEachIndexed { index, option ->
                    Button(
                        onClick = {
                            onAnswerSelected(index)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isAnswerLocked
                    ) {
                        Text(text = option)
                    }
                }

                feedbackMessage?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {}
    )
}
