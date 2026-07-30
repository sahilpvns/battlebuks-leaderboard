package com.battlebucks.myapplication.engine

import com.battlebucks.myapplication.core.domain.Player
import com.battlebucks.myapplication.core.domain.ScoreUpdate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

/**
 * Module 1: Score Generator / Game Engine Module
 *
 * Responsibilities:
 * - Maintain list of players
 * - Generate random score updates at random intervals
 * - Emit score updates continuously
 *
 * Rules:Scores
 * - Scores only increase
 * - Deterministic per session
 * - UI-agnostic, reusable, testable
 */
class ScoreGenerator(
    private val players: List<Player>
) {
    // Cache to maintain deterministic scores per session
    private val scoreCache = mutableMapOf<String, Long>().apply {
        players.forEachIndexed { index, player ->
            this[player.id] = initialScoreFor(index)
        }
    }

    private fun initialScoreFor(index: Int): Long {
        val seedScores = listOf(3200L, 3100L, 3000L, 2900L, 1450L, 1200L, 1100L, 1000L)
        return if (index < seedScores.size) seedScores[index] else Random.nextLong(200, 900)
    }

    /**
     * Generate continuous score updates at random intervals
     * Returns Flow that emits ScoreUpdate objects
     */
    fun generateScoreUpdates(): Flow<ScoreUpdate> = flow {
        while (true) {
            // Select random player
            val player = players.random()

            // Generate random score increment (1-50)
            val increment = Random.nextLong(1, 50)

            // Update cache (deterministic)
            val currentScore = scoreCache[player.id] ?: 0L
            val newScore = currentScore + increment
            scoreCache[player.id] = newScore

            // Emit update
            emit(
                ScoreUpdate(
                    playerId = player.id,
                    newScore = newScore
                )
            )

            // Random interval 500ms - 2000ms
            val delayMs = Random.nextLong(500, 2000)
            delay(delayMs.milliseconds)
        }
    }
}