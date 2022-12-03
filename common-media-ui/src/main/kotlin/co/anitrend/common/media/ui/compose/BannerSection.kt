/*
 * Copyright (C) 2022  AniTrend
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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import coil.compose.rememberImagePainter

@Composable
fun BannerSection(
    cover: IMediaCover,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Image(
        painter = rememberImagePainter(cover.banner),
        contentDescription = "Media banner image",
        contentScale = ContentScale.Crop,
        modifier = modifier.clickable {
            val banner = cover.banner ?: return@clickable
            val param = ImageViewerRouter.Param(banner)
            ImageViewerRouter.startActivity(
                context = context,
                navPayload = param.asNavPayload()
            )
        }
    )
}
