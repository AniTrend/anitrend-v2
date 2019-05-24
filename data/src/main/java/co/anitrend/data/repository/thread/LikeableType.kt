package co.anitrend.data.repository.thread

import androidx.annotation.StringDef

/**
 * Types that can be liked
 */
@StringDef(
    LikeableType.THREAD,
    LikeableType.THREAD_COMMENT,
    LikeableType.ACTIVITY,
    LikeableType.ACTIVITY_REPLY
)
annotation class LikeableType {
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