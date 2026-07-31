package com.battlebucks.myapplication.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import com.battlebucks.myapplication.core.domain.LeaderboardEntry
import com.battlebucks.myapplication.ui.theme.AvatarBlue
import com.battlebucks.myapplication.ui.theme.AvatarGreen
import com.battlebucks.myapplication.ui.theme.AvatarOrange
import com.battlebucks.myapplication.ui.theme.AvatarPurple
import com.battlebucks.myapplication.ui.theme.AvatarRed
import com.battlebucks.myapplication.ui.theme.AvatarYellow
import com.battlebucks.myapplication.ui.theme.BackgroundCard
import com.battlebucks.myapplication.ui.theme.BackgroundCardElevated
import com.battlebucks.myapplication.ui.theme.BackgroundDark
import com.battlebucks.myapplication.ui.theme.Gold
import com.battlebucks.myapplication.ui.theme.OrangeLight
import com.battlebucks.myapplication.ui.theme.OrangePrimary
import com.battlebucks.myapplication.ui.theme.RedAccent
import com.battlebucks.myapplication.ui.theme.TextMuted
import com.battlebucks.myapplication.ui.theme.TextPrimary
import com.battlebucks.myapplication.ui.theme.TextSecondary
import com.battlebucks.myapplication.ui.theme.TrendUp

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        RadialGlowBackground()

        when (uiState) {
            is LeaderboardUiState.Loading -> LoadingState()
            is LeaderboardUiState.Success -> {
                val state = uiState as LeaderboardUiState.Success
                LeaderboardContent(
                    entries = state.entries,
                    currentUserRank = state.currentUserRank,
                    currentUserScore = state.currentUserScore,
                    rankDeltas = state.rankDeltas
                )
            }
            is LeaderboardUiState.Error -> ErrorState((uiState as LeaderboardUiState.Error).message)
        }
    }
}

@Composable
private fun RadialGlowBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF6B1500),
                    Color(0xFF3A0A00),
                    BackgroundDark,
                    BackgroundDark
                ),
                center = Offset(size.width / 2f, size.height * 0.15f),
                radius = size.width * 1.2f
            )
        )
    }
}

@Composable
private fun LeaderboardContent(
    entries: List<LeaderboardEntry>,
    currentUserRank: Int,
    currentUserScore: Long,
    rankDeltas: Map<String, Int>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SeasonHeader()
        HeroSection(
            currentUserRank = currentUserRank,
            currentUserScore = currentUserScore
        )
        LeaderboardSection(
            entries = entries,
            rankDeltas = rankDeltas,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SeasonHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = BackgroundCardElevated,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GENESIS SEASON",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select season",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Season ends in 60 days",
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun HeroSection(
    currentUserRank: Int,
    currentUserScore: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LegendsEmblem(modifier = Modifier.size(120.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "LEGENDS",
            color = OrangePrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatPill(
                borderColor = RedAccent,
                icon = {
                    Text(
                        text = "▲",
                        color = Gold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                label = formatRank(currentUserRank)
            )
            StatPill(
                borderColor = Gold,
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(14.dp)
                    )
                },
                label = currentUserScore.toString()
            )
        }
    }
}

@Composable
private fun StatPill(
    borderColor: Color,
    icon: @Composable () -> Unit,
    label: String
) {
    Row(
        modifier = Modifier
            .border(1.dp, borderColor.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .background(BackgroundCard.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        icon()
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun LegendsEmblem(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f

        val shieldPath = Path().apply {
            moveTo(cx, h * 0.08f)
            lineTo(w * 0.85f, h * 0.25f)
            lineTo(w * 0.85f, h * 0.55f)
            quadraticTo(w * 0.85f, h * 0.85f, cx, h * 0.95f)
            quadraticTo(w * 0.15f, h * 0.85f, w * 0.15f, h * 0.55f)
            lineTo(w * 0.15f, h * 0.25f)
            close()
        }

        drawPath(
            path = shieldPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF8B0000), Color(0xFF4A0000), Color(0xFF2A0000)),
                start = Offset(cx, 0f),
                end = Offset(cx, h)
            ),
            style = Fill
        )

        drawPath(
            path = shieldPath,
            color = OrangePrimary,
            style = Stroke(width = 3f)
        )

        val flamePath = Path().apply {
            moveTo(cx, 0f)
            lineTo(cx + w * 0.12f, h * 0.12f)
            lineTo(cx + w * 0.04f, h * 0.18f)
            lineTo(cx + w * 0.08f, h * 0.22f)
            lineTo(cx, h * 0.08f)
            lineTo(cx - w * 0.08f, h * 0.22f)
            lineTo(cx - w * 0.04f, h * 0.18f)
            lineTo(cx - w * 0.12f, h * 0.12f)
            close()
        }
        drawPath(
            path = flamePath,
            brush = Brush.linearGradient(
                colors = listOf(OrangeLight, OrangePrimary, RedAccent),
                start = Offset(cx, 0f),
                end = Offset(cx, h * 0.22f)
            ),
            style = Fill
        )

        drawCircle(
            color = Color(0xFF1A1A1E),
            radius = w * 0.22f,
            center = Offset(cx, h * 0.48f)
        )

        drawCircle(
            color = Color.White,
            radius = w * 0.04f,
            center = Offset(cx - w * 0.07f, h * 0.44f)
        )
        drawCircle(
            color = Color.White,
            radius = w * 0.04f,
            center = Offset(cx + w * 0.07f, h * 0.44f)
        )

        val hoodPath = Path().apply {
            moveTo(cx - w * 0.18f, h * 0.38f)
            quadraticTo(cx, h * 0.28f, cx + w * 0.18f, h * 0.38f)
            lineTo(cx + w * 0.15f, h * 0.62f)
            quadraticTo(cx, h * 0.58f, cx - w * 0.15f, h * 0.62f)
            close()
        }
        drawPath(path = hoodPath, color = Color(0xFF0A0A0C), style = Fill)
    }
}

@Composable
private fun LeaderboardSection(
    entries: List<LeaderboardEntry>,
    rankDeltas: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(BackgroundCard.copy(alpha = 0.95f))
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Leaderboard",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .border(1.dp, TextMuted, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = TextMuted,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(
                items = entries,
                key = { it.player.id }
            ) { entry ->
                LeaderboardItem(
                    entry = entry,
                    rankDelta = rankDeltas[entry.player.id] ?: 0
                )
            }
        }
    }
}

