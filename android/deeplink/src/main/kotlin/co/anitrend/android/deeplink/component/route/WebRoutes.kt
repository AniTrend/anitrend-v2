/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.android.deeplink.component.route

import android.content.Intent
import androidx.core.text.isDigitsOnly
import co.anitrend.android.core.environment.IAniTrendEnvironment
import co.anitrend.android.deeplink.component.route.contract.Route
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.CharacterRouter
import co.anitrend.navigation.FeedRouter
import co.anitrend.navigation.ForumRouter
import co.anitrend.navigation.MainRouter
import co.anitrend.navigation.MediaListRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.NavigationDrawerRouter
import co.anitrend.navigation.NotificationRouter
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.RecommendationDiscoverRouter
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.navigation.SearchRouter
import co.anitrend.navigation.StaffRouter
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.forActivity
import com.kingsleyadio.deeplink.Action
import com.kingsleyadio.deeplink.DeepLinkUri
import com.kingsleyadio.deeplink.Environment
import timber.log.Timber

internal object MainRoute : Route("home") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val payload =
            NavigationDrawerRouter
                .NavigationDrawerParam(
                    destination = NavigationDrawerRouter.Destination.HOME,
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val payload =
            NavigationDrawerRouter
                .NavigationDrawerParam(
                    destination = NavigationDrawerRouter.Destination.FORUMS,
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val id = params["id"]
        return if (id == null) {
            val payload =
                NavigationDrawerRouter
                    .NavigationDrawerParam(
                        destination = NavigationDrawerRouter.Destination.SOCIAL,
                    ).asNavPayload()
            MainRouter.forActivity(env.context, payload)
        } else {
            val payload =
                FeedRouter
                    .FeedParam(
                        id = id.toLongOrNull(),
                    ).asNavPayload()
            FeedRouter.forActivity(env.context, payload)
        }
    }
}

internal object ForumRoute : Route(
    "forum/thread/:id",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val payload =
            ForumRouter
                .ForumParam(
                    id = params["id"]?.toLongOrNull(),
                ).asNavPayload()
        return ForumRouter.forActivity(env.context, payload)
    }
}

internal object ReviewRoute : Route("reviews") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        return ReviewDiscoverRouter.forActivity(env.context)
    }
}

internal object RecommendationRoute : Route("recommendations") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        return RecommendationDiscoverRouter.forActivity(env.context)
    }
}

internal object CharacterRoute : Route(
    "character/:id",
    "character/:id/:name",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val payload =
            CharacterRouter
                .CharacterParam(
                    id = params["id"]?.toLongOrNull(),
                    name = params["name"],
                ).asNavPayload()
        return CharacterRouter.forActivity(env.context, payload)
    }
}

internal object StudioRoute : Route(
    "studio/:id",
    "studio/:id/:name",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val payload =
            StudioRouter
                .StudioParam(
                    id = params["id"]?.toLongOrNull(),
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val payload =
            StaffRouter
                .StaffParam(
                    id = params["id"]?.toLongOrNull(),
                    name = params["name"],
                ).asNavPayload()
        return StaffRouter.forActivity(env.context, payload)
    }
}

internal object MediaRoute : Route(
    "anime/:id",
    "anime/:id/*",
    "manga/:id",
    "manga/:id/*",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val mediaType =
            uri
                .pathSegments()
                .first()
                .uppercase()

        val payload =
            MediaRouter
                .MediaParam(
                    id = requireNotNull(params["id"]).toLong(),
                    type = MediaType.valueOf(mediaType),
                ).asNavPayload()
        return MediaRouter.forActivity(env.context, payload)
    }
}

internal object SearchRoute : Route(
    "search/anime",
    "search/manga",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val destination =
            when (uri.pathSegments().lastOrNull()) {
                "anime" -> SearchRouter.Destination.ANIME
                "manga" -> SearchRouter.Destination.MANGA
                else -> SearchRouter.Destination.HOME
            }
        val payload = uri.toSearchParam(destination = destination).asNavPayload()
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val lastSegment = uri.pathSegments().last { it.isNotBlank() }
        val mediaType =
            when (lastSegment) {
                "animelist" -> MediaType.ANIME
                "mangalist" -> MediaType.MANGA
                else -> throw NotImplementedError("Unable to match last segment: $lastSegment")
            }
        val scoreFormat = (env as IAniTrendEnvironment).settings.scoreFormat
        val payload =
            when (identifier?.isDigitsOnly()) {
                true ->
                    MediaListRouter
                        .MediaListParam(
                            userId = identifier.toLongOrNull(),
                            scoreFormat = scoreFormat.value,
                            type = mediaType,
                        ).asNavPayload()
                else ->
                    MediaListRouter
                        .MediaListParam(
                            userName = identifier,
                            scoreFormat = scoreFormat.value,
                            type = mediaType,
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload =
            when (identifier?.isDigitsOnly()) {
                true ->
                    ProfileRouter
                        .ProfileParam(
                            userId = identifier.toLongOrNull(),
                        ).asNavPayload()
                else ->
                    ProfileRouter
                        .ProfileParam(
                            userName = identifier,
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload =
            when (identifier?.isDigitsOnly()) {
                true ->
                    ProfileRouter
                        .ProfileParam(
                            userId = identifier.toLongOrNull(),
                        ).asNavPayload()
                else ->
                    ProfileRouter
                        .ProfileParam(
                            userName = identifier,
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload =
            when (identifier?.isDigitsOnly()) {
                true ->
                    ProfileRouter
                        .ProfileParam(
                            userId = identifier.toLongOrNull(),
                        ).asNavPayload()
                else ->
                    ProfileRouter
                        .ProfileParam(
                            userName = identifier,
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
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        val identifier = params["id"]
        val payload =
            when (identifier?.isDigitsOnly()) {
                true ->
                    ProfileRouter
                        .ProfileParam(
                            userId = identifier.toLongOrNull(),
                        ).asNavPayload()
                else ->
                    ProfileRouter
                        .ProfileParam(
                            userName = identifier,
                        ).asNavPayload()
            }
        return ProfileRouter.forActivity(env.context, payload)
    }
}

internal object NotificationRoute : Route(
    "notifications",
) {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        super.run(uri, params, env)
        return NotificationRouter.forActivity(env.context)
    }
}

private fun DeepLinkUri.toSearchParam(destination: SearchRouter.Destination): SearchRouter.SearchParam =
    SearchRouter.SearchParam(
        query = queryParameter("search") ?: queryParameter("query"),
        genres =
            queryParameter("genres")
                ?.split(",")
                ?.map(String::trim)
                ?.filter(String::isNotEmpty),
        year = queryParameter("year")?.toIntOrNull(),
        season = queryParameter("season").enumValueOrNull<MediaSeason>(),
        format = queryParameter("format").enumValueOrNull<MediaFormat>(),
        status =
            (
                queryParameter("status")
                    ?: queryParameter("airing status")
            ).enumValueOrNull<MediaStatus>(),
        destination = destination,
    )

private inline fun <reified T : Enum<T>> String?.enumValueOrNull(): T? =
    this
        ?.trim()
        ?.takeIf(String::isNotEmpty)
        ?.uppercase()
        ?.let { value -> runCatching { enumValueOf<T>(value) }.getOrNull() }

object FallbackAction : Action<Intent?> {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment,
    ): Intent? {
        val entries = params.entries.joinToString()
        Timber.w("Registered routers failed to match | uri: $uri | params: $entries")
        return MainRouter.forActivity(env.context)
    }
}
