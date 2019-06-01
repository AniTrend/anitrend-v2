package co.anitrend.data.repository.thread.attributes

import androidx.annotation.StringDef

/**
 * Thread sort values
 */
@StringDef(
    ThreadSort.ID,
    ThreadSort.TITLE,
    ThreadSort.TITLE_DESC,
    ThreadSort.CREATED_AT,
    ThreadSort.CREATED_AT_DESC,
    ThreadSort.UPDATED_AT,
    ThreadSort.UPDATED_AT_DESC,
    ThreadSort.REPLIED_AT,
    ThreadSort.REPLIED_AT_DESC,
    ThreadSort.REPLY_COUNT,
    ThreadSort.REPLY_COUNT_DESC,
    ThreadSort.VIEW_COUNT,
    ThreadSort.VIEW_COUNT_DESC,
    ThreadSort.IS_STICKY,
    ThreadSort.SEARCH_MATCH
)
annotation class ThreadSort {
    companion object {
        const val ID = "ID"
        const val TITLE = "TITLE"
        const val TITLE_DESC = "TITLE_DESC"
        const val CREATED_AT = "CREATED_AT"
        const val CREATED_AT_DESC = "CREATED_AT_DESC"
        const val UPDATED_AT = "UPDATED_AT"
        const val UPDATED_AT_DESC = "UPDATED_AT_DESC"
        const val REPLIED_AT = "REPLIED_AT"
        const val REPLIED_AT_DESC = "REPLIED_AT_DESC"
        const val REPLY_COUNT = "REPLY_COUNT"
        const val REPLY_COUNT_DESC = "REPLY_COUNT_DESC"
        const val VIEW_COUNT = "VIEW_COUNT"
        const val VIEW_COUNT_DESC = "VIEW_COUNT_DESC"
        const val IS_STICKY = "IS_STICKY"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            TITLE,
            TITLE_DESC,
            CREATED_AT,
            CREATED_AT_DESC,
            UPDATED_AT,
            UPDATED_AT_DESC,
            REPLIED_AT,
            REPLIED_AT_DESC,
            REPLY_COUNT,
            REPLY_COUNT_DESC,
            VIEW_COUNT,
            VIEW_COUNT_DESC,
            IS_STICKY,
            SEARCH_MATCH
        )
    }
}