@Composable
private fun LeaderboardItem(
    entry: LeaderboardEntry,
    rankDelta: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BackgroundCardElevated)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerAvatar(
            rank = entry.rank,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${entry.rank}.",
            color = Gold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(28.dp)
        )

        Text(
            text = entry.player.username,
            color = TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (rankDelta > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Rank up",
                    tint = TrendUp,
                    modifier = Modifier.size(14.dp)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = TrendUp,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = Gold,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = entry.score.toString(),
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PlayerAvatar(
    rank: Int,
    modifier: Modifier = Modifier
) {
    val bgColor = avatarColorForRank(rank)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(24.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f

            drawCircle(
                color = Color(0xFF1A1A1E),
                radius = w * 0.35f,
                center = Offset(cx, h * 0.42f)
            )
            drawCircle(
                color = Color.White,
                radius = w * 0.06f,
                center = Offset(cx - w * 0.1f, h * 0.38f)
            )
            drawCircle(
                color = Color.White,
                radius = w * 0.06f,
                center = Offset(cx + w * 0.1f, h * 0.38f)
            )

            val hoodPath = Path().apply {
                moveTo(cx - w * 0.3f, h * 0.32f)
                quadraticTo(cx, h * 0.18f, cx + w * 0.3f, h * 0.32f)
                lineTo(cx + w * 0.25f, h * 0.65f)
                quadraticTo(cx, h * 0.58f, cx - w * 0.25f, h * 0.65f)
                close()
            }
            drawPath(path = hoodPath, color = Color(0xFF0A0A0C), style = Fill)
        }
    }
}

private fun avatarColorForRank(rank: Int): Color = when (rank) {
    1 -> AvatarRed
    2 -> AvatarOrange
    3 -> AvatarYellow
    4 -> AvatarGreen
    5 -> AvatarPurple
    else -> AvatarBlue
}

private fun formatRank(rank: Int): String = when {
    rank <= 0 -> "—"
    rank % 100 in 11..13 -> "${rank}th"
    rank % 10 == 1 -> "${rank}st"
    rank % 10 == 2 -> "${rank}nd"
    rank % 10 == 3 -> "${rank}rd"
    else -> "${rank}th"
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = OrangePrimary)
            Text(
                text = "Loading leaderboard...",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Error",
                fontSize = 24.sp,
                color = RedAccent,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}
