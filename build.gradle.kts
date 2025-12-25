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
            .setEditorConfigPath("$projectDir/.editorconfig")
            .customRuleSets(
                listOf("io.nlopez.compose.rules:ktlint:${libs.versions.detektComposeRules.get()}")
            )
            .editorConfigOverride(
                mapOf(
                    "max_line_length" to "120",
                    "ktlint_standard_argument-list-wrapping" to "disabled",
                    "ktlint_standard_parameter-list-wrapping" to "disabled",
                    "ktlint_standard_trailing-comma-on-call-site" to "disabled",
                    "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
                    "ktlint_standard_function-expression-body" to "disabled",
                    "ktlint_standard_class-signature" to "disabled",
                    "ktlint_standard_function-signature" to "disabled",
                )
            )
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**/*.kts")
        ktlint("1.5.0").editorConfigOverride(
            mapOf(
                "max_line_length" to "120",
                "ktlint_standard_argument-list-wrapping" to "disabled",
                "ktlint_standard_parameter-list-wrapping" to "disabled",
                "ktlint_standard_trailing-comma-on-call-site" to "disabled",
                "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
                "ktlint_standard_function-expression-body" to "disabled",
                "ktlint_standard_class-signature" to "disabled",
            )
        )
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

dependencies {
    "detektPlugins"("io.nlopez.compose.rules:detekt:${libs.versions.detektComposeRules.get()}")
}
