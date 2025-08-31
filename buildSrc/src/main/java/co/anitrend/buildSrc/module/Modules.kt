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

    const val DATA_MODULE_PATTERN = ":data:"
    const val DOMAIN_MODULE_PATTERN = ":domain:"
    const val ANDROID_MODULE_PATTERN = ":android:"
    const val FEATURE_MODULE_PATTERN = ":feature:"
    const val COMMON_MODULE_PATTERN = ":common:"
    const val TASK_MODULE_PATTERN = ":task:"
    const val APP_MODULE_PATTERN = ":app:"

    interface Module {
        val id: String

        /**
         * @return Formatted id of module as a path string
         */
        val path: String
            get() = ":$id"
    }

    enum class App(override val id: String) : Module {
        Main("app"),
        Core("app:core"),
        Navigation("app:navigation"),
    }

    enum class Domain(override val id: String) : Module {
        Common("domain"),
    }

    enum class Data(override val id: String) : Module {
        Common("data"),
        Android("data:android"),
        Core("data:core"),
        Feed("data:feed"),
        Imgur("data:imgur"),
        Settings("data:settings"),
        Edge("data:edge"),
    }

    enum class Android(override val id: String) : Module {
        Core("android:core"),
        Navigation("android:navigation"),
        DeepLink("android:deeplink"),
    }

    enum class Common(override val id: String) : Module {
        Character("common:character"),
        Forum("common:forum"),
        Media("common:media"),
        Recommendation("common:recommendation"),
        Review("common:review"),
        Staff("common:staff"),
        User("common:user"),
        Episode("common:episode"),
        News("common:news"),
        MediaList("common:medialist"),
        Editor("common:editor"),
        Feed("common:feed"),
        Studio("common:studio"),
        Genre("common:genre"),
        Tag("common:tag"),
        Shared("common:shared"),
        Markdown("common:markdown")
    }

    enum class Feature(override val id: String) : Module {
        Auth("feature:auth"),
        Airing("feature:airing"),
        Character("feature:character"),
        Episode("feature:episode"),
        Feed("feature:feed"),
        Staff("feature:staff"),
        Studio("feature:studio"),
        Suggestion("feature:suggestion"),
        Media("feature:media"),
        MediaCarousel("feature:media:carousel"),
        MediaDiscover("feature:media:discover"),
        MediaDiscoverFilter("feature:media:discover:filter"),
        Notification("feature:notification"),
        MediaList("feature:medialist"),
        MediaListEditor("feature:medialist:editor"),
        Review("feature:review"),
        ReviewDiscover("feature:review:discover"),
        Forum("feature:forum"),
        Recommendation("feature:recommendation"),
        Settings("feature:settings"),
        Search("feature:search"),
        Profile("feature:profile"),
        Account("feature:account"),
        About("feature:about"),
        News("feature:news"),
        ImageViewer("feature:image-viewer"),
        Updater("feature:updater")
    }

    enum class Task(override val id: String) : Module {
        Character("task:character"),
        Forum("task:forum"),
        Media("task:media"),
        MediaList("task:medialist"),
        Recommendation("task:recommendation"),
        Review("task:review"),
        Staff("task:staff"),
        User("task:user"),
        Episode("task:episode"),
        News("task:news"),
        Feed("task:feed"),
        Studio("task:studio"),
        Genre("task:genre"),
        Tag("task:tag"),
        Updater("task:updater"),
        Account("task:account"),
        Config("task:config"),
        Favourite("task:favourite"),
    }
}


