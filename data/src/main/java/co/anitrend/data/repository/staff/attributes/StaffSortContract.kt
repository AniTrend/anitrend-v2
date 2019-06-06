package co.anitrend.data.repository.staff.attributes

import androidx.annotation.StringDef

/**
 * Staff sort values
 */
@StringDef(
    StaffSortContract.ID,
    StaffSortContract.ROLE,
    StaffSortContract.LANGUAGE,
    StaffSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class StaffSortContract {
    companion object {
        const val ID = "ID"
        const val ROLE = "ROLE"
        const val LANGUAGE = "LANGUAGE"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ROLE,
            LANGUAGE,
            SEARCH_MATCH
        )
    }
}

@StaffSortContract
typealias StaffSort = String