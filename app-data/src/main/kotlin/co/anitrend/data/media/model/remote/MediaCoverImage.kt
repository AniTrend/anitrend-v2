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

package co.anitrend.data.media.model.remote

/** [MediaCover](https://anilist.github.io/ApiV2-GraphQL-Docs/mediacoverimage.doc.html)
 *
 * @param color Average #hex color of cover image
 * @param extraLarge The cover image url of the media at its largest size.
 * If this size isn't available, [large] will be provided instead.
 * @param large The cover image at its large size
 * @param medium The cover image at medium size
 */
data class MediaCoverImage(
    val color: String?,
    val extraLarge: String?,
    val large: String?,
    val medium: String?
)