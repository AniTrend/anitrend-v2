package co.anitrend.data.repository.media

import androidx.annotation.StringDef

/**
 * Media Rank Type values
 */
@StringDef(
    MediaRankType.RATED,
    MediaRankType.POPULAR
)
annotation class MediaRankType {
    companion object {
        /** Ranking is based on the media's ratings/score **/
        const val RATED = "RATED"
        /** Ranking is based on the media's popularity **/
        const val POPULAR = "POPULAR"

        val ALL = listOf(
            RATED,
            POPULAR
        )
    }
}