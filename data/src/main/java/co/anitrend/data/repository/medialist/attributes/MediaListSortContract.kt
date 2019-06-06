package co.anitrend.data.repository.medialist.attributes

import androidx.annotation.StringDef

/**
 * MediaList Sort values
 */
@StringDef(
    MediaListSortContract.MEDIA_ID,
    MediaListSortContract.SCORE,
    MediaListSortContract.STATUS,
    MediaListSortContract.PROGRESS,
    MediaListSortContract.PROGRESS_VOLUMES,
    MediaListSortContract.REPEAT,
    MediaListSortContract.PRIORITY,
    MediaListSortContract.STARTED_ON,
    MediaListSortContract.FINISHED_ON,
    MediaListSortContract.ADDED_TIME,
    MediaListSortContract.UPDATED_TIME
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaListSortContract {
    companion object {
        const val MEDIA_ID = "MEDIA_ID"
        const val SCORE = "SCORE"
        const val STATUS = "STATUS"
        const val PROGRESS = "PROGRESS"
        const val PROGRESS_VOLUMES = "PROGRESS_VOLUMES"
        const val REPEAT = "REPEAT"
        const val PRIORITY = "PRIORITY"
        const val STARTED_ON = "STARTED_ON"
        const val FINISHED_ON = "FINISHED_ON"
        const val ADDED_TIME = "ADDED_TIME"
        const val UPDATED_TIME = "UPDATED_TIME"

        val ALL = listOf(
            MEDIA_ID,
            SCORE,
            STATUS,
            PROGRESS,
            PROGRESS_VOLUMES,
            REPEAT,
            PRIORITY,
            STARTED_ON,
            FINISHED_ON,
            ADDED_TIME,
            UPDATED_TIME
        )
    }
}

@MediaListSortContract
typealias MediaListSort = String