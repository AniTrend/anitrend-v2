/*
 * Copyright (C) 2021  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.buildSrc.module

internal object Modules {

    const val androidModulePattern = "android-"
    const val featureModulePattern = "feature-"
    const val commonModulePattern = "common-"
    const val taskModulePattern = "task-"
    const val appModulePattern = "app-"

    interface Module {
        val id: String

        /**
         * @return Formatted id of module as a path string
         */
        fun path(): String = ":$id"
    }

    enum class App(override val id: String) : Module {
        Main("app"),
        Core("app-core"),
        Data("app-data"),
        Domain("app-domain"),
        Navigation("app-navigation")
    }

    enum class Android(override val id: String) : Module {
        Core("android-core"),
        Navigation("android-navigation"),
        OnBoarding("android-onboarding"),
        Splash("android-splash")
    }

    enum class Common(override val id: String) : Module {
        Character("common-character-ui"),
        Forum("common-forum-ui"),
        Media("common-media-ui"),
        Recommendation("common-recommendation-ui"),
        Review("common-review-ui"),
        Staff("common-staff-ui"),
        User("common-user-ui"),
        Episode("common-episode-ui"),
        News("common-news-ui"),
        Editor("common-editor-ui"),
        Feed("common-feed-ui"),
        Studio("common-studio-ui"),
        Genre("common-genre-ui"),
        Tag("common-tag-ui"),
        Shared("common-shared-ui"),
        Markdown("common-markdown-ui")
    }

    enum class Feature(override val id: String) : Module {
        Auth("feature-auth"),
        Airing("feature-airing"),
        Character("feature-character"),
        Episode("feature-episode"),
        Staff("feature-staff"),
        Studio("feature-studio"),
        Media("feature-media"),
        MediaCarousel("feature-media-carousel"),
        MediaDiscover("feature-media-discover"),
        MediaDiscoverFilter("feature-media-discover-filter"),
        Notification("feature-notification"),
        MediaList("feature-medialist"),
        Review("feature-review"),
        Forum("feature-forum"),
        Recommendation("feature-recommendation"),
        Settings("feature-settings"),
        Search("feature-search"),
        Profile("feature-profile"),
        Account("feature-account"),
        About("feature-about"),
        News("feature-news"),
        ImageViewer("feature-image-viewer"),
        Updater("feature-updater")
    }

    enum class Task(override val id: String) : Module {
        Character("task-character"),
        Forum("task-forum"),
        Media("task-media"),
        MediaList("task-medialist"),
        Recommendation("task-recommendation"),
        Review("task-review"),
        Staff("task-staff"),
        User("task-user"),
        Episode("task-episode"),
        News("task-news"),
        Feed("task-feed"),
        Studio("task-studio"),
        Genre("task-genre"),
        Tag("task-tag"),
        Updater("task-updater")
    }
}


