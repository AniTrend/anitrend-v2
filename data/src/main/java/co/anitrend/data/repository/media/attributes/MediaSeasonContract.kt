package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media Season values
 */
@StringDef(
    MediaSeasonContract.WINTER,
    MediaSeasonContract.SPRING,
    MediaSeasonContract.SUMMER,
    MediaSeasonContract.FALL
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaSeasonContract {
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

@MediaSeasonContract
typealias MediaSeason = String