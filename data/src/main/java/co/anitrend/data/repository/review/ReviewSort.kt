package co.anitrend.data.repository.review

import androidx.annotation.StringDef

/**
 * Review sort values
 */
@StringDef(
    ReviewSort.ID,
    ReviewSort.ID_DESC,
    ReviewSort.SCORE,
    ReviewSort.SCORE_DESC,
    ReviewSort.RATING,
    ReviewSort.RATING_DESC,
    ReviewSort.CREATED,
    ReviewSort.CREATED_DESC,
    ReviewSort.UPDATED,
    ReviewSort.UPDATED_DESC
)
annotation class ReviewSort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val SCORE = "SCORE"
        const val SCORE_DESC = "SCORE_DESC"
        const val RATING = "RATING"
        const val RATING_DESC = "RATING_DESC"
        const val CREATED = "CREATED"
        const val CREATED_DESC = "CREATED_DESC"
        const val UPDATED = "UPDATED"
        const val UPDATED_DESC = "UPDATED_DESC"

        val ALL = listOf(
            ID,
            ID_DESC,
            SCORE,
            SCORE_DESC,
            RATING,
            RATING_DESC,
            CREATED,
            CREATED_DESC,
            UPDATED,
            UPDATED_DESC
        )
    }
}