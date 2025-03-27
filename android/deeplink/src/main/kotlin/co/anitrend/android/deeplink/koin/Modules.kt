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
package co.anitrend.android.deeplink.koin

import android.content.Intent
import co.anitrend.android.core.environment.IAniTrendEnvironment
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.android.deeplink.component.presenter.OnBoardingPresenter
import co.anitrend.android.deeplink.component.presenter.SplashPresenter
import co.anitrend.android.deeplink.component.route.AboutRoute
import co.anitrend.android.deeplink.component.route.ActivityRoute
import co.anitrend.android.deeplink.component.route.AiringRoute
import co.anitrend.android.deeplink.component.route.CharacterRoute
import co.anitrend.android.deeplink.component.route.DiscoverRoute
import co.anitrend.android.deeplink.component.route.EpisodesRoute
import co.anitrend.android.deeplink.component.route.FallbackAction
import co.anitrend.android.deeplink.component.route.ForumDiscoverRoute
import co.anitrend.android.deeplink.component.route.ForumRoute
import co.anitrend.android.deeplink.component.route.MainRoute
import co.anitrend.android.deeplink.component.route.MediaListRoute
import co.anitrend.android.deeplink.component.route.MediaRoute
import co.anitrend.android.deeplink.component.route.NewsRoute
import co.anitrend.android.deeplink.component.route.NotificationRoute
import co.anitrend.android.deeplink.component.route.OAuthRoute
import co.anitrend.android.deeplink.component.route.ProfileRoute
import co.anitrend.android.deeplink.component.route.RecommendationRoute
import co.anitrend.android.deeplink.component.route.ReviewRoute
import co.anitrend.android.deeplink.component.route.SearchPageRoute
import co.anitrend.android.deeplink.component.route.SearchRoute
import co.anitrend.android.deeplink.component.route.SettingsRoute
import co.anitrend.android.deeplink.component.route.SocialRoute
import co.anitrend.android.deeplink.component.route.StaffRoute
import co.anitrend.android.deeplink.component.route.StudioRoute
import co.anitrend.android.deeplink.component.route.SuggestionsRoute
import co.anitrend.android.deeplink.component.route.UpdatesRoute
import co.anitrend.android.deeplink.component.route.UserFavouritesRoute
import co.anitrend.android.deeplink.component.route.UserReviewRoute
import co.anitrend.android.deeplink.component.route.UserRoute
import co.anitrend.android.deeplink.component.route.UserStatsRoute
import co.anitrend.android.deeplink.component.screen.DeepLinkScreen
import co.anitrend.android.deeplink.component.viewmodel.DeepLinkViewModel
import co.anitrend.android.deeplink.environment.AniTrendEnvironment
import co.anitrend.android.deeplink.provider.FeatureProvider
import co.anitrend.navigation.DeepLinkRouter
import com.kingsleyadio.deeplink.DeepLinkParser
import com.kingsleyadio.deeplink.Environment
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

private val presenterModule =
    module {
        scope<DeepLinkScreen> {
            scoped {
                SplashPresenter(
                    context = androidContext(),
                    settings = get(),
                )
            }
        }
        scope<DeepLinkScreen> {
            scoped {
                OnBoardingPresenter(
                    context = androidContext(),
                    settings = get(),
                )
            }
        }
    }

private val coreModule =
    module {
        factory<Environment> {
            val settings = get<IUserSettings>()
            AniTrendEnvironment(
                context = androidApplication(),
                isAuthenticated = settings.isAuthenticated.value,
                settings = settings,
            )
        } binds (arrayOf(AniTrendEnvironment::class, IAniTrendEnvironment::class))
    }

private val viewModelModule =
    module {
        viewModelOf(::DeepLinkViewModel)
    }

private val routerModule =
    module {
        factory {
            DeepLinkParser
                .of<Intent?>(get())
                // AniList specific routes
                .addRoute(MainRoute)
                .addRoute(ActivityRoute)
                .addRoute(ForumRoute)
                .addRoute(ReviewRoute)
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
                .addRoute(NotificationRoute)
                // AniTrend specific routes
                .addRoute(DiscoverRoute)
                .addRoute(SocialRoute)
                .addRoute(SuggestionsRoute)
                .addRoute(SettingsRoute)
                .addRoute(ProfileRoute)
                .addRoute(UpdatesRoute)
                .addRoute(AboutRoute)
                .addRoute(OAuthRoute)
                .addRoute(NewsRoute)
                .addRoute(AiringRoute)
                .addRoute(SearchPageRoute)
                .addFallbackAction(FallbackAction)
                .build()
        }
    }

private val featureModule =
    module {
        factory<DeepLinkRouter.Provider> {
            FeatureProvider(
                deepLinkParser = get(),
            )
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(presenterModule, coreModule, viewModelModule, routerModule, featureModule),
    )
