pluginManagement {
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Budget Manager"
include(":app")
include(":feature:home")
include(":feature:settings")
include(":feature:reports")
include(":feature:create")
include(":feature:auth")
include(":uikit")
include(":common")
include(":core")
include(":data")
include(":domain")
