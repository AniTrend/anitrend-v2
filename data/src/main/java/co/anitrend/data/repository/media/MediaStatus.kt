package co.anitrend.data.repository.media

import androidx.annotation.StringDef

/**
 * Media status values
 */
@StringDef(
    MediaStatus.FINISHED,
    MediaStatus.RELEASING,
    MediaStatus.NOT_YET_RELEASED,
    MediaStatus.CANCELLED
)
annotation class MediaStatus {
    companion object {
        /** Has completed and is no longer being released */
        const val FINISHED = "FINISHED"
        /** Currently releasing **/
        const val RELEASING = "RELEASING"
        /** To be released at a later date */
        const val NOT_YET_RELEASED = "NOT_YET_RELEASED"
        /** Ended before the work could be finished */
        const val CANCELLED = "CANCELLED"

        val ALL = listOf(
            FINISHED,
            RELEASING,
            NOT_YET_RELEASED,
            CANCELLED
        )
    }
}