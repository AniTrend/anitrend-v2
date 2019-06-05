package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media Sort values
 */
@StringDef(
    MediaSortContract.ID,
    MediaSortContract.TITLE_ROMAJI,
    MediaSortContract.TITLE_ENGLISH,
    MediaSortContract.TITLE_NATIVE,
    MediaSortContract.TYPE,
    MediaSortContract.FORMAT,
    MediaSortContract.START_DATE,
    MediaSortContract.END_DATE,
    MediaSortContract.SCORE,
    MediaSortContract.POPULARITY,
    MediaSortContract.TRENDING,
    MediaSortContract.EPISODES,
    MediaSortContract.DURATION,
    MediaSortContract.STATUS,
    MediaSortContract.CHAPTERS,
    MediaSortContract.VOLUMES,
    MediaSortContract.UPDATED_AT,
    MediaSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaSortContract {
    companion object {
        const val ID = "ID"
        const val TITLE_ROMAJI = "TITLE_ROMAJI"
        const val TITLE_ENGLISH = "TITLE_ENGLISH"
        const val TITLE_NATIVE = "TITLE_NATIVE"
        const val TYPE = "TYPE"
        const val FORMAT = "FORMAT"
        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"
        const val SCORE = "SCORE"
        const val POPULARITY = "POPULARITY"
        const val TRENDING = "TRENDING"
        const val EPISODES = "EPISODES"
        const val DURATION = "DURATION"
        const val STATUS = "STATUS"
        const val CHAPTERS = "CHAPTERS"
        const val VOLUMES = "VOLUMES"
        const val UPDATED_AT = "UPDATED_AT"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            TITLE_ROMAJI,
            TITLE_ENGLISH,
            TITLE_NATIVE,
            TYPE,
            FORMAT,
            START_DATE,
            END_DATE,
            SCORE,
            POPULARITY,
            TRENDING,
            EPISODES,
            DURATION,
            STATUS,
            CHAPTERS,
            VOLUMES,
            UPDATED_AT,
            SEARCH_MATCH
        )
    }
}

@MediaSortContract
typealias MediaSort = String