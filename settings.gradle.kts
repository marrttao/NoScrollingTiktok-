rootProject.name = "unscroll"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven {
            // Morphe publishes its patcher and Gradle plugin to GitHub Packages.
            // Reading requires a GitHub token with `read:packages` (CI uses GITHUB_TOKEN).
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MorpheApp/registry")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("app.morphe.patches") version "1.3.4"
}

settings {
    extensions {
        defaultNamespace = "app.unscroll.extension"

        // Must be absolute, otherwise extension subprojects cannot resolve it.
        proguardFiles(rootProject.projectDir.resolve("extensions/proguard-rules.pro").toString())
    }
}
