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
import androidx.core.net.toUri
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.extensions.startViewIntent
import co.anitrend.media.component.compose.studios.MediaStudiosRoute
import co.anitrend.navigation.MediaStudiosRouter
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.navigation.extensions.startActivity

class MediaStudiosScreen : AniTrendScreen() {
    private val param by extra<MediaStudiosRouter.MediaStudiosParam>(
        key = nameOf<MediaStudiosRouter.MediaStudiosParam>(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val studiosParam = requireNotNull(param)
        setContent {
            AniTrendTheme3 {
                MediaStudiosRoute(
                    mediaId = studiosParam.mediaId,
                    mediaTitle = studiosParam.mediaTitle,
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                    onStudioClick = { studioParam ->
                        StudioRouter.startActivity(
                            context = this@MediaStudiosScreen,
                            navPayload = studioParam.asNavPayload(),
                        )
                    },
                    onExternalLinkClick = { url ->
                        startViewIntent(url.toUri())
                    },
                )
            }
        }
    }
}
