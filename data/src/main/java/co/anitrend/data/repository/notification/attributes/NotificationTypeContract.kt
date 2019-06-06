package co.anitrend.data.repository.notification.attributes

import androidx.annotation.StringDef

/**
 * Notification type values
 */
@StringDef(
    NotificationTypeContract.ACTIVITY_MESSAGE,
    NotificationTypeContract.ACTIVITY_REPLY,
    NotificationTypeContract.FOLLOWING,
    NotificationTypeContract.ACTIVITY_MENTION,
    NotificationTypeContract.THREAD_COMMENT_MENTION,
    NotificationTypeContract.THREAD_SUBSCRIBED,
    NotificationTypeContract.THREAD_COMMENT_REPLY,
    NotificationTypeContract.AIRING,
    NotificationTypeContract.ACTIVITY_LIKE,
    NotificationTypeContract.ACTIVITY_REPLY_LIKE,
    NotificationTypeContract.THREAD_LIKE,
    NotificationTypeContract.THREAD_COMMENT_LIKE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class NotificationTypeContract {
    companion object {
        /** A user has sent you message */
        const val ACTIVITY_MESSAGE = "ACTIVITY_MESSAGE"

        /** A user has replied to your activity */
        const val ACTIVITY_REPLY = "ACTIVITY_REPLY"

        /** A user has followed you */
        const val FOLLOWING = "FOLLOWING"

        /** A user has mentioned you in their activity */
        const val ACTIVITY_MENTION = "ACTIVITY_MENTION"

        /** A user has mentioned you in a forum comment */
        const val THREAD_COMMENT_MENTION = "THREAD_COMMENT_MENTION"

        /** A user has commented in one of your subscribed forum threads */
        const val THREAD_SUBSCRIBED = "THREAD_SUBSCRIBED"

        /** A user has replied to your forum comment */
        const val THREAD_COMMENT_REPLY = "THREAD_COMMENT_REPLY"

        /** An anime you are currently watching has aired */
        const val AIRING = "AIRING"

        /** A user has liked your activity */
        const val ACTIVITY_LIKE = "ACTIVITY_LIKE"

        /** A user has liked your activity reply */
        const val ACTIVITY_REPLY_LIKE = "ACTIVITY_REPLY_LIKE"

        /** A user has liked your forum thread */
        const val THREAD_LIKE = "THREAD_LIKE"

        /** A user has liked your forum comment */
        const val THREAD_COMMENT_LIKE = "THREAD_COMMENT_LIKE"
        
        val ALL = listOf(
            ACTIVITY_MESSAGE,
            ACTIVITY_REPLY,
            FOLLOWING,
            ACTIVITY_MENTION,
            THREAD_COMMENT_MENTION,
            THREAD_SUBSCRIBED,
            THREAD_COMMENT_REPLY,
            AIRING,
            ACTIVITY_LIKE,
            ACTIVITY_REPLY_LIKE,
            THREAD_LIKE,
            THREAD_COMMENT_LIKE
        )
    }
}

@NotificationTypeContract
typealias NotificationType = String