/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.medialist.model

import co.anitrend.domain.common.DateInt
import co.anitrend.domain.common.DateLike
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.sort.contract.ISortWithOrder
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListSort
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat

sealed class MediaListParam {

    /**
     * @param id Filter by media list id
     * @param userId Filter by user id
     */
    data class Entry(
        val id: Long,
        val userId: Long
    ) : MediaListParam()

    /**
     * @property userId Filter by a user's id
     * @property type Filter by the list entries media type
     * @property status Filter by the watching/reading status
     * @property notes Filter by note words and #tags
     * @property startedAt Filter by the date the user started the media
     * @property completedAt Filter by the date the user completed the media
     * @property notes_like Filter by note words and #tags
     * @property startedAt_greater Filter by the date the user started the media
     * @property startedAt_lesser Filter by the date the user started the media
     * @property startedAt_like Filter by the date the user started the media
     * @property completedAt_greater Filter by the date the user completed the media
     * @property completedAt_lesser Filter by the date the user completed the media
     * @property completedAt_like Filter by the date the user completed the media
     * @property sort The order the results will be returned in
     */
    abstract class Entries : MediaListParam() {
        abstract val scoreFormat: ScoreFormat
        abstract val type: MediaType
        abstract val userId: Long
        abstract val completedAt: DateInt?
        abstract val completedAt_greater: DateInt?
        abstract val completedAt_lesser: DateInt?
        abstract val completedAt_like: DateLike?
        abstract val notes: String?
        abstract val notes_like: String?
        abstract val sort: List<ISortWithOrder<MediaListSort>>?
        abstract val startedAt: DateInt?
        abstract val startedAt_greater: DateInt?
        abstract val startedAt_lesser: DateInt?
        abstract val startedAt_like: DateLike?
        abstract val status: MediaListStatus?
        abstract val status_in: List<MediaListStatus>?
        abstract val status_not: MediaListStatus?
        abstract val status_not_in: List<MediaListStatus>?
    }

    /** [MediaList query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
     *
     * @param id Filter by a list entry's id
     * @param mediaId Filter by the media id of the list entry
     * @param mediaId_in Filter by matching list of media ids
     * @param mediaId_not_in Filter by non-matching list of media ids
     * @param isFollowing Filter list entries to users who are being followed by the authenticated user
     * @param userId_in Filter by a user's id
     * @param compareWithAuthList Limit to only entries also on the auth user's list.
     * Requires [userId] or [userName] arguments.
     */
    data class Paged(
        val id: Long? = null,
        val mediaId: Long? = null,
        val mediaId_in: List<Int>? = null,
        val mediaId_not_in: List<Int>? = null,
        val isFollowing: Boolean? = null,
        val userId_in: List<Long>? = null,
        val compareWithAuthList: Boolean? = null,
        override val scoreFormat: ScoreFormat,
        override val type: MediaType,
        override val userId: Long,
        override val completedAt: DateInt? = null,
        override val completedAt_greater: DateInt? = null,
        override val completedAt_lesser: DateInt? = null,
        override val completedAt_like: DateLike? = null,
        override val notes: String? = null,
        override val notes_like: String? = null,
        override val sort: List<ISortWithOrder<MediaListSort>>? = null,
        override val startedAt: DateInt? = null,
        override val startedAt_greater: DateInt? = null,
        override val startedAt_lesser: DateInt? = null,
        override val startedAt_like: DateLike? = null,
        override val status: MediaListStatus? = null,
        override val status_in: List<MediaListStatus>? = null,
        override val status_not: MediaListStatus? = null,
        override val status_not_in: List<MediaListStatus>? = null,
    ) : Entries()

    /** [MediaListCollection query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
     *
     * Media list collection query, provides list pre-grouped by status & custom lists.
     * User ID and Media Type arguments required.
     *
     * @param chunk Which chunk of list entries to load
     * @param forceSingleCompletedList Always return completed list entries in one group,
     * overriding the user's split completed option.
     * @param perChunk The amount of entries per chunk, max 500
     */
    data class Collection(
        val chunk: Int? = null,
        val perChunk: Int? = null,
        val forceSingleCompletedList: Boolean? = null,
        override val scoreFormat: ScoreFormat,
        override val completedAt: DateInt? = null,
        override val completedAt_greater: DateInt? = null,
        override val completedAt_lesser: DateInt? = null,
        override val completedAt_like: DateLike? = null,
        override val notes: String? = null,
        override val notes_like: String? = null,
        override val sort: List<ISortWithOrder<MediaListSort>>? = null,
        override val startedAt: DateInt? = null,
        override val startedAt_greater: DateInt? = null,
        override val startedAt_lesser: DateInt? = null,
        override val startedAt_like: DateLike? = null,
        override val status: MediaListStatus? = null,
        override val status_in: List<MediaListStatus>? = null,
        override val status_not: MediaListStatus? = null,
        override val status_not_in: List<MediaListStatus>? = null,
        override val type: MediaType,
        override val userId: Long
    ) : Entries()

    /** [SaveMediaListEntry mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Create or update a media list entry
     *
     * @param id The list entry id, required for updating
     * @param mediaId The id of the media the entry is of
     * @param status The watching/reading status
     * @param score The score of the media in the user's chosen scoring method
     * @param scoreRaw The score of the media in 100 point
     * @param progress The amount of episodes/chapters consumed by the user
     * @param progressVolumes The amount of volumes read by the user
     * @param repeat The amount of times the user has rewatched/read the media
     * @param priority Priority of planning
     * @param private If the entry should only be visible to authenticated user
     * @param notes Text notes
     * @param hiddenFromStatusLists If the entry shown be hidden from non-custom lists
     * @param customLists Array of custom list names which should be enabled for this entry
     * @param advancedScores Array of advanced scores
     * @param startedAt When the entry was started by the user
     * @param completedAt When the entry was completed by the user
     */
    data class SaveEntry(
        val id: Long? = null,
        val mediaId: Long,
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
        val customLists: List<String>? = null,
        val advancedScores: List<Float>? = null,
        val startedAt: FuzzyDate? = null,
        val completedAt: FuzzyDate? = null
    ) : MediaListParam()

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
    data class SaveEntries(
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
        val ids: List<Long>
    ) : MediaListParam()

    /** [DeleteCustomList mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Delete a media custom list
     *
     * @param customList The name of the custom list to delete
     * @param type The media list type of the custom list
     */
    data class DeleteCustomList(
        val customList: String,
        val type: MediaType
    ) : MediaListParam()

    /** [DeleteMediaListEntry mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Delete a media list entry
     *
     * @param id The id of the media list entry to delete
     */
    data class DeleteEntry(
        val id: Long
    ) : MediaListParam()
}