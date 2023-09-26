pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Organize"
include(":app")
include(":res")
include(":core")
include(":data")
include(":entity")
include(":screen:splash")
include(":screen:tasks")
include(":dialog:newtask")
include(":dialog:time")
include(":dialog:date")
include(":dialog:priority")
include(":dialog:category")
include(":dialog:recall")
