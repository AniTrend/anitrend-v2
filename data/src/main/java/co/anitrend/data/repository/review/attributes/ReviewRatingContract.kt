package co.anitrend.data.repository.review.attributes

import androidx.annotation.StringDef

/**
 * Review rating values
 */
@StringDef(
    ReviewRatingContract.NO_VOTE,
    ReviewRatingContract.UP_VOTE,
    ReviewRatingContract.DOWN_VOTE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class ReviewRatingContract {
    companion object {
        const val NO_VOTE = "NO_VOTE"
        const val UP_VOTE = "UP_VOTE"
        const val DOWN_VOTE = "DOWN_VOTE"

        val ALL = listOf(
            NO_VOTE,
            UP_VOTE,
            DOWN_VOTE
        )
    }
}

@ReviewRatingContract
typealias ReviewRating = String