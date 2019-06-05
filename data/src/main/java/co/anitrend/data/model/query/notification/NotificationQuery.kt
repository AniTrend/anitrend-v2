package co.anitrend.data.model.query.notification

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.notification.attributes.NotificationType

/** [Notification query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param type Filter by the type of notifications
 * @param resetNotificationCount Reset the unread notification count to 0 on load
 */
data class NotificationQuery(
    val type: NotificationType? = null,
    val resetNotificationCount: Boolean = false
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "type" to type,
        "resetNotificationCount" to resetNotificationCount
    )
}