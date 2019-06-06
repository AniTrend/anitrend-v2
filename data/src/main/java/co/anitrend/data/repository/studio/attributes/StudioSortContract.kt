package co.anitrend.data.repository.studio.attributes

import androidx.annotation.StringDef

/**
 * Studio sort values
 */
@StringDef(
    StudioSortContract.ID,
    StudioSortContract.ID_DESC,
    StudioSortContract.NAME,
    StudioSortContract.NAME_DESC,
    StudioSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class StudioSortContract {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val NAME = "NAME"
        const val NAME_DESC = "NAME_DESC"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ID_DESC,
            NAME,
            NAME_DESC,
            SEARCH_MATCH
        )
    }
}

@StudioSortContract
typealias StudioSort = String