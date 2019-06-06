package co.anitrend.data.repository.thread.attributes

import androidx.annotation.StringDef

/**
 * Types that can be liked
 */
@StringDef(
    LikeableTypeContract.THREAD,
    LikeableTypeContract.THREAD_COMMENT,
    LikeableTypeContract.ACTIVITY,
    LikeableTypeContract.ACTIVITY_REPLY
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class LikeableTypeContract {
    companion object {
        const val THREAD = "THREAD"
        const val THREAD_COMMENT = "THREAD_COMMENT"
        const val ACTIVITY = "ACTIVITY"
        const val ACTIVITY_REPLY = "ACTIVITY_REPLY"

        val ALL = listOf(
            THREAD,
            THREAD_COMMENT,
            ACTIVITY,
            ACTIVITY_REPLY
        )
    }
}

@LikeableTypeContract
typealias LikeableType = String