package co.anitrend.data.repository.medialist

import androidx.annotation.StringDef

/**
 * MediaList Sort values
 */
@StringDef(
    MediaListSort.MEDIA_ID,
    MediaListSort.MEDIA_ID_DESC,
    MediaListSort.SCORE,
    MediaListSort.SCORE_DESC,
    MediaListSort.STATUS,
    MediaListSort.STATUS_DESC,
    MediaListSort.PROGRESS,
    MediaListSort.PROGRESS_DESC,
    MediaListSort.PROGRESS_VOLUMES,
    MediaListSort.PROGRESS_VOLUMES_DESC,
    MediaListSort.REPEAT,
    MediaListSort.REPEAT_DESC,
    MediaListSort.PRIORITY,
    MediaListSort.PRIORITY_DESC,
    MediaListSort.STARTED_ON,
    MediaListSort.STARTED_ON_DESC,
    MediaListSort.FINISHED_ON,
    MediaListSort.FINISHED_ON_DESC,
    MediaListSort.ADDED_TIME,
    MediaListSort.ADDED_TIME_DESC,
    MediaListSort.UPDATED_TIME,
    MediaListSort.UPDATED_TIME_DESC
)
annotation class MediaListSort {
    companion object {
        const val MEDIA_ID = "MEDIA_ID"
        const val MEDIA_ID_DESC = "MEDIA_ID_DESC"
        const val SCORE = "SCORE"
        const val SCORE_DESC = "SCORE_DESC"
        const val STATUS = "STATUS"
        const val STATUS_DESC = "STATUS_DESC"
        const val PROGRESS = "PROGRESS"
        const val PROGRESS_DESC = "PROGRESS_DESC"
        const val PROGRESS_VOLUMES = "PROGRESS_VOLUMES"
        const val PROGRESS_VOLUMES_DESC = "PROGRESS_VOLUMES_DESC"
        const val REPEAT = "REPEAT"
        const val REPEAT_DESC = "REPEAT_DESC"
        const val PRIORITY = "PRIORITY"
        const val PRIORITY_DESC = "PRIORITY_DESC"
        const val STARTED_ON = "STARTED_ON"
        const val STARTED_ON_DESC = "STARTED_ON_DESC"
        const val FINISHED_ON = "FINISHED_ON"
        const val FINISHED_ON_DESC = "FINISHED_ON_DESC"
        const val ADDED_TIME = "ADDED_TIME"
        const val ADDED_TIME_DESC = "ADDED_TIME_DESC"
        const val UPDATED_TIME = "UPDATED_TIME"
        const val UPDATED_TIME_DESC = "UPDATED_TIME_DESC"

        val ALL = listOf(
            MEDIA_ID,
            MEDIA_ID_DESC,
            SCORE,
            SCORE_DESC,
            STATUS,
            STATUS_DESC,
            PROGRESS,
            PROGRESS_DESC,
            PROGRESS_VOLUMES,
            PROGRESS_VOLUMES_DESC,
            REPEAT,
            REPEAT_DESC,
            PRIORITY,
            PRIORITY_DESC,
            STARTED_ON,
            STARTED_ON_DESC,
            FINISHED_ON,
            FINISHED_ON_DESC,
            ADDED_TIME,
            ADDED_TIME_DESC,
            UPDATED_TIME,
            UPDATED_TIME_DESC
        )
    }
}