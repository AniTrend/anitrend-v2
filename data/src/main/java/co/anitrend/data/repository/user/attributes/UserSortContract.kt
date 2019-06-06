package co.anitrend.data.repository.user.attributes

import androidx.annotation.StringDef

/**
 * User sort values
 */
@StringDef(
    UserSortContract.ID,
    UserSortContract.USERNAME,
    UserSortContract.WATCHED_TIME,
    UserSortContract.CHAPTERS_READ,
    UserSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class UserSortContract {
    companion object {
        const val ID = "ID"
        const val USERNAME = "USERNAME"
        const val WATCHED_TIME = "WATCHED_TIME"
        const val CHAPTERS_READ = "CHAPTERS_READ"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            USERNAME,
            WATCHED_TIME,
            CHAPTERS_READ,
            SEARCH_MATCH
        )
    }
}

@UserSortContract
typealias UserSort = String