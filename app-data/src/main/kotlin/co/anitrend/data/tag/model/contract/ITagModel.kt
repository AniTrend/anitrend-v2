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

package co.anitrend.data.tag.model.contract

import co.anitrend.data.core.common.Identity

/** [MediaTag](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatag.doc.html)
 * A tag that describes a theme or element of the media
 *
 * @property name The name of the tag
 * @property description A general description of the tag
 * @property category The categories of tags this tag belongs to
 * @property isGeneralSpoiler If the tag could be a spoiler for any media
 * @property isAdult If the tag is only for adult 18+ media
 * @property id The id of the tag
 */
internal interface ITagModel : Identity {
    val name: String
    val description: String?
    val category: String?
    val isGeneralSpoiler: Boolean?
    val isAdult: Boolean?
}