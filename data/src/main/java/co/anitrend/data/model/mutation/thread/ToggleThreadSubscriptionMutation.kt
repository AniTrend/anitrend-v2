package co.anitrend.data.model.mutation.thread

import co.anitrend.data.model.contract.IGraphQuery

/** [ToggleThreadSubscription mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Toggle the subscription of a forum thread
 *
 * @param threadId The id of the forum thread to un/subscribe
 * @param subscribe Whether to subscribe or unsubscribe from the forum thread
 */
data class ToggleThreadSubscriptionMutation(
    val threadId: Int,
    val subscribe: Boolean
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "threadId" to threadId,
        "subscribe" to subscribe
    )
}