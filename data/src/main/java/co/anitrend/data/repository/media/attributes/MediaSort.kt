package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media Sort values
 */
@StringDef(
    MediaSort.ID,
    MediaSort.ID_DESC,
    MediaSort.TITLE_ROMAJI,
    MediaSort.TITLE_ROMAJI_DESC,
    MediaSort.TITLE_ENGLISH,
    MediaSort.TITLE_ENGLISH_DESC,
    MediaSort.TITLE_NATIVE,
    MediaSort.TITLE_NATIVE_DESC,
    MediaSort.TYPE,
    MediaSort.TYPE_DESC,
    MediaSort.FORMAT,
    MediaSort.FORMAT_DESC,
    MediaSort.START_DATE,
    MediaSort.START_DATE_DESC,
    MediaSort.END_DATE,
    MediaSort.END_DATE_DESC,
    MediaSort.SCORE,
    MediaSort.SCORE_DESC,
    MediaSort.POPULARITY,
    MediaSort.POPULARITY_DESC,
    MediaSort.TRENDING,
    MediaSort.TRENDING_DESC,
    MediaSort.EPISODES,
    MediaSort.EPISODES_DESC,
    MediaSort.DURATION,
    MediaSort.DURATION_DESC,
    MediaSort.STATUS,
    MediaSort.STATUS_DESC,
    MediaSort.CHAPTERS,
    MediaSort.CHAPTERS_DESC,
    MediaSort.VOLUMES,
    MediaSort.VOLUMES_DESC,
    MediaSort.UPDATED_AT,
    MediaSort.UPDATED_AT_DESC,
    MediaSort.SEARCH_MATCH
)
annotation class MediaSort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val TITLE_ROMAJI = "TITLE_ROMAJI"
        const val TITLE_ROMAJI_DESC = "TITLE_ROMAJI_DESC"
        const val TITLE_ENGLISH = "TITLE_ENGLISH"
        const val TITLE_ENGLISH_DESC = "TITLE_ENGLISH_DESC"
        const val TITLE_NATIVE = "TITLE_NATIVE"
        const val TITLE_NATIVE_DESC = "TITLE_NATIVE_DESC"
        const val TYPE = "TYPE"
        const val TYPE_DESC = "TYPE_DESC"
        const val FORMAT = "FORMAT"
        const val FORMAT_DESC = "FORMAT_DESC"
        const val START_DATE = "START_DATE"
        const val START_DATE_DESC = "START_DATE_DESC"
        const val END_DATE = "END_DATE"
        const val END_DATE_DESC = "END_DATE_DESC"
        const val SCORE = "SCORE"
        const val SCORE_DESC = "SCORE_DESC"
        const val POPULARITY = "POPULARITY"
        const val POPULARITY_DESC = "POPULARITY_DESC"
        const val TRENDING = "TRENDING"
        const val TRENDING_DESC = "TRENDING_DESC"
        const val EPISODES = "EPISODES"
        const val EPISODES_DESC = "EPISODES_DESC"
        const val DURATION = "DURATION"
        const val DURATION_DESC = "DURATION_DESC"
        const val STATUS = "STATUS"
        const val STATUS_DESC = "STATUS_DESC"
        const val CHAPTERS = "CHAPTERS"
        const val CHAPTERS_DESC = "CHAPTERS_DESC"
        const val VOLUMES = "VOLUMES"
        const val VOLUMES_DESC = "VOLUMES_DESC"
        const val UPDATED_AT = "UPDATED_AT"
        const val UPDATED_AT_DESC = "UPDATED_AT_DESC"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ID_DESC,
            TITLE_ROMAJI,
            TITLE_ROMAJI_DESC,
            TITLE_ENGLISH,
            TITLE_ENGLISH_DESC,
            TITLE_NATIVE,
            TITLE_NATIVE_DESC,
            TYPE,
            TYPE_DESC,
            FORMAT,
            FORMAT_DESC,
            START_DATE,
            START_DATE_DESC,
            END_DATE,
            END_DATE_DESC,
            SCORE,
            SCORE_DESC,
            POPULARITY,
            POPULARITY_DESC,
            TRENDING,
            TRENDING_DESC,
            EPISODES,
            EPISODES_DESC,
            DURATION,
            DURATION_DESC,
            STATUS,
            STATUS_DESC,
            CHAPTERS,
            CHAPTERS_DESC,
            VOLUMES,
            VOLUMES_DESC,
            UPDATED_AT,
            UPDATED_AT_DESC,
            SEARCH_MATCH
        )
    }
}