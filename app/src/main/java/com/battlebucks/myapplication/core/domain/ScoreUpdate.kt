package com.battlebucks.myapplication.core.domain

data class ScoreUpdate(
    val playerId: String,
    val newScore: Long,
    val timestamp: Long = System.currentTimeMillis()
)
