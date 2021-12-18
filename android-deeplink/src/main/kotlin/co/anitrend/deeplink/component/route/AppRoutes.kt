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
import co.anitrend.deeplink.environment.AniTrendEnvironment
import co.anitrend.navigation.*
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.forActivity
import com.hellofresh.deeplink.DeepLinkUri
import com.hellofresh.deeplink.Environment

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
        env as AniTrendEnvironment
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