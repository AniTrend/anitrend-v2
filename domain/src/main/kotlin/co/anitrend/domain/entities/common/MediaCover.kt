/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.domain.entities.common

import co.anitrend.domain.common.entity.IEntityMediaCover

/** [MediaCover](https://anilist.github.io/ApiV2-GraphQL-Docs/mediacoverimage.doc.html)
 * An image model for [co.anitrend.data.model.response.core.media.Media]
 *
 * @param color Average #hex color of cover image
 * @param extraLarge The cover image url of the media at its largest size.
 * If this size isn't available, [large] will be provided instead.
 * @param large The cover image at its large size
 * @param medium The cover image at medium size
 */
data class MediaCover(
    override val color: String?,
    override val extraLarge: String?,
    override val large: String?,
    override val medium: String?
) : IEntityMediaCover