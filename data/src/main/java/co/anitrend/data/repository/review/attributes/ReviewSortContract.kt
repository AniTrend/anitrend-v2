package co.anitrend.data.repository.review.attributes

import androidx.annotation.StringDef

/**
 * Review sort values
 */
@StringDef(
    ReviewSortContract.ID,
    ReviewSortContract.SCORE,
    ReviewSortContract.RATING,
    ReviewSortContract.CREATED,
    ReviewSortContract.UPDATED
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class ReviewSortContract {
    companion object {
        const val ID = "ID"
        const val SCORE = "SCORE"
        const val RATING = "RATING"
        const val CREATED = "CREATED"
        const val UPDATED = "UPDATED"

        val ALL = listOf(
            ID,
            SCORE,
            RATING,
            CREATED,
            UPDATED
        )
    }
}

@ReviewSortContract
typealias ReviewSort = String