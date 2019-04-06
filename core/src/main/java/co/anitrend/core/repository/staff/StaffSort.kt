package co.anitrend.core.repository.staff

import androidx.annotation.StringDef

/**
 * Staff sort values
 */
@StringDef(
    StaffSort.ID,
    StaffSort.ID_DESC,
    StaffSort.ROLE,
    StaffSort.ROLE_DESC,
    StaffSort.LANGUAGE,
    StaffSort.LANGUAGE_DESC,
    StaffSort.SEARCH_MATCH
)
annotation class StaffSort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val ROLE = "ROLE"
        const val ROLE_DESC = "ROLE_DESC"
        const val LANGUAGE = "LANGUAGE"
        const val LANGUAGE_DESC = "LANGUAGE_DESC"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ID_DESC,
            ROLE,
            ROLE_DESC,
            LANGUAGE,
            LANGUAGE_DESC,
            SEARCH_MATCH
        )
    }
}