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

package co.anitrend.domain.media.entity.attribute.image

import co.anitrend.domain.common.entity.contract.IMediaCover

data class MediaImage(
    override val color: String?,
    override val extraLarge: String?,
    override val large: String?,
    override val medium: String?,
    override val banner: String?
) : IMediaCover {
    companion object {
        fun empty() = MediaImage(
            null,
            null,
            null,
            null,
            null
        )
    }
}