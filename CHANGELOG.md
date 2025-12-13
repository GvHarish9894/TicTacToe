# Changelog

All notable changes to the Tic Tac Toe Android application will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.0] - 2024-12-13

### Added
- **Splash Screen** with animated loading progress and game board preview
- **Game Mode Selection Screen** with Player vs Player and Player vs AI (coming soon) modes
- **Main Game Screen** with:
  - Interactive 3x3 game board
  - Turn indicator showing current player
  - Score tracking for both players
  - Reset game functionality
- **Win Detection** with 8 winning combinations (rows, columns, diagonals)
- **Animated Win Line** that draws through winning cells
- **Game Result Dialog** showing winner/draw with:
  - Trophy/handshake icon
  - Win duration display
  - Play Again and Exit options
- **Statistics Screen** with:
  - Total games played
  - Player X and Player O win counts
  - Win rates percentage
  - Draw count
  - Fastest win time
  - Current win streak
- **Settings Screen** placeholder for future customization
- **Bottom Navigation** with Home, Stats, and Settings tabs
- **DataStore Persistence** for saving game statistics
- **Dark Green Theme** with:
  - Custom color palette
  - Material 3 styling
  - Gradient backgrounds
  - Neumorphic cell design

### Technical Details
- Built with Jetpack Compose and Material 3
- MVVM architecture with ViewModels
- Navigation Compose for screen navigation
- DataStore Preferences for data persistence
- Custom animations using Compose Animation APIs

### Dependencies Added
- Navigation Compose 2.8.4
- Lifecycle ViewModel Compose 2.8.7
- DataStore Preferences 1.1.1

## [1.0.0] - Initial Release

### Added
- Basic Android project structure
- Jetpack Compose setup
- Material 3 theme configuration
- Default MainActivity with greeting placeholder

---

## Upcoming Features

### [1.3.0] - Planned
- [ ] Player vs AI mode with multiple difficulty levels
- [ ] Sound effects and haptic feedback
- [ ] Game history and replay
- [ ] Custom player names
- [ ] Theme customization (colors, board styles)

### [1.4.0] - Planned
- [ ] Online multiplayer
- [ ] Leaderboards
- [ ] Achievements
- [ ] Social sharing

---

## Project Structure

```
app/src/main/java/com/techgv/tictactoe/
├── MainActivity.kt
├── data/
│   ├── model/
│   │   ├── Player.kt
│   │   ├── GameState.kt
│   │   ├── GameResult.kt
│   │   └── GameStats.kt
│   └── repository/
│       └── StatsRepository.kt
├── domain/
│   └── GameLogic.kt
└── ui/
    ├── navigation/
    │   ├── Screen.kt
    │   └── NavGraph.kt
    ├── theme/
    │   ├── Color.kt
    │   ├── Theme.kt
    │   ├── Type.kt
    │   └── Shape.kt
    ├── components/
    │   ├── GameBoard.kt
    │   ├── GameCell.kt
    │   ├── WinLine.kt
    │   ├── ScoreBoard.kt
    │   ├── TurnIndicator.kt
    │   ├── ResultDialog.kt
    │   ├── BottomNavBar.kt
    │   └── ModeSelectionCard.kt
    └── screens/
        ├── splash/SplashScreen.kt
        ├── gamemode/GameModeScreen.kt
        ├── game/
        │   ├── GameScreen.kt
        │   └── GameViewModel.kt
        ├── stats/
        │   ├── StatsScreen.kt
        │   └── StatsViewModel.kt
        └── settings/SettingsScreen.kt
```

## Build Commands

```bash
# Build the project
./gradlew build

# Install debug APK
./gradlew installDebug

# Run unit tests
./gradlew test

# Clean build
./gradlew clean build
```
