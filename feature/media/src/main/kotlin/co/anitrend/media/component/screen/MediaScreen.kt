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
package co.anitrend.media.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.anitrend.android.core.compose.design.ContentWrapper
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.arch.extension.ext.extra
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.common.shared.ui.extension.shareContent
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.extensions.runIfAuthenticated
import co.anitrend.core.extensions.startViewIntent
import co.anitrend.core.ui.inject
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.component.compose.MediaScreenContent
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaPeopleRouter
import co.anitrend.navigation.MediaRecommendationsRouter
import co.anitrend.navigation.MediaRelationsRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.navigation.extensions.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaScreen : AniTrendScreen() {
    private val viewModel by viewModel<MediaViewModel>()

    private val mediaRouterParam by extra<MediaRouter.MediaParam>(
        key = nameOf<MediaRouter.MediaParam>(),
    )

    private val settings by inject<IUserSettings>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper(
                    stateFlow = viewModel.loadState,
                    param = mediaRouterParam,
                    onLoad = viewModel::invoke,
                    onClick = viewModel::retry,
                ) {
                    val scoreFormat by settings.scoreFormat.flow.collectAsStateWithLifecycle(ScoreFormat.POINT_100)
                    MediaScreenContent(
                        mediaState = viewModelState(),
                        scoreFormat = scoreFormat,
                        onMyAnimeListButtonClick = { url ->
                            startViewIntent(url.toUri())
                        },
                        onBookmarkButtonClick = { view, media ->
                            view.openMediaListSheetFor(media, settings)
                        },
                        onFavouriteButtonClick = { view, params ->
                            view.runIfAuthenticated(settings) {
                                FavouriteTaskRouter
                                    .forWorker()
                                    .createOneTimeUniqueWorker(
                                        context = view.context,
                                        params = params,
                                    ).enqueue()
                            }
                        },
                        onFloatingActionButtonClick = { media ->
                            shareContent {
                                setType("text/plain")
                                setText(media.siteUrl.aniList)
                                setSubject(media.title.userPreferred?.toString())
                            }
                        },
                        onMediaDiscoverableItemClick = { param ->
                            MediaDiscoverRouter.startActivity(
                                context = this@MediaScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onImageClick = { param ->
                            ImageViewerRouter.startActivity(
                                context = this@MediaScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onMediaConnectionItemClick = { param ->
                            when (param) {
                                is MediaRouter.MediaParam ->
                                    MediaRouter.startActivity(
                                        context = this@MediaScreen,
                                        navPayload = param.asNavPayload(),
                                    )

                                is MediaListEditorRouter.MediaListEditorParam ->
                                    window.decorView.openMediaListSheetFor(param, settings)

                                else -> Unit
                            }
                        },
                        onPeopleClick = { param ->
                            MediaPeopleRouter.startActivity(
                                context = this@MediaScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onStudioClick = { param ->
                            StudioRouter.startActivity(
                                context = this@MediaScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onRelatedClick = { param ->
                            MediaRelationsRouter.startActivity(
                                context = this@MediaScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onRecommendationsClick = { param ->
                            MediaRecommendationsRouter.startActivity(
                                context = this@MediaScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onCommunityClick = { param ->
                            ReviewDiscoverRouter.startActivity(
                                context = this@MediaScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onExternalLinkClick = { url ->
                            startViewIntent(url.toUri())
                        },
                        onBackClick = onBackPressedDispatcher::onBackPressed,
                    )
                }
            }
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel
}
