package com.battlebucks.myapplication.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.battlebucks.myapplication.core.domain.LeaderboardEntry
import com.battlebucks.myapplication.engine.ScoreGenerator
import com.battlebucks.myapplication.leaderboard.LeaderboardProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel - Bridge between Engine and UI
 *
 * Responsibilities:
 * - Start score generation
 * - Collect leaderboard state
 * - Expose UI state
 *
 * Ranking logic does NOT live here
 * This only manages UI state and coordinates modules
 */
class LeaderboardViewModel(
    private val scoreGenerator: ScoreGenerator,
    private val leaderboardProcessor: LeaderboardProcessor
) : ViewModel() {

    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        startLeaderboardUpdates()
    }

    private fun startLeaderboardUpdates() {
        // Collect score updates from generator
        viewModelScope.launch {
            scoreGenerator.generateScoreUpdates()
                .catch { error ->
                    _uiState.value = LeaderboardUiState.Error(
                        error.message ?: "Unknown error occurred"
                    )
                }
                .collect { scoreUpdate ->
                    // Process each update
                    leaderboardProcessor.processScoreUpdate(scoreUpdate)
                }
        }

        // Collect leaderboard state from processor
        viewModelScope.launch {
            leaderboardProcessor.leaderboardState
                .collect { entries ->
                    _uiState.value = LeaderboardUiState.Success(entries)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Coroutines are automatically cancelled in viewModelScope
        // No additional cleanup needed
    }
}

/**
 * UI State representation
 */
sealed class LeaderboardUiState {
    object Loading : LeaderboardUiState()
    data class Success(val entries: List<LeaderboardEntry>) : LeaderboardUiState()
    data class Error(val message: String) : LeaderboardUiState()
}