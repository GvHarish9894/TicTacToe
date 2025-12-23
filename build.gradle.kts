// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint("1.5.0")
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**/*.kts")
        ktlint("1.5.0")
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/detekt.yml")
    parallel = true
    source.setFrom(
        files(
            "app/src/main/java",
            "app/src/test/java",
        ),
    )
}
