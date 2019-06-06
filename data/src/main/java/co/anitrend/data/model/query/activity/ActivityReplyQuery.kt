package co.anitrend.data.model.query.activity

import co.anitrend.data.model.contract.IGraphQuery

/** [ActivityReply query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the reply id
 * @param activityId Filter by the parent id
 */
data class ActivityReplyQuery(
    val id: Int? = null,
    val activityId: Int? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "activityId" to activityId
    )
}