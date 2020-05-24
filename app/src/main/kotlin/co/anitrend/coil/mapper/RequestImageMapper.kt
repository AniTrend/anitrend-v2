/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.coil.mapper

import co.anitrend.core.android.helpers.image.model.CoverRequestImage
import co.anitrend.core.android.helpers.image.model.MediaRequestImage
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.controller.PowerController
import co.anitrend.core.controller.SaveData
import coil.map.MeasuredMapper
import coil.size.Size
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class RequestImageMapper(
    private val powerController: PowerController
) : MeasuredMapper<RequestImage<*>, HttpUrl> {

    internal fun getImageUrlUsing(requestImage: RequestImage<*>): String? {
        val saveData = powerController.shouldSaveData()
        return when (requestImage) {
            is MediaRequestImage -> when (requestImage.type) {
                MediaRequestImage.ImageType.BANNER -> requestImage.image?.banner
                MediaRequestImage.ImageType.POSTER -> {
                    if (saveData == SaveData.Disabled)
                        requestImage.image?.extraLarge
                    else
                        requestImage.image?.large
                }
            }
            is CoverRequestImage ->
                if (saveData == SaveData.Disabled)
                    requestImage.image?.large
                else
                    requestImage.image?.medium
        }
    }

    /** Return true if this can convert [data]. */
    override fun handles(data: RequestImage<*>) = true

    /** Convert [data] into [HttpUrl]. */
    override fun map(data: RequestImage<*>, size: Size): HttpUrl {
        return getImageUrlUsing(data)?.toHttpUrl() ?: FALLBACK_URL
    }

    companion object {
        private val FALLBACK_URL =
            ("https://github.com/anitrend/anitrend-v2/raw/master/app/src/main/ic_launcher-web.png")
                .toHttpUrl()
    }
}