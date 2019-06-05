package co.anitrend.data.repository.thread.attributes

import androidx.annotation.StringDef

/**
 * Thread sort values
 */
@StringDef(
    ThreadSortContract.ID,
    ThreadSortContract.TITLE,
    ThreadSortContract.CREATED_AT,
    ThreadSortContract.UPDATED_AT,
    ThreadSortContract.REPLIED_AT,
    ThreadSortContract.REPLY_COUNT,
    ThreadSortContract.VIEW_COUNT,
    ThreadSortContract.IS_STICKY,
    ThreadSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class ThreadSortContract {
    companion object {
        const val ID = "ID"
        const val TITLE = "TITLE"
        const val CREATED_AT = "CREATED_AT"
        const val UPDATED_AT = "UPDATED_AT"
        const val REPLIED_AT = "REPLIED_AT"
        const val REPLY_COUNT = "REPLY_COUNT"
        const val VIEW_COUNT = "VIEW_COUNT"
        const val IS_STICKY = "IS_STICKY"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            TITLE,
            CREATED_AT,
            UPDATED_AT,
            REPLIED_AT,
            REPLY_COUNT,
            VIEW_COUNT,
            IS_STICKY,
            SEARCH_MATCH
        )
    }
}

@ThreadSortContract
typealias ThreadSort = String