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
import android.net.Uri
import androidx.core.net.UriCompat
import co.anitrend.data.auth.helper.CALLBACK_QUERY_ERROR_DESCRIPTION_KEY
import co.anitrend.data.auth.helper.CALLBACK_QUERY_ERROR_KEY
import co.anitrend.data.auth.helper.CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY
import co.anitrend.data.auth.helper.CALLBACK_QUERY_TOKEN_KEY
import co.anitrend.data.auth.helper.CALLBACK_QUERY_TOKEN_TYPE_KEY
import co.anitrend.deeplink.component.route.contract.Route
import co.anitrend.deeplink.environment.contract.IAniTrendEnvironment
import co.anitrend.navigation.AboutRouter
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.MainRouter
import co.anitrend.navigation.NavigationDrawerRouter
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.SettingsRouter
import co.anitrend.navigation.UpdaterRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.forActivity
import com.kingsleyadio.deeplink.DeepLinkUri
import com.kingsleyadio.deeplink.Environment
import com.kingsleyadio.deeplink.extension.get
import com.kingsleyadio.deeplink.extension.toAndroidUri
import timber.log.Timber

internal object DiscoverRoute : Route("discover") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = NavigationDrawerRouter.Param(
            destination = NavigationDrawerRouter.Destination.DISCOVER
        ).asNavPayload()
        return MainRouter.forActivity(env.context, payload)
    }
}

internal object SocialRoute : Route("social") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = NavigationDrawerRouter.Param(
            destination = NavigationDrawerRouter.Destination.SOCIAL
        ).asNavPayload()
        return MainRouter.forActivity(env.context, payload)
    }
}

internal object SuggestionsRoute : Route("suggestions") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = NavigationDrawerRouter.Param(
            destination = NavigationDrawerRouter.Destination.SUGGESTIONS
        ).asNavPayload()
        return MainRouter.forActivity(env.context, payload)
    }
}

internal object SettingsRoute : Route("settings") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        return SettingsRouter.forActivity(env.context)
    }
}

internal object ProfileRoute : Route("profile") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        env as IAniTrendEnvironment
        val payload = ProfileRouter.Param(
            userId = env.userId
        ).asNavPayload()
        return ProfileRouter.forActivity(env.context, payload)
    }
}

internal object UpdatesRoute : Route("updates") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        return UpdaterRouter.forActivity(env.context)
    }
}

internal object AboutRoute : Route("about") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        return AboutRouter.forActivity(env.context)
    }
}

internal object NewsRoute : Route("news") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = NavigationDrawerRouter.Param(
            destination = NavigationDrawerRouter.Destination.NEWS
        ).asNavPayload()
        return MainRouter.forActivity(env.context, payload)
    }
}

internal object EpisodesRoute : Route("episodes") {
    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        val payload = NavigationDrawerRouter.Param(
            destination = NavigationDrawerRouter.Destination.EPISODES
        ).asNavPayload()
        return MainRouter.forActivity(env.context, payload)
    }
}

internal object OAuthRoute : Route(
    "oauth/v2/anilist",
    "oauth/v2/trakt"
) {

    private fun DeepLinkUri.getAuthRouterParam(): AuthRouter.Param {
        val fullyQualifiedUrl = UriCompat.toSafeString(toAndroidUri())
        return runCatching {
            AuthRouter.Param(
                accessToken = requireNotNull(queryParameter(CALLBACK_QUERY_TOKEN_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_KEY was not found in -> $fullyQualifiedUrl"
                },
                tokenType = requireNotNull(queryParameter(CALLBACK_QUERY_TOKEN_TYPE_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_TYPE_KEY was not found in -> $fullyQualifiedUrl"
                },
                expiresIn = requireNotNull(queryParameter(CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY was not found in -> $fullyQualifiedUrl"
                }.toLong()
            )
        }.getOrElse {
            Timber.w(it)
            AuthRouter.Param(
                errorTitle = queryParameter(CALLBACK_QUERY_ERROR_KEY),
                errorDescription = queryParameter(CALLBACK_QUERY_ERROR_DESCRIPTION_KEY)
            )
        }
    }

    private fun DeepLinkUri.normalize(): Uri {
        val normalizedUrl = toString().replaceFirst('#', '?')
        return Uri.parse(normalizedUrl)
    }

    override fun run(
        uri: DeepLinkUri,
        params: Map<String, String>,
        env: Environment
    ): Intent? {
        super.run(uri, params, env)
        // Only focusing on AL oath for now, we'll extend this later
        val deepLinkUri = DeepLinkUri.get(uri.normalize())
        val payload = deepLinkUri.getAuthRouterParam().asNavPayload()
        return AuthRouter.forActivity(
            env.context,
            payload
        )
    }
}
