package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media list scoring type
 */
@StringDef(
    ScoreFormat.POINT_100,
    ScoreFormat.POINT_10_DECIMAL,
    ScoreFormat.POINT_10,
    ScoreFormat.POINT_5,
    ScoreFormat.POINT_3
)
annotation class ScoreFormat {
    companion object {
        // An integer from 0-100
        const val POINT_100 = "POINT_100"
        // A float from 0-10 with 1 decimal place
        const val POINT_10_DECIMAL = "POINT_10_DECIMAL"
        // An integer from 0-10
        const val POINT_10 = "POINT_10"
        // An integer from 0-5. Should be represented in Stars
        const val POINT_5 = "POINT_5"
        // An integer from 0-3. Should be represented in Smileys. 0 => No Score, 1 => :(, 2
        // => :|, 3 => :)
        const val POINT_3 = "POINT_3"

        val ALL = listOf(
            POINT_100,
            POINT_10_DECIMAL,
            POINT_10,
            POINT_5,
            POINT_3
        )
    }
}