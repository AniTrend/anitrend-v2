/*
 * Copyright (C) 2026 AniTrend
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.component.compose.connection.MediaRecommendationsRoute
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.MediaRecommendationsRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.navigation.extensions.startActivity

class MediaRecommendationsScreen : AniTrendScreen() {
    private val param by extra<MediaRecommendationsRouter.MediaRecommendationsParam>(
        key = nameOf<MediaRecommendationsRouter.MediaRecommendationsParam>(),
    )

    private val settings by inject<IUserSettings>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recommendationsParam = requireNotNull(param)
        setContent {
            AniTrendTheme3 {
                val scoreFormat by settings.scoreFormat.flow.collectAsStateWithLifecycle(ScoreFormat.POINT_100)
                MediaRecommendationsRoute(
                    mediaId = recommendationsParam.mediaId,
                    mediaTitle = recommendationsParam.mediaTitle,
                    scoreFormat = scoreFormat,
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                    onMediaItemClick = { param ->
                        when (param) {
                            is MediaRouter.MediaParam ->
                                MediaRouter.startActivity(
                                    context = this@MediaRecommendationsScreen,
                                    navPayload = param.asNavPayload(),
                                )

                            else -> Unit
                        }
                    },
                )
            }
        }
    }
}
