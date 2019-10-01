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

package co.anitrend.data.model.core.medialist

import androidx.room.PrimaryKey
import co.anitrend.data.model.core.media.Media
import co.anitrend.domain.medialist.entities.IMediaList
import co.anitrend.domain.common.model.FuzzyDate
import co.anitrend.domain.medialist.enums.MediaListStatus

/** [MediaList](https://anilist.github.io/ApiV2-GraphQL-Docs/medialist.doc.html)
 * List of anime or manga
 *
 */
data class MediaList(
    @PrimaryKey
    override val id: Long,
    override val advancedScores: Map<String, String>?,
    override val completedAt: FuzzyDate?,
    override val createdAt: Long?,
    override val customLists: Map<String, String>?,
    override val hiddenFromStatusLists: Boolean?,
    override val media: Media?,
    override val mediaId: Int,
    override val notes: String?,
    override val priority: Int?,
    override val private: Boolean?,
    override val progress: Int?,
    override val progressVolumes: Int?,
    override val repeat: Int?,
    override val score: Float?,
    override val startedAt: FuzzyDate?,
    override val status: MediaListStatus?,
    override val updatedAt: Long?,
    override val userId: Int
) : IMediaList