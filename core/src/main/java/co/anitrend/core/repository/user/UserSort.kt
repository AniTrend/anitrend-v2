package co.anitrend.core.repository.user

import androidx.annotation.StringDef

/**
 * User sort values
 */
@StringDef(
    UserSort.ID,
    UserSort.ID_DESC,
    UserSort.USERNAME,
    UserSort.USERNAME_DESC,
    UserSort.WATCHED_TIME,
    UserSort.WATCHED_TIME_DESC,
    UserSort.CHAPTERS_READ,
    UserSort.CHAPTERS_READ_DESC,
    UserSort.SEARCH_MATCH
)
annotation class UserSort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val USERNAME = "USERNAME"
        const val USERNAME_DESC = "USERNAME_DESC"
        const val WATCHED_TIME = "WATCHED_TIME"
        const val WATCHED_TIME_DESC = "WATCHED_TIME_DESC"
        const val CHAPTERS_READ = "CHAPTERS_READ"
        const val CHAPTERS_READ_DESC = "CHAPTERS_READ_DESC"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ID_DESC,
            USERNAME,
            USERNAME_DESC,
            WATCHED_TIME,
            WATCHED_TIME_DESC,
            CHAPTERS_READ,
            CHAPTERS_READ_DESC,
            SEARCH_MATCH
        )
    }
}