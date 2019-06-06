package co.anitrend.data.repository.medialist.attributes

import androidx.annotation.StringDef

/**
 * Media list watching/reading status.
 */
@StringDef(
    MediaListStatusContract.CURRENT,
    MediaListStatusContract.PLANNING,
    MediaListStatusContract.COMPLETED,
    MediaListStatusContract.DROPPED,
    MediaListStatusContract.PAUSED,
    MediaListStatusContract.REPEATING
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaListStatusContract {
    companion object {
        /** Currently watching/reading */
        const val CURRENT = "CURRENT"

        /** Planning to watch/read */
        const val PLANNING = "PLANNING"

        /** Finished watching/reading */
        const val COMPLETED = "COMPLETED"

        /** Stopped watching/reading before completing */
        const val DROPPED = "DROPPED"

        /** Paused watching/reading */
        const val PAUSED = "PAUSED"

        /** Paused watching/reading */
        const val REPEATING = "REPEATING"

        val ALL = listOf(
            CURRENT,
            PLANNING,
            COMPLETED,
            DROPPED,
            PAUSED,
            REPEATING
        )
    }
}

@MediaListStatusContract
typealias MediaListStatus = String