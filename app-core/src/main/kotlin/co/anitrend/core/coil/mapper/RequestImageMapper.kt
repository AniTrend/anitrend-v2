/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.core.coil.mapper

import co.anitrend.core.android.controller.power.contract.IPowerController
import co.anitrend.core.android.controller.power.contract.PowerSaverState
import co.anitrend.core.android.helpers.image.model.RequestImage
import coil.map.Mapper
import coil.request.Options
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class RequestImageMapper(
    powerController: IPowerController,
) : Mapper<RequestImage<*>, HttpUrl> {
    private val powerSaverState = powerController.powerSaverState()

    internal fun getImageUrlUsing(requestImage: RequestImage<*>): String {
        return when (requestImage) {
            is RequestImage.Media ->
                when (requestImage.type) {
                    RequestImage.Media.ImageType.BANNER -> requestImage.image?.banner
                    RequestImage.Media.ImageType.POSTER -> {
                        when (powerSaverState) {
                            PowerSaverState.Disabled -> requestImage.image?.extraLarge
                            else -> requestImage.image?.large
                        }
                    }
                }
            is RequestImage.Cover ->
                when (powerSaverState) {
                    PowerSaverState.Disabled -> requestImage.image?.large
                    else -> requestImage.image?.medium
                }
        }.toString()
    }

    /**
     * Convert [data] into [V]. Return 'null' if this mapper cannot convert [data].
     *
     * @param data The data to convert.
     * @param options The options for this request.
     */
    override fun map(
        data: RequestImage<*>,
        options: Options,
    ): HttpUrl {
        val url = getImageUrlUsing(data)
        return url.toHttpUrl()
    }
}
