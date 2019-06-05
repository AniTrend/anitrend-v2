package co.anitrend.data.repository.feed.attributes

import androidx.annotation.StringDef

/**
 * Activity Sort Values
 */
@StringDef(
    ActivitySortContract.ID
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class ActivitySortContract {
    companion object {
        const val ID = "ID"

        val ALL = listOf(
            ID
        )
    }
}

@ActivitySortContract
typealias ActivitySort = String
