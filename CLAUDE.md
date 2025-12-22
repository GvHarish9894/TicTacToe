# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build the project
./gradlew build

# Clean build
./gradlew clean build

# Install debug APK on connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew test --tests "com.techgv.tictactoe.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

## Architecture

Android Tic Tac Toe game using Jetpack Compose with Material 3, built with Kotlin DSL Gradle scripts.

- **Package**: `com.techgv.tictactoe`
- **Min SDK**: 24, **Target SDK**: 36
- **UI Framework**: Jetpack Compose with Material 3
- **State Management**: ViewModel + StateFlow
- **Persistence**: DataStore Preferences

### Key Architectural Patterns

**Navigation Flow**: `Splash → GameMode → Game ↔ Settings`
- Navigation defined in `ui/navigation/NavGraph.kt` using Compose Navigation
- Screen routes defined as sealed class in `ui/navigation/Screen.kt`

**Data Layer**:
- `data/repository/StatsRepository.kt` - Persists game statistics (wins, draws, streaks) via DataStore
- `data/repository/SettingsRepository.kt` - Persists user preferences (sound, haptics, player names) via DataStore
- Models in `data/model/` - `GameState`, `GameResult`, `GameStats`, `GameSettings`, `Player`

**Domain Layer**:
- `domain/GameLogic.kt` - Pure game logic as an object: win detection, move validation, board state management

**UI Layer**:
- ViewModels use `AndroidViewModel` with `StateFlow` for UI state
- `GameViewModel` manages game state, coordinates with repositories, delegates logic to `GameLogic`
- Reusable components in `ui/components/`: `GameBoard`, `GameCell`, `ScoreBoard`, `ResultDialog`, etc.

### ViewModel State Management

When creating ViewModels, follow these patterns:

1. **Use a single UI state data class** instead of multiple separate StateFlows:
   ```kotlin
   // ✅ Good: Single combined state
   data class ScreenViewState(
       val items: List<Item> = emptyList(),
       val isLoading: Boolean = false,
       val showDialog: Boolean = false
   )

   class MyViewModel : ViewModel() {
       private val _uiState = MutableStateFlow(ScreenViewState())
       val uiState: StateFlow<ScreenViewState> = _uiState.asStateFlow()
   }

   // ❌ Avoid: Multiple separate StateFlows
   private val _items = MutableStateFlow<List<Item>>(emptyList())
   private val _isLoading = MutableStateFlow(false)
   private val _showDialog = MutableStateFlow(false)
   ```

2. **Keep events separate** using `SharedFlow` (one-time events like navigation, snackbars):
   ```kotlin
   private val _events = MutableSharedFlow<UiEvent>()
   val events: SharedFlow<UiEvent> = _events.asSharedFlow()
   ```

3. **Update state atomically** using `update { it.copy(...) }`

### Game State Model

The board is represented as `List<Player>` with 9 elements (indices 0-8), where `Player` is an enum (`X`, `O`, `NONE`). `GameResult` is a sealed class: `Win(winner, winningLine)`, `Draw`, or `InProgress`.