package com.battlebucks.myapplication.leaderboard

import com.battlebucks.myapplication.core.domain.LeaderboardEntry
import com.battlebucks.myapplication.core.domain.Player
import com.battlebucks.myapplication.core.domain.ScoreUpdate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Module 2: Leaderboard Processor
 *
 * Responsibilities:
 * - Listen to real-time score updates
 * - Maintain leaderboard state
 * - Apply ranking rules
 * - Expose leaderboard as reactive stream
 *
 * This module does NOT generate scores itself
 */
class LeaderboardProcessor(
    private val initialPlayers: List<Player>,
    private val rankingEngine: RankingEngine = RankingEngine()
) {
    // Thread-safe score storage
    private val scores = mutableMapOf<String, Long>().apply {
        val seedScores = listOf(3200L, 3100L, 3000L, 2900L, 1450L, 1200L, 1100L, 1000L)
        initialPlayers.forEachIndexed { index, player ->
            this[player.id] = if (index < seedScores.size) seedScores[index] else 0L
        }
    }

    private val mutex = Mutex()

    // Reactive leaderboard state
    private val _leaderboardState = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboardState: StateFlow<List<LeaderboardEntry>> = _leaderboardState.asStateFlow()

    suspend fun initialize() {
        mutex.withLock {
            updateLeaderboard()
        }
    }

    /**
     * Process a single score update
     * Thread-safe using Mutex
     */
    suspend fun processScoreUpdate(update: ScoreUpdate) {
        mutex.withLock {
            // Update score (only if higher)
            val currentScore = scores[update.playerId] ?: 0L
            if (update.newScore > currentScore) {
                scores[update.playerId] = update.newScore
            }

            // Recalculate leaderboard
            updateLeaderboard()
        }
    }

    /**
     * Process multiple updates in batch for efficiency
     */
    suspend fun processBatchUpdates(updates: List<ScoreUpdate>) {
        mutex.withLock {
            updates.forEach { update ->
                val currentScore = scores[update.playerId] ?: 0L
                if (update.newScore > currentScore) {
                    scores[update.playerId] = update.newScore
                }
            }
            updateLeaderboard()
        }
    }

    /**
     * Recalculate leaderboard using RankingEngine
     * This is the core business logic
     */
    private suspend fun updateLeaderboard() {
        // Get current scores sorted by score DESC
        val sortedPlayers = scores.entries
            .sortedByDescending { it.value }
            .mapNotNull { entry ->
                val player = initialPlayers.find { it.id == entry.key }
                if (player != null) {
                    player to entry.value
                } else {
                    null
                }
            }

        // Calculate ranks using Ranking Engine
        val rankedEntries = rankingEngine.calculateRanks(sortedPlayers)

        // Update state
        _leaderboardState.emit(rankedEntries)
    }
}