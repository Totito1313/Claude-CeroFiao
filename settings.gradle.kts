pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CeroFiao"

include(":app")

// Core modules
include(":core:core-model")
include(":core:core-common")
include(":core:core-designsystem")
include(":core:core-database")
include(":core:core-datastore")
include(":core:core-network")
include(":core:core-data")
include(":core:core-domain")
include(":core:core-ui")

// Feature modules - Phase 1
include(":feature:feature-dashboard")
include(":feature:feature-transactions")
include(":feature:feature-accounts")
include(":feature:feature-categories")
include(":feature:feature-exchange-rates")
include(":feature:feature-settings")
include(":feature:feature-onboarding")
include(":feature:feature-budget")
include(":feature:feature-debt")
