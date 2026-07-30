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
        players.forEach { player ->
            this[player.id] = 0L
        }
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