package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * MediaFormatContract values
 */
@StringDef(
    MediaFormatContract.TV,
    MediaFormatContract.TV_SHORT,
    MediaFormatContract.MOVIE,
    MediaFormatContract.SPECIAL,
    MediaFormatContract.OVA,
    MediaFormatContract.ONA,
    MediaFormatContract.MUSIC,
    MediaFormatContract.MANGA,
    MediaFormatContract.NOVEL,
    MediaFormatContract.ONE_SHOT
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaFormatContract {
    companion object {
        /** Anime broadcast on television */
        const val TV = "TV"

        /** Anime which are under 15 minutes in length and broadcast on television */
        const val TV_SHORT = "TV_SHORT"

        /** Anime movies with a theatrical release */
        const val MOVIE = "MOVIE"

        /**
         * Special episodes that have been included in DVD/Blu-ray releases, picture
         * dramas, pilots, etc
         */
        const val SPECIAL = "SPECIAL"

        /**
         * (Original Video Animation) Anime that have been released directly on DVD/Blu-ray
         * without originally going through a theatrical release or television broadcast
         */
        const val OVA = "OVA"

        /**
         * (Original Net Animation) Anime that have been originally released online or are
         * only available through streaming services.
         */
        const val ONA = "ONA"

        /** Short anime released as a music video */
        const val MUSIC = "MUSIC"

        /** Professionally published manga with more than one chapter */
        const val MANGA = "MANGA"

        /** Written books released as a novel or series of light novels */
        const val NOVEL = "NOVEL"

        /** Manga with just one chapter */
        const val ONE_SHOT = "ONE_SHOT"

        val ALL = listOf(
            TV,
            TV_SHORT,
            MOVIE,
            SPECIAL,
            OVA,
            ONA,
            MUSIC,
            MANGA,
            NOVEL,
            ONE_SHOT
        )
    }
}

@MediaFormatContract
typealias MediaFormat = String