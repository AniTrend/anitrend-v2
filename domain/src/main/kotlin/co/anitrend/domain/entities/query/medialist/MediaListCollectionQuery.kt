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

package co.anitrend.domain.entities.query.medialist

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.enums.media.MediaType
import co.anitrend.domain.enums.medialist.MediaListSort

/** [MediaListCollection query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
 *
 * Media list collection query, provides list pre-grouped by status & custom lists.
 *
 * @param userId Filter by a user's id
 * @param userName Filter by a user's name
 * @param type Filter by the list entries media type
 * @param notes Filter by note words and #tags
 * @param startedAt Filter by the date the user started the media
 * @param completedAt Filter by the date the user completed the media
 * @param forceSingleCompletedList Always return completed list entries in one group, overriding the user's split completed option.
 * @param notes_like Filter by note words and #tags
 * @param startedAt_greater Filter by the date the user started the media
 * @param startedAt_lesser Filter by the date the user started the media
 * @param startedAt_like Filter by the date the user started the media
 * @param completedAt_greater Filter by the date the user completed the media
 * @param completedAt_lesser Filter by the date the user completed the media
 * @param completedAt_like Filter by the date the user completed the media
 * @param sort The order the results will be returned in
 */
data class MediaListCollectionQuery(
    val userId: Long? = null,
    val userName: String? = null,
    val type: MediaType,
    val notes: String? = null,
    val startedAt: co.anitrend.domain.common.FuzzyDateInt? = null,
    val completedAt: co.anitrend.domain.common.FuzzyDateInt? = null,
    val forceSingleCompletedList: Boolean? = null,
    val notes_like: String? = null,
    val startedAt_greater: co.anitrend.domain.common.FuzzyDateInt? = null,
    val startedAt_lesser: co.anitrend.domain.common.FuzzyDateInt? = null,
    val startedAt_like: co.anitrend.domain.common.FuzzyDateLike? = null,
    val completedAt_greater: co.anitrend.domain.common.FuzzyDateInt? = null,
    val completedAt_lesser: co.anitrend.domain.common.FuzzyDateInt? = null,
    val completedAt_like: co.anitrend.domain.common.FuzzyDateLike? = null,
    val sort: List<MediaListSort>? = null
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "userId" to userId,
        "userName" to userName,
        "type" to type,
        "notes" to notes,
        "startedAt" to startedAt,
        "completedAt" to completedAt,
        "forceSingleCompletedList" to forceSingleCompletedList,
        "notes_like" to notes_like,
        "startedAt_greater" to startedAt_greater,
        "startedAt_lesser" to startedAt_lesser,
        "startedAt_like" to startedAt_like,
        "completedAt_greater" to completedAt_greater,
        "completedAt_lesser" to completedAt_lesser,
        "completedAt_like" to completedAt_like,
        "sort" to sort
    )
}