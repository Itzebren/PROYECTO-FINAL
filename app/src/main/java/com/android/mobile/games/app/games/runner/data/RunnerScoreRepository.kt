package com.android.mobile.games.app.games.runner.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val RUNNER_SCORE_DATASTORE_NAME = "runner_scores"

private val Context.runnerScoreDataStore by preferencesDataStore(
    name = RUNNER_SCORE_DATASTORE_NAME
)

class RunnerScoreRepository(
    private val context: Context
) {

    fun getBestScore(): Flow<Int> {
        return context.runnerScoreDataStore.data.map { preferences ->
            preferences[BEST_SCORE_KEY] ?: 0
        }
    }

    suspend fun saveBestScoreIfNeeded(score: Int) {
        context.runnerScoreDataStore.edit { preferences ->
            val currentBestScore = preferences[BEST_SCORE_KEY] ?: 0

            if (score > currentBestScore) {
                preferences[BEST_SCORE_KEY] = score
            }
        }
    }

    private companion object {
        val BEST_SCORE_KEY = intPreferencesKey(name = "best_score")
    }
}
