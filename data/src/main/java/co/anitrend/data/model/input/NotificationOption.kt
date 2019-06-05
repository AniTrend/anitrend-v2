package co.anitrend.data.model.input

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.notification.attributes.NotificationType

/** [NotificationOptionInput](https://anilist.github.io/ApiV2-GraphQL-Docs/notificationoptioninput.doc.html)
 *
 * Notification option input
 *
 * @param enabled Whether this type of notification is enabled
 * @param type The type of notification
 */
data class NotificationOption(
    val enabled: Boolean,
    val type: NotificationType
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "enabled" to enabled,
        "type" to type
    )
}