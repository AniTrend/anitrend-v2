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

package co.anitrend.deeplink.koin

import android.content.Intent
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.deeplink.environment.AniTrendEnvironment
import co.anitrend.deeplink.component.route.*
import co.anitrend.deeplink.component.screen.DeepLinkScreen
import com.hellofresh.deeplink.DeepLinkParser
import com.hellofresh.deeplink.Environment
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val coreModule = module {
    factory<Environment> {
        val settings = get<IAuthenticationSettings>()
        AniTrendEnvironment(
            context = androidContext(),
            isAuthenticated = settings.isAuthenticated.value,
            userId = settings.authenticatedUserId.value
        )
    }
}

private val routerModule = module {
    scope<DeepLinkScreen> {
        factory {
            DeepLinkParser.of<Intent?>(get())
                // AniList specific routes
                .addRoute(MainRoute)
                .addRoute(ActivityRoute)
                .addRoute(ForumRoute)
                .addRoute(ReviewRoute)
                .addRoute(NewsRoute)
                .addRoute(EpisodesRoute)
                .addRoute(ForumDiscoverRoute)
                .addRoute(RecommendationRoute)
                .addRoute(CharacterRoute)
                .addRoute(StudioRoute)
                .addRoute(StaffRoute)
                .addRoute(MediaRoute)
                .addRoute(SearchRoute)
                .addRoute(MediaListRoute)
                .addRoute(UserRoute)
                .addRoute(UserStatsRoute)
                .addRoute(UserFavouritesRoute)
                .addRoute(UserReviewRoute)
                // AniTrend specific routes
                .addRoute(DiscoverRoute)
                .addRoute(SocialRoute)
                .addRoute(SuggestionsRoute)
                .addRoute(SettingsRoute)
                .addRoute(ProfileRoute)
                .addRoute(UpdatesRoute)
                .addRoute(AboutRoute)
                .addFallbackAction(FallbackAction)
                .build()
        }
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(coreModule, routerModule)
)