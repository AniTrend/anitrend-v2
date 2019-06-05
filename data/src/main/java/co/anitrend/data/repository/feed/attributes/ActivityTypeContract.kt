package co.anitrend.data.repository.feed.attributes

import androidx.annotation.StringDef

@StringDef(
    ActivityTypeContract.TEXT,
    ActivityTypeContract.ANIME_LIST,
    ActivityTypeContract.MANGA_LIST,
    ActivityTypeContract.MESSAGE,
    ActivityTypeContract.MEDIA_LIST
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class ActivityTypeContract {
    companion object {
        /** A text activity */
        const val TEXT = "TEXT"

        /** A anime list update activity */
        const val ANIME_LIST = "ANIME_LIST"

        /** A manga list update activity */
        const val MANGA_LIST = "MANGA_LIST"

        /** A text message activity sent to another user */
        const val MESSAGE = "MESSAGE"

        /** Anime & Manga list update, only used in query arguments */
        const val MEDIA_LIST = "MEDIA_LIST"

        val ALL = listOf(
            TEXT,
            ANIME_LIST,
            MANGA_LIST,
            MESSAGE,
            MEDIA_LIST
        )
    }
}

@ActivityTypeContract
typealias ActivityType = String