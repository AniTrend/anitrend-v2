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

package co.anitrend.data.medialist.model.query

import co.anitrend.domain.common.FuzzyDateInt
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListSort
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.android.parcel.Parcelize

/** [MediaListCollection query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
 *
 * Media list collection query, provides list pre-grouped by status & custom lists.
 * User ID and Media Type arguments required.
 *
 * @param chunk Which chunk of list entries to load
 * @param completedAt Filter by the date the user completed the media
 * @param completedAt_greater Filter by the date the user completed the media
 * @param completedAt_lesser Filter by the date the user completed the media
 * @param completedAt_like Filter by the date the user completed the media
 * @param forceSingleCompletedList Always return completed list entries in one group,
 * overriding the user's split completed option.
 * @param notes Filter by note words and #tags
 * @param notes_like Filter by note words and #tags
 * @param perChunk The amount of entries per chunk, max 500
 * @param sort The order the results will be returned in
 * @param startedAt Filter by the date the user started the media
 * @param startedAt_greater Filter by the date the user started the media
 * @param startedAt_lesser Filter by the date the user started the media
 * @param startedAt_like Filter by the date the user started the media
 * @param status Filter by the watching/reading status
 * @param status_in Filter by the watching/reading status
 * @param status_not Filter by the watching/reading status
 * @param status_not_in Filter by the watching/reading status
 * @param type Filter by the list entries media type
 * @param userId Filter by a user's id
 * @param userName Filter by a user's name
 */
@Parcelize
data class MediaListCollectionQuery(
    var chunk: Int? = null,
    var completedAt: FuzzyDateInt? = null,
    var completedAt_greater: FuzzyDateInt? = null,
    var completedAt_lesser: FuzzyDateInt? = null,
    var completedAt_like: String? = null,
    var forceSingleCompletedList: Boolean? = null,
    var notes: String? = null,
    var notes_like: String? = null,
    var perChunk: Int? = null,
    var sort: List<MediaListSort>? = null,
    var startedAt: FuzzyDateInt? = null,
    var startedAt_greater: FuzzyDateInt? = null,
    var startedAt_lesser: FuzzyDateInt? = null,
    var startedAt_like: String? = null,
    var status: MediaListStatus? = null,
    var status_in: List<MediaListStatus>? = null,
    var status_not: MediaListStatus? = null,
    var status_not_in: List<MediaListStatus>? = null,
    var type: MediaType,
    var userId: Long,
    var userName: String? = null
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "chunk" to chunk,
        "completedAt" to completedAt,
        "completedAt_greater" to completedAt_greater,
        "completedAt_lesser" to completedAt_lesser,
        "completedAt_like" to completedAt_like,
        "forceSingleCompletedList" to forceSingleCompletedList,
        "notes" to notes,
        "notes_like" to notes_like,
        "perChunk" to perChunk,
        "sort" to sort,
        "startedAt" to startedAt,
        "startedAt_greater" to startedAt_greater,
        "startedAt_lesser" to startedAt_lesser,
        "startedAt_like" to startedAt_like,
        "status" to status,
        "status_in" to status_in,
        "status_not" to status_not,
        "status_not_in" to status_not_in,
        "type" to type,
        "userId" to userId,
        "userName" to userName
    )
}