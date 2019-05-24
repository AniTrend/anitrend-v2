package co.anitrend.data.repository.activity

import androidx.annotation.StringDef

@StringDef(
    ActivityType.TEXT,
    ActivityType.ANIME_LIST,
    ActivityType.MANGA_LIST,
    ActivityType.MESSAGE,
    ActivityType.MEDIA_LIST
)
annotation class ActivityType {
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