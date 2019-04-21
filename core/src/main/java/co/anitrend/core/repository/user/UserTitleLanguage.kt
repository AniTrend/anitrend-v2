package co.anitrend.core.repository.user

import androidx.annotation.StringDef

/**
 * User title language values
 */
@StringDef(
    UserTitleLanguage.ROMAJI,
    UserTitleLanguage.ENGLISH,
    UserTitleLanguage.NATIVE,
    UserTitleLanguage.ROMAJI_STYLISED,
    UserTitleLanguage.ENGLISH_STYLISED,
    UserTitleLanguage.NATIVE_STYLISED
)
annotation class UserTitleLanguage {
    companion object {
        /** The romanization of the native language title **/
        const val ROMAJI = "ROMAJI"

        /** The official english title **/
        const val ENGLISH = "ENGLISH"

        /** Official title in it's native language **/
        const val NATIVE = "NATIVE"

        /** The romanization of the native language title, stylised by media creator **/
        const val ROMAJI_STYLISED = "ROMAJI_STYLISED"

        /** he official english title, stylised by media creator **/
        const val ENGLISH_STYLISED = "ENGLISH_STYLISED"

        /** Official title in it's native language, stylised by media creator **/
        const val NATIVE_STYLISED = "NATIVE_STYLISED"

        val ALL = listOf(
            ROMAJI,
            ENGLISH,
            NATIVE,
            ROMAJI_STYLISED,
            ENGLISH_STYLISED,
            NATIVE_STYLISED
        )
    }
}