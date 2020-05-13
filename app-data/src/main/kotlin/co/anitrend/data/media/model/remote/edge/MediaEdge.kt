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

package co.anitrend.data.media.model.remote.edge

import co.anitrend.data.arch.common.entity.IEntityEdge
import co.anitrend.data.character.model.remote.Character
import co.anitrend.data.media.model.remote.Media
import co.anitrend.data.staff.model.remote.Staff
import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.domain.media.enums.MediaRelation

/** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
 * Media connection edge
 *
 * @property characterRole The characters role in the media
 * @property characters The characters in the media voiced by the parent actor
 * @property favouriteOrder The order the media should be displayed from the users favourites
 * @property isMainStudio If the studio is the main animation studio of the media (For Studio->MediaConnection field only)
 * @property relationType The type of relation to the parent model
 * @property staffRole The role of the staff member in the production of the media
 * @property voiceActors The voice actors of the character
 */
internal data class MediaEdge(
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