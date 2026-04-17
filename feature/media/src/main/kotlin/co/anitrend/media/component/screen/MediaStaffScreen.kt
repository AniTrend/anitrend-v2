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
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.media.component.compose.people.MediaPeopleRoute
import co.anitrend.navigation.MediaPeopleRouter
import co.anitrend.navigation.MediaStaffRouter
import co.anitrend.navigation.extensions.nameOf

class MediaStaffScreen : AniTrendScreen() {
    private val param by extra<MediaStaffRouter.MediaStaffParam>(
        key = nameOf<MediaStaffRouter.MediaStaffParam>(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val staffParam = requireNotNull(param)
        setContent {
            AniTrendTheme3 {
                MediaPeopleRoute(
                    mediaId = staffParam.mediaId,
                    mediaTitle = staffParam.mediaTitle,
                    selectedSection = MediaPeopleRouter.Section.STAFF,
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                    showSegmentedControl = false,
                )
            }
        }
    }
}
