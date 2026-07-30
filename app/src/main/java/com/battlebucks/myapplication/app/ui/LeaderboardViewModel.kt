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

const val CURRENT_USER_ID = "player_6"

class LeaderboardViewModel(
    private val scoreGenerator: ScoreGenerator,
    private val leaderboardProcessor: LeaderboardProcessor
) : ViewModel() {

    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    private val previousRanks = mutableMapOf<String, Int>()

    init {
        startLeaderboardUpdates()
    }

    private fun startLeaderboardUpdates() {
        viewModelScope.launch {
            leaderboardProcessor.initialize()
        }

        viewModelScope.launch {
            scoreGenerator.generateScoreUpdates()
                .catch { error ->
                    _uiState.value = LeaderboardUiState.Error(
                        error.message ?: "Unknown error occurred"
                    )
                }
                .collect { scoreUpdate ->
                    leaderboardProcessor.processScoreUpdate(scoreUpdate)
                }
        }

        viewModelScope.launch {
            leaderboardProcessor.leaderboardState
                .collect { entries ->
                    val rankDeltas = entries.associate { entry ->
                        val previous = previousRanks[entry.player.id]
                        val delta = if (previous != null) previous - entry.rank else 0
                        entry.player.id to delta
                    }
                    entries.forEach { previousRanks[it.player.id] = it.rank }

                    val currentUser = entries.find { it.player.id == CURRENT_USER_ID }
                    _uiState.value = LeaderboardUiState.Success(
                        entries = entries,
                        currentUserRank = currentUser?.rank ?: 0,
                        currentUserScore = currentUser?.score ?: 0L,
                        rankDeltas = rankDeltas
                    )
                }
        }
    }
}

sealed class LeaderboardUiState {
    object Loading : LeaderboardUiState()
    data class Success(
        val entries: List<LeaderboardEntry>,
        val currentUserRank: Int = 0,
        val currentUserScore: Long = 0L,
        val rankDeltas: Map<String, Int> = emptyMap()
    ) : LeaderboardUiState()
    data class Error(val message: String) : LeaderboardUiState()
}
