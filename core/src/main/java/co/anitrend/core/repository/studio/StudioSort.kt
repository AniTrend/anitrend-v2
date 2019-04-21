package co.anitrend.core.repository.studio

import androidx.annotation.StringDef

/**
 * Studio sort values
 */
@StringDef(
    StudioSort.ID,
    StudioSort.ID_DESC,
    StudioSort.NAME,
    StudioSort.NAME_DESC,
    StudioSort.SEARCH_MATCH
)
annotation class StudioSort {
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