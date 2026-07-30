package com.battlebucks.myapplication.leaderboard

import com.battlebucks.myapplication.core.domain.LeaderboardEntry
import com.battlebucks.myapplication.core.domain.Player


/**
 * Ranking Engine - Handles all ranking logic
 *
 * Business Rules:
 * - Sorted by score DESC
 * - Same score -> same rank
 * - Next rank skips accordingly (competition ranking: 1,2,2,4)
 *
 * This is pure business logic - no Android dependencies
 */
class RankingEngine {

    /**
     * Calculate ranks for sorted players
     * Implements competition ranking (1224 ranking)
     *
     * Example: Scores [100, 90, 90, 80]
     * Ranks:    [1, 2, 2, 4]
     */
    fun calculateRanks(
        sortedPlayers: List<Pair<Player, Long>>
    ): List<LeaderboardEntry> {
        if (sortedPlayers.isEmpty()) return emptyList()

        val result = mutableListOf<LeaderboardEntry>()
        var currentRank = 1
        var index = 0

        while (index < sortedPlayers.size) {
            val currentScore = sortedPlayers[index].second
            var count = 0

            // Count all players with same score
            while (index + count < sortedPlayers.size &&
                sortedPlayers[index + count].second == currentScore) {
                count++
            }

            // Assign same rank to all players with same score
            for (i in 0 until count) {
                val (player, score) = sortedPlayers[index + i]
                result.add(
                    LeaderboardEntry(
                        player = player,
                        score = score,
                        rank = currentRank
                    )
                )
            }

            // Skip ranks for next group (competition ranking)
            currentRank += count
            index += count
        }

        return result
    }
}