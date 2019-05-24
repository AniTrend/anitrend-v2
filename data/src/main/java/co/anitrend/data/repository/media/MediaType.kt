package co.anitrend.data.repository.media

import androidx.annotation.StringDef

/**
 * Media types
 */
@StringDef(
    MediaType.ANIME,
    MediaType.MANGA
)
annotation class MediaType {
    companion object {
        /** Japanese Anime **/
        const val ANIME = "ANIME"
        /** Asian comic **/
        const val MANGA = "MANGA"

        val ALL = listOf(
            ANIME,
            MANGA
        )
    }
}