package com.battlebucks.myapplication.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.battlebucks.myapplication.app.ui.LeaderboardScreen
import com.battlebucks.myapplication.app.ui.LeaderboardViewModel
import com.battlebucks.myapplication.core.domain.Player
import com.battlebucks.myapplication.engine.ScoreGenerator
import com.battlebucks.myapplication.leaderboard.LeaderboardProcessor
import com.battlebucks.myapplication.leaderboard.RankingEngine
import com.battlebucks.myapplication.ui.theme.LeaderboardAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: LeaderboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val players = createPlayers()
        val rankingEngine = RankingEngine()
        val leaderboardProcessor = LeaderboardProcessor(players, rankingEngine)
        val scoreGenerator = ScoreGenerator(players)
        viewModel = LeaderboardViewModel(scoreGenerator, leaderboardProcessor)

        setContent {
            LeaderboardAppTheme {
                LeaderboardScreen(viewModel = viewModel)
            }
        }
    }

    private fun createPlayers(): List<Player> {
        val names = listOf(
            "Deep",
            "Predekin_Singh",
            "Himanshu",
            "Manya Aggarwal",
            "Vishal",
            "Shreyas",
            "Rahul",
            "Anwesha",
            "Priya",
            "Arjun",
            "Neha",
            "Karan",
            "Sneha",
            "Rohan",
            "Divya",
            "Amit",
            "Pooja",
            "Varun",
            "Kavya",
            "Nikhil"
        )
        return names.mapIndexed { index, name ->
            Player(
                id = "player_${index + 1}",
                username = name
            )
        }
    }
}
