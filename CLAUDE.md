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

This is an Android app using Jetpack Compose for UI, built with Kotlin DSL Gradle scripts.

- **Package**: `com.techgv.tictactoe`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **UI Framework**: Jetpack Compose with Material 3
- **Theme**: Supports dynamic colors on Android 12+ with fallback to custom light/dark color schemes

### Project Structure

- `app/src/main/java/com/techgv/tictactoe/` - Main application code
  - `MainActivity.kt` - Entry point activity using Compose
  - `ui/theme/` - Material 3 theming (Color, Theme, Type)
- `app/src/test/` - Unit tests (JUnit 4)
- `app/src/androidTest/` - Instrumented tests (Espresso)
- `gradle/libs.versions.toml` - Centralized dependency version catalog