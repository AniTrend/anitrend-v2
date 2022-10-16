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

package co.anitrend.media.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.anitrend.common.media.ui.compose.BannerSection
import co.anitrend.common.media.ui.compose.SummarySection
import co.anitrend.core.android.compose.backgroundColor
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.domain.media.entity.Media
import co.anitrend.media.component.viewmodel.state.MediaState

@Composable
fun MediaDetailComponent(mediaState: MediaState) {
    AniTrendTheme3 {
        val state = mediaState.model.observeAsState()
        val media: Media = state.value ?: return@AniTrendTheme3
        Column {
            BannerSection(
                cover = media.image,
                modifier = Modifier.height(180.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .absoluteOffset(y = (-16).dp)
                    .background(
                        color = LocalContext.current.backgroundColor(),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                SummarySection(
                    media = media,
                    modifier = Modifier
                        .absoluteOffset(y = (-16).dp)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                )
            }
            /*val selectedIndex = remember(media.id) { 0 }
            TabRow(
                selectedTabIndex = selectedIndex,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = LocalContext.current.backgroundColor(),
                contentColor = LocalContext.current.primaryColor(),
                indicator = { tabPositions -> },
                divider = {}
            ) {
                //Tab()
            }*/
        }
    }
}