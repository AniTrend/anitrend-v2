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

package co.anitrend.data.model.mutation.medialist

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.model.response.meta.FuzzyDate
import co.anitrend.data.repository.medialist.attributes.MediaListStatus

/** [UpdateMediaList mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Update multiple media list entries to the same values
 *
 * @param status The watching/reading status
 * @param score The score of the media in the user's chosen scoring method
 * @param scoreRaw The score of the media in 100 point
 * @param progress The amount of episodes/chapters consumed by the user
 * @param progressVolumes The amount of volumes read by the user
 * @param repeat The amount of times the user has rewatched/read the media
 * @param priority Priority of planning
 * @param private If the entry should only be visible to authenticated user
 * @param notes Text notes
 * @param hiddenFromStatusLists If the entry shown be hidden from  non-custom lists
 * @param advancedScores Array of advanced scores
 * @param startedAt When the entry was started by the user
 * @param completedAt When the entry was completed by the user
 * @param ids The list entries ids to update
 */
data class UpdateMediaListEntries(
    val status: MediaListStatus,
    val score: Float? = null,
    val scoreRaw: Int? = null,
    val progress: Int? = null,
    val progressVolumes: Int? = null,
    val repeat: Int? = null,
    val priority: Int? = null,
    val private: Boolean = false,
    val notes: String? = null,
    val hiddenFromStatusLists: Boolean = false,
    val advancedScores: List<Float>? = null,
    val startedAt: FuzzyDate? = null,
    val completedAt: FuzzyDate? = null,
    val ids: List<Int>
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "status" to status,
        "score" to score,
        "scoreRaw" to scoreRaw,
        "progress" to progress,
        "progressVolumes" to progressVolumes,
        "repeat" to repeat,
        "priority" to priority,
        "private" to private,
        "notes" to notes,
        "hiddenFromStatusLists" to hiddenFromStatusLists,
        "advancedScores" to advancedScores,
        "startedAt" to startedAt?.toMap(),
        "completedAt" to completedAt?.toMap(),
        "ids" to ids
    )
}