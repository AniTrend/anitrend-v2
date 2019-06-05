package co.anitrend.data.model.query.medialist

import co.anitrend.data.model.contract.FuzzyDateInt
import co.anitrend.data.model.contract.FuzzyDateLike
import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.media.attributes.MediaType
import co.anitrend.data.repository.medialist.attributes.MediaListSort
import co.anitrend.data.repository.medialist.attributes.MediaListStatus

/** [MediaList query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
 *
 * @param id Filter by a list entry's id
 * @param userId Filter by a user's id
 * @param userName Filter by a user's name
 * @param type Filter by the list entries media type
 * @param status Filter by the watching/reading status
 * @param mediaId Filter by the media id of the list entry
 * @param isFollowing Filter list entries to users who are being followed by the authenticated user
 * @param notes Filter by note words and #tags
 * @param startedAt Filter by the date the user started the media
 * @param completedAt Filter by the date the user completed the media
 * @param userId_in Filter by a user's id
 * @param notes_like Filter by note words and #tags
 * @param startedAt_greater Filter by the date the user started the media
 * @param startedAt_lesser Filter by the date the user started the media
 * @param startedAt_like Filter by the date the user started the media
 * @param completedAt_greater Filter by the date the user completed the media
 * @param completedAt_lesser Filter by the date the user completed the media
 * @param completedAt_like Filter by the date the user completed the media
 * @param sort The order the results will be returned in
 */
data class MediaListQuery(
    val id: Int? = null,
    val userId: Int? = null,
    val userName: String? = null,
    val type: MediaType,
    val status: MediaListStatus? = null,
    val mediaId: Int? = null,
    val isFollowing: Boolean? = null,
    val notes: String? = null,
    val startedAt: FuzzyDateInt? = null,
    val completedAt: FuzzyDateInt? = null,
    val userId_in: List<Int>? = null,
    val notes_like: String? = null,
    val startedAt_greater: FuzzyDateInt? = null,
    val startedAt_lesser: FuzzyDateInt? = null,
    val startedAt_like: FuzzyDateLike? = null,
    val completedAt_greater: FuzzyDateInt? = null,
    val completedAt_lesser: FuzzyDateInt? = null,
    val completedAt_like: FuzzyDateLike? = null,
    val sort: List<MediaListSort>?
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "userId" to userId,
        "userName" to userName,
        "type" to type,
        "status" to status,
        "mediaId" to mediaId,
        "isFollowing" to isFollowing,
        "notes" to notes,
        "startedAt" to startedAt,
        "completedAt" to completedAt,
        "userId_in" to userId_in,
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