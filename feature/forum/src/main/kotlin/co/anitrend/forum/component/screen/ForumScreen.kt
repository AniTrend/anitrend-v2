/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.forum.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.shared.ui.compose.FragmentItemHost
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.navigation.ForumRouter

class ForumScreen : AniTrendScreen() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                FragmentItemHost(
                    fragmentItem =
                        FragmentItem(
                            fragment = ForumRouter.forFragment(),
                            parameter = intent.extras,
                        ),
                )
            }
        }
    }
}
