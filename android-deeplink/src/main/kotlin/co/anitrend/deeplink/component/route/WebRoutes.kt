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

package co.anitrend.deeplink.component.route

import android.content.Intent
import androidx.core.text.isDigitsOnly
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.*
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.forActivity
import com.hellofresh.deeplink.Action
import com.hellofresh.deeplink.DeepLinkUri
import com.hellofresh.deeplink.Environment
import timber.log.Timber
import java.util.*

internal object MainRoute : Route("home") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = NavigationDrawerRouter.Param(
            destination = NavigationDrawerRouter.Destination.HOME
        ).asNavPayload()
        return MainRouter.forActivity(env.context, payload)
    }
}

internal object ForumDiscoverRoute : Route(
    "forum/:category",
    "forum/recent/:category",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = NavigationDrawerRouter.Param(
            destination = NavigationDrawerRouter.Destination.FORUMS
        ).asNavPayload()
        return MainRouter.forActivity(env.context, payload)
    }
}

internal object ActivityRoute : Route(
    "activity",
    "activity/:id",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val id = params["id"]
        return if (id == null) {
            val payload = NavigationDrawerRouter.Param(
                destination = NavigationDrawerRouter.Destination.SOCIAL
            ).asNavPayload()
            MainRouter.forActivity(env.context, payload)
        } else {
            TODO("Feed details deep linking not supported yet")
            //FeedRouter.forActivity(env.context)
        }

    }
}

internal object ForumRoute : Route(
    "forum/thread/:id"
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        TODO("Forum detail deep linking not yet supported yet")
    }
}

internal object ReviewRoute : Route("reviews") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        return ReviewDiscoverRouter.forActivity(env.context)
    }
}

internal object RecommendationRoute : Route("recommendations") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        return RecommendationDiscoverRouter.forActivity(env.context)
    }
}

internal object CharacterRoute : Route(
    "character/:id",
    "character/:id/:name"
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = CharacterRouter.Param(
            id = params["id"]?.toLong(),
            name = params["name"]
        ).asNavPayload()
        return CharacterRouter.forActivity(env.context, payload)
    }
}

internal object StudioRoute : Route(
    "studio/:id",
    "studio/:id/:name"
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = StudioRouter.Param(
            id = requireNotNull(params["id"]?.toLong())
        ).asNavPayload()
        return StudioRouter.forActivity(env.context, payload)
    }
}

internal object StaffRoute : Route(
    "staff/:id",
    "staff/:id/:name",
    "actor/:id",
    "actor/:id/:name",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = StaffRouter.Param(
            id = params["id"]?.toLong(),
            name = params["name"]
        ).asNavPayload()
        return StaffRouter.forActivity(env.context, payload)
    }
}

internal object MediaRoute : Route(
    "anime/:id",
    "anime/:id/*",
    "manga/:id",
    "manga/:id/*"
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val mediaType = uri.pathSegments().first()
            .uppercase()

        val payload = MediaRouter.Param(
            id = requireNotNull(params["id"]).toLong(),
            type = MediaType.valueOf(mediaType)
        ).asNavPayload()
        return MediaRouter.forActivity(env.context, payload)
    }
}

internal object SearchRoute : Route(
    "search/anime",
    "search/manga"
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = SearchRouter.Param(
            query = uri.queryParameter("search"),
            genres = uri.queryParameter("genres")?.split(","),
            year = uri.queryParameter("year")?.toInt(),
            season = uri.queryParameter("season")?.let { MediaSeason.valueOf(it) },
            format = uri.queryParameter("format")?.let { MediaFormat.valueOf(it) },
            status = uri.queryParameter("airing status")?.let { MediaStatus.valueOf(it) },
        ).asNavPayload()
        return SearchRouter.forActivity(env.context, payload)
    }
}

internal object MediaListRoute : Route(
    "user/:id/animelist",
    "user/:id/mangalist",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val mediaType = when (uri.pathSegments().last()) {
            "animelist" -> MediaType.ANIME
            else -> MediaType.MANGA
        }
        val payload = when (identifier?.isDigitsOnly()) {
            true -> MediaListRouter.Param(
                userId = identifier.toLong(),
                type = mediaType
            ).asNavPayload()
            else -> MediaListRouter.Param(
                userName = identifier,
                type = mediaType
            ).asNavPayload()
        }
        return MediaListRouter.forActivity(env.context, payload)
    }
}

internal object UserRoute : Route(
    "user/:id",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload = when (identifier?.isDigitsOnly()) {
            true -> ProfileRouter.Param(
                userId = identifier.toLong()
            ).asNavPayload()
            else -> ProfileRouter.Param(
                userName = identifier
            ).asNavPayload()
        }
        return ProfileRouter.forActivity(env.context, payload)
    }
}

internal object UserStatsRoute : Route(
    "user/:id/stats/:media_type",
    "user/:id/stats/:media_type",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload = when (identifier?.isDigitsOnly()) {
            true -> ProfileRouter.Param(
                userId = identifier.toLong()
            ).asNavPayload()
            else -> ProfileRouter.Param(
                userName = identifier
            ).asNavPayload()
        }
        return ProfileRouter.forActivity(env.context, payload)
    }
}

internal object UserFavouritesRoute : Route(
    "user/:id/favourites",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload = when (identifier?.isDigitsOnly()) {
            true -> ProfileRouter.Param(
                userId = identifier.toLong()
            ).asNavPayload()
            else -> ProfileRouter.Param(
                userName = identifier
            ).asNavPayload()
        }
        return ProfileRouter.forActivity(env.context, payload)
    }
}

internal object UserReviewRoute : Route(
    "user/:id/reviews",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload = when (identifier?.isDigitsOnly()) {
            true -> ProfileRouter.Param(
                userId = identifier.toLong()
            ).asNavPayload()
            else -> ProfileRouter.Param(
                userName = identifier
            ).asNavPayload()
        }
        return ProfileRouter.forActivity(env.context, payload)
    }
}

object FallbackAction : Action<Intent?> {
    override fun run(uri: DeepLinkUri, params: Map<String, String>, env: Environment): Intent? {
        val entries = params.entries.joinToString()
        Timber.w("Registered routers failed to match | uri: $uri | params: $entries")
        return SplashRouter.forActivity(env.context)
    }
}