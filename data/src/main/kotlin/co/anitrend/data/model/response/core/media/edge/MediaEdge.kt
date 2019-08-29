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

package co.anitrend.data.model.response.core.media.edge

import co.anitrend.data.entity.contract.IEntityEdge
import co.anitrend.data.model.response.core.character.Character
import co.anitrend.data.model.response.core.media.Media
import co.anitrend.data.model.response.core.staff.Staff
import co.anitrend.data.usecase.character.attributes.CharacterRole
import co.anitrend.data.usecase.media.attributes.MediaRelation

/** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
 *
 *
 */
data class MediaEdge(
    val characterRole: CharacterRole?,
    val characters: List<Character>?,
    val favouriteOrder: Int?,
    val isMainStudio: Boolean,
    val relationType: MediaRelation?,
    val staffRole: String?,
    val voiceActors: List<Staff>?,
    override val id: Long,
    override val node: Media?
) : IEntityEdge<Media>