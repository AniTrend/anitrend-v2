/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.common.media.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.domain.media.entity.Media

@Composable
fun MediaAiringSchedule(
    category: Media.Category,
    color: Color,
) {
    Text(
        text = stringResource(R.string.label_episode_airing_in_time, 10, "2 days from now"),
        color = color,
        style = AniTrendTheme.typography.caption,
    )
}

@AniTrendPreview.Mobile
@Composable
private fun MediaAiringSchedulePreview() {
    AniTrendTheme3 {
        MediaAiringSchedule(
            category =
                Media.Category.Anime(
                    episodes = 12,
                    duration = 24,
                    broadcast = null,
                    premiered = null,
                    schedule = null,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
