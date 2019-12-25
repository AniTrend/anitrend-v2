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

package co.anitrend.domain.media.entities

import co.anitrend.domain.common.entity.IEntity

/** [MediaTag](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatag.doc.html)
 * Media tag contract
 *
 * @property name The name of the tag
 * @property description A general description of the tag
 * @property category The categories of tags this tag belongs to
 * @property rank The relevance ranking of the tag out of the 100 for this media
 * @property isGeneralSpoiler If the tag could be a spoiler for any media
 * @property isMediaSpoiler If the tag is a spoiler for this media
 * @property isAdult If the tag is only for adult 18+ media
 */
interface IMediaTag : IEntity {
    val name: String
    val description: String?
    val category: String?
    val rank: Int?
    val isGeneralSpoiler: Boolean?
    val isMediaSpoiler: Boolean?
    val isAdult: Boolean?
}