package com.battlebucks.myapplication.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.battlebucks.myapplication.app.ui.LeaderboardScreen
import com.battlebucks.myapplication.app.ui.LeaderboardViewModel
import com.battlebucks.myapplication.core.domain.Player
import com.battlebucks.myapplication.engine.ScoreGenerator
import com.battlebucks.myapplication.leaderboard.LeaderboardProcessor
import com.battlebucks.myapplication.leaderboard.RankingEngine

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: LeaderboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MANUAL DEPENDENCY INJECTION - No DI framework
        val players = createPlayers()
        val rankingEngine = RankingEngine()
        val leaderboardProcessor = LeaderboardProcessor(players, rankingEngine)
        val scoreGenerator = ScoreGenerator(players)
        viewModel = LeaderboardViewModel(scoreGenerator, leaderboardProcessor)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LeaderboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun createPlayers(): List<Player> {
        return (1..20).map { index ->
            Player(
                id = "player_$index",
                username = "Player$index"
            )
        }
    }
}
