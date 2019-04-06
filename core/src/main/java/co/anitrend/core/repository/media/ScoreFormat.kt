package co.anitrend.core.repository.media

import androidx.annotation.StringDef

/**
 * Media list scoring type
 */
@StringDef(
    ScoreFormat.ID,
    ScoreFormat.ID_DESC,
    ScoreFormat.SCORE,
    ScoreFormat.SCORE_DESC,
    ScoreFormat.RATING,
    ScoreFormat.RATING_DESC,
    ScoreFormat.CREATED_AT,
    ScoreFormat.CREATED_AT_DESC,
    ScoreFormat.UPDATED_AT,
    ScoreFormat.UPDATED_AT_DESC
)
annotation class ScoreFormat {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val SCORE = "SCORE"
        const val SCORE_DESC = "SCORE_DESC"
        const val RATING = "RATING"
        const val RATING_DESC = "RATING_DESC"
        const val CREATED_AT = "CREATED_AT"
        const val CREATED_AT_DESC = "CREATED_AT_DESC"
        const val UPDATED_AT = "UPDATED_AT"
        const val UPDATED_AT_DESC = "UPDATED_AT_DESC"

        val ALL = listOf(
            ID,
            ID_DESC,
            SCORE,
            SCORE_DESC,
            RATING,
            RATING_DESC,
            CREATED_AT,
            CREATED_AT_DESC,
            UPDATED_AT,
            UPDATED_AT_DESC
        )
    }
}