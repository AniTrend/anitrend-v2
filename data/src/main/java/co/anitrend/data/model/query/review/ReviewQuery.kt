package co.anitrend.data.model.query.review

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.media.attributes.MediaType
import co.anitrend.data.repository.review.attributes.ReviewSort

/** [Review query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by Review id
 * @param mediaId Filter by media id
 * @param userId Filter by media id
 * @param mediaType Filter by media type
 * @param sort The order the results will be returned in
 */
data class ReviewQuery(
    val id: Int? = null,
    val mediaId: Int? = null,
    val userId: Int? = null,
    val mediaType: MediaType? = null,
    val sort: List<MediaType>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "mediaId" to mediaId,
        "userId" to userId,
        "mediaType" to mediaType,
        "sort" to sort
    )
}