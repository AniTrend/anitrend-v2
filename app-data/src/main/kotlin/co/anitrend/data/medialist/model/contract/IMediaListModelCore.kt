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

package co.anitrend.data.medialist.model.contract

import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.medialist.enums.MediaListStatus

/** [MediaList](https://anilist.github.io/ApiV2-GraphQL-Docs/medialist.doc.html)
 * MediaList contract for anime or manga
 *
 * @property advancedScores Map of advanced scores with name keys
 * @property completedAt When the entry was completed by the user
 * @property createdAt When the entry data was created
 * @property customLists Map of booleans for which custom lists the entry are in
 * @property hiddenFromStatusLists If the entry shown be hidden from non-custom lists
 * @property media The media for the list entry
 * @property mediaId The id of the media
 * @property notes Text notes
 * @property priority Priority of planning
 * @property private If the entry should only be visible to authenticated user
 * @property progress The amount of episodes/chapters consumed by the user
 * @property progressVolumes The amount of volumes read by the user
 * @property repeat The amount of times the user has rewatched/read the media
 * @property score The score of the entry
 * @property startedAt When the entry was started by the user
 * @property status The watching/reading status
 * @property updatedAt When the entry data was last updated
 * @property userId The id of the user owner of the list entry
 */
internal interface IMediaListModelCore : Identity {
    val advancedScores: Map<String, String>?
    val customLists: Map<String, String>?
    val completedAt: FuzzyDate?
    val createdAt: Long?
    val hiddenFromStatusLists: Boolean?
    val mediaId: Int
    val notes: String?
    val priority: Int?
    val private: Boolean?
    val progress: Int?
    val progressVolumes: Int?
    val repeat: Int?
    val score: Float?
    val startedAt: FuzzyDate?
    val status: MediaListStatus?
    val updatedAt: Long?
    val userId: Int
}