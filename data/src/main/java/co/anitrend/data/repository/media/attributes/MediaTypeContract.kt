package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media types
 */
@StringDef(
    MediaTypeContract.ANIME,
    MediaTypeContract.MANGA
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaTypeContract {
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

@MediaTypeContract
typealias MediaType = String