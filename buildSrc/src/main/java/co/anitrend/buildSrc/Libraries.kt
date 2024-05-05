/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.buildSrc

import co.anitrend.buildSrc.module.Modules

object Libraries {

    object Repositories {
        const val jitPack = "https://www.jitpack.io"
        const val sonatypeReleases = "https://oss.sonatype.org/content/repositories/releases"
    }

    object AniTrend {

        object CommonUi {
            val character = Modules.Common.Character.path()
            val forum = Modules.Common.Forum.path()
            val media = Modules.Common.Media.path()
            val recommendation = Modules.Common.Recommendation.path()
            val review = Modules.Common.Review.path()
            val staff = Modules.Common.Staff.path()
            val user = Modules.Common.User.path()
            val episode = Modules.Common.Episode.path()
            val news = Modules.Common.News.path()
            val mediaList = Modules.Common.MediaList.path()
            val editor = Modules.Common.Editor.path()
            val feed = Modules.Common.Feed.path()
            val studio = Modules.Common.Studio.path()
            val genre = Modules.Common.Genre.path()
            val tag = Modules.Common.Tag.path()
			val shared = Modules.Common.Shared.path()
			val markdown = Modules.Common.Markdown.path()
        }

        object Data {
            val android = Modules.Data.Android.path()
            val core = Modules.Data.Core.path()
            val feed = Modules.Data.Feed.path()
            //val imgur = Modules.Data.Imgur.path()
            val jikan = Modules.Data.Jikan.path()
            val relation = Modules.Data.Relation.path()
            //val theme = Modules.Data.Theme.path()
            //val thexem = Modules.Data.TheXem.path()
            //val tmdb = Modules.Data.Tmdb.path()
            //val trakt = Modules.Data.Trakt.path()
            val settings = Modules.Data.Settings.path()
            val edge = Modules.Data.Edge.path()
        }
    }
}
