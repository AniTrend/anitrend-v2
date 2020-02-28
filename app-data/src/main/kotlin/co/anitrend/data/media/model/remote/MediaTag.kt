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

package co.anitrend.data.media.model.remote

import co.anitrend.data.media.model.contract.IMediaTag

/** [MediaTag](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatag.doc.html)
 * A tag that describes a theme or element of the media
 */
data class MediaTag(
    override val id: Long,
    override val name: String,
    override val description: String?,
    override val category: String?,
    override val rank: Int?,
    override val isGeneralSpoiler: Boolean?,
    override val isMediaSpoiler: Boolean?,
    override val isAdult: Boolean?
) : IMediaTag