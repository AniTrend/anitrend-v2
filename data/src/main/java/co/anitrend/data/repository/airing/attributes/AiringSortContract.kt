package co.anitrend.data.repository.airing.attributes

import androidx.annotation.StringDef

/**
 * Airing schedule sort values
 */
@StringDef(
    AiringSortContract.ID,
    AiringSortContract.MEDIA_ID,
    AiringSortContract.TIME,
    AiringSortContract.EPISODE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class AiringSortContract {
    companion object {
        const val ID = "ID"
        const val MEDIA_ID = "MEDIA_ID"
        const val TIME = "TIME"
        const val EPISODE = "EPISODE"

        val ALL = listOf(
            ID,
            MEDIA_ID,
            TIME,
            EPISODE
        )
    }
}

@AiringSortContract
typealias AiringSort = String