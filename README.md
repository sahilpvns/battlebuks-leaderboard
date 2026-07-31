# Real-Time Leaderboard System

## Overview
Android application demonstrating a real-time leaderboard system for a mobile gaming platform. Built with clean architecture principles, Kotlin Coroutines, and Jetpack Compose.

## Architecture

### Module Structure




### Key Design Decisions

**1. Manual Dependency Injection**
- No Dagger/Hilt to avoid over-engineering
- Dependencies explicitly wired in MainActivity
- Shows clear understanding of dependency management

**2. Ranking Logic Lives in Domain Layer**
- RankingEngine is pure Kotlin (no Android dependencies)
- Easy to test, reusable across platforms
- UI and ViewModel only handle presentation

**3. Reactive Streams with Flow**
- ScoreGenerator emits Flow<ScoreUpdate>
- LeaderboardProcessor exposes StateFlow<List<LeaderboardEntry>>
- UI automatically updates on state changes

**4. Thread Safety**
- Mutex used in LeaderboardProcessor for concurrent updates
- Coroutines run on Dispatchers.Default (background)
- ViewModel handles lifecycle automatically

## How to Run

1. Clone the repository
2. Open in Android Studio (Ladybug or newer)
3. Build and run on emulator (API 24+) or physical device

## Performance Considerations

### UI Thread
- All heavy processing runs on background threads
- UI only collects StateFlow values
- Compose handles recomposition efficiently

### Recomposition Optimization
- Stable keys in LazyColumn (`key = { it.player.id }`)
- Stateless composables where possible
- StateFlow emissions are conflated

### Memory Leak Prevention
- ViewModel scoped coroutines auto-cancel on clear
- No direct Activity references in modules
- All flows are cold until collected

### Lifecycle Behavior
- **Rotation**: ViewModel survives, state preserved
- **Background**: Coroutines continue (simulation)
- Would pause in production using lifecycle-aware coroutines

## Scaling

### 1K Users
- Current architecture works efficiently
- O(1) updates, O(n log n) ranking
- Memory: ~1MB for data

### 100K Users
- Implement bucket-based ranking
- Use incremental updates instead of full recomputation
- Consider Redis/Elasticache for distributed state
- Shard processing across multiple instances

## Trade-offs

### Made Consciously
1. **In-memory storage**: Simpler, real-time. Would add persistence for production
2. **Full recomputation**: Simple but O(n log n). Would use incremental for scaling
3. **No DI framework**: Avoids complexity. Would add for larger teams
4. **20 sample players**: Enough to demonstrate. Configurable for testing

## What I'd Improve with More Time

1. **Persistence**: Room for offline support and crash recovery
2. **WebSocket Integration**: Real backend connection instead of simulator
3. **Performance Monitoring**: Firebase Performance or custom metrics
4. **Instrumentation Tests**: Full UI integration tests
5. **Feature Flags**: Gradual rollout capability
6. **Analytics**: Track leaderboard interactions
7. **Anti-Cheat**: Score velocity checks, anomaly detection
