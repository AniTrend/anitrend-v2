package co.anitrend.data.repository.review.attributes

import androidx.annotation.StringDef

/**
 * Review rating values
 */
@StringDef(
    ReviewRating.NO_VOTE,
    ReviewRating.UP_VOTE,
    ReviewRating.DOWN_VOTE
)
annotation class ReviewRating {
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