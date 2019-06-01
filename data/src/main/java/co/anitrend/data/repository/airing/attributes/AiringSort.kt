package co.anitrend.data.repository.airing.attributes

import androidx.annotation.StringDef

/**
 * Airing schedule sort values
 */
@StringDef(
    AiringSort.ID,
    AiringSort.ID_DESC,
    AiringSort.MEDIA_ID,
    AiringSort.MEDIA_ID_DESC,
    AiringSort.TIME,
    AiringSort.TIME_DESC,
    AiringSort.EPISODE,
    AiringSort.EPISODE_DESC
)
annotation class AiringSort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val MEDIA_ID = "MEDIA_ID"
        const val MEDIA_ID_DESC = "MEDIA_ID_DESC"
        const val TIME = "TIME"
        const val TIME_DESC = "TIME_DESC"
        const val EPISODE = "EPISODE"
        const val EPISODE_DESC = "EPISODE_DESC"

        val ALL = listOf(
            ID,
            ID_DESC,
            MEDIA_ID,
            MEDIA_ID_DESC,
            TIME,
            TIME_DESC,
            EPISODE,
            EPISODE_DESC
        )
    }
}