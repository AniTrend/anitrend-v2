package co.anitrend.data.repository.medialist

import androidx.annotation.StringDef

/**
 * Media list watching/reading status.
 */
@StringDef(
    MediaListStatus.CURRENT,
    MediaListStatus.PLANNING,
    MediaListStatus.COMPLETED,
    MediaListStatus.DROPPED,
    MediaListStatus.PAUSED,
    MediaListStatus.REPEATING
)
annotation class MediaListStatus {
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