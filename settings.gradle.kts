pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            setUrl("https://www.jitpack.io")
        }
        maven {
            setUrl("https://oss.sonatype.org/content/repositories/releases")
        }
    }
}

rootProject.name= "anitrend-v2"
include(
    ":app",
    ":app:core",
    ":app:navigation",
    // Global modules
    ":domain",
    ":data",
    // Data modules
    ":data:android",
    ":data:core",
	":data:edge",
    ":data:feed",
    ":data:imgur",
	":data:settings",
    // Android modules
    ":android:deeplink",
    ":android:navigation",
    ":android:core",
    // Feature modules
    ":feature:auth",
    ":feature:character",
	":feature:episode",
	":feature:feed",
    ":feature:staff",
    ":feature:studio",
    ":feature:suggestion",
    ":feature:media",
    ":feature:notification",
	":feature:medialist",
	":feature:medialist:editor",
    ":feature:review",
    ":feature:review:discover",
    ":feature:forum",
    ":feature:recommendation",
    ":feature:settings",
    ":feature:search",
    ":feature:profile",
    ":feature:account",
    ":feature:about",
    ":feature:news",
	":feature:image-viewer",
	":feature:updater",
	":feature:airing",
	":feature:media:discover",
	":feature:media:carousel",
	":feature:media:discover:filter",
    // Common modules
    ":common:character",
    ":common:forum",
    ":common:media",
    ":common:recommendation",
    ":common:review",
    ":common:staff",
    ":common:user",
    ":common:news",
    ":common:editor",
    ":common:episode",
    ":common:feed",
    ":common:studio",
    ":common:genre",
    ":common:tag",
	":common:shared",
	":common:markdown",
	":common:medialist",
    // Task modules
    ":task:character",
    ":task:forum",
    ":task:media",
    ":task:medialist",
    ":task:recommendation",
    ":task:review",
    ":task:staff",
    ":task:user",
    ":task:episode",
    ":task:news",
    ":task:feed",
    ":task:studio",
    ":task:genre",
    ":task:tag",
    ":task:updater",
	":task:account",
    ":task:config",
    ":task:favourite"
)
