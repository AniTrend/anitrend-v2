package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media Rank Type values
 */
@StringDef(
    MediaRankTypeContract.RATED,
    MediaRankTypeContract.POPULAR
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaRankTypeContract {
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

@MediaRankTypeContract
typealias MediaRankType = String