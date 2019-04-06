package co.anitrend.core.repository.media

import androidx.annotation.StringDef

/**
 * Media Season values
 */
@StringDef(
    MediaSeason.WINTER,
    MediaSeason.SPRING,
    MediaSeason.SUMMER,
    MediaSeason.FALL
)
annotation class MediaSeason {
    companion object {
        /** Months December to February */
        const val WINTER = "WINTER"
        /** Months March to May */
        const val SPRING = "SPRING"
        /** Months June to August */
        const val SUMMER = "SUMMER"
        /** Months September to November */
        const val FALL = "FALL"

        val ALL = listOf(
            WINTER,
            SPRING,
            SUMMER,
            FALL
        )
    }
}