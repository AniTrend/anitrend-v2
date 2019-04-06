package co.anitrend.core.repository.activity

import androidx.annotation.StringDef

/**
 * Activity Sort Values
 */
@StringDef(
    ActivitySort.ID,
    ActivitySort.ID_DESC
)
annotation class ActivitySort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"

        val ALL = listOf(
            ID,
            ID_DESC
        )
    }
}