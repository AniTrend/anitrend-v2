package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media trend sort values
 */
@StringDef(
    MediaTrendSortContract.ID,
    MediaTrendSortContract.MEDIA_ID,
    MediaTrendSortContract.DATE,
    MediaTrendSortContract.SCORE,
    MediaTrendSortContract.POPULARITY,
    MediaTrendSortContract.TRENDING,
    MediaTrendSortContract.EPISODE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaTrendSortContract {
    companion object {
        const val ID = "ID"
        const val MEDIA_ID = "MEDIA_ID"
        const val DATE = "DATE"
        const val SCORE = "SCORE"
        const val POPULARITY = "POPULARITY"
        const val TRENDING = "TRENDING"
        const val EPISODE = "EPISODE"

        val ALL = listOf(
            ID,
            MEDIA_ID,
            DATE,
            SCORE,
            POPULARITY,
            TRENDING,
            EPISODE
        )
    }
}

@MediaTrendSortContract
typealias MediaTrendSort = String