package com.battlebucks.myapplication.core.domain

data class LeaderboardEntry(
    val player: Player,
    val score: Long,
    val rank: Int
)
