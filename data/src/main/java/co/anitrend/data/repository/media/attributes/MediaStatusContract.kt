package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media status values
 */
@StringDef(
    MediaStatusContract.FINISHED,
    MediaStatusContract.RELEASING,
    MediaStatusContract.NOT_YET_RELEASED,
    MediaStatusContract.CANCELLED
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaStatusContract {
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

@MediaStatusContract
typealias MediaStatus = String