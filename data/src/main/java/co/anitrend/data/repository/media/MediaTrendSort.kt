package co.anitrend.data.repository.media

import androidx.annotation.StringDef

/**
 * Media trend sort values
 */
@StringDef(
    MediaTrendSort.ID,
    MediaTrendSort.ID_DESC,
    MediaTrendSort.MEDIA_ID,
    MediaTrendSort.MEDIA_ID_DESC,
    MediaTrendSort.DATE,
    MediaTrendSort.DATE_DESC,
    MediaTrendSort.SCORE,
    MediaTrendSort.SCORE_DESC,
    MediaTrendSort.POPULARITY,
    MediaTrendSort.POPULARITY_DESC,
    MediaTrendSort.TRENDING,
    MediaTrendSort.TRENDING_DESC,
    MediaTrendSort.EPISODE,
    MediaTrendSort.EPISODE_DESC
)
annotation class MediaTrendSort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val MEDIA_ID = "MEDIA_ID"
        const val MEDIA_ID_DESC = "MEDIA_ID_DESC"
        const val DATE = "DATE"
        const val DATE_DESC = "DATE_DESC"
        const val SCORE = "SCORE"
        const val SCORE_DESC = "SCORE_DESC"
        const val POPULARITY = "POPULARITY"
        const val POPULARITY_DESC = "POPULARITY_DESC"
        const val TRENDING = "TRENDING"
        const val TRENDING_DESC = "TRENDING_DESC"
        const val EPISODE = "EPISODE"
        const val EPISODE_DESC = "EPISODE_DESC"

        val ALL = listOf(
            ID,
            ID_DESC,
            MEDIA_ID,
            MEDIA_ID_DESC,
            DATE,
            DATE_DESC,
            SCORE,
            SCORE_DESC,
            POPULARITY,
            POPULARITY_DESC,
            TRENDING,
            TRENDING_DESC,
            EPISODE,
            EPISODE_DESC
        )
    }
}