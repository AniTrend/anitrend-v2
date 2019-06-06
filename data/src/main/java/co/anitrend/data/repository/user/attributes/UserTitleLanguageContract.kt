package co.anitrend.data.repository.user.attributes

import androidx.annotation.StringDef

/**
 * User title language values
 */
@StringDef(
    UserTitleLanguageContract.ROMAJI,
    UserTitleLanguageContract.ENGLISH,
    UserTitleLanguageContract.NATIVE,
    UserTitleLanguageContract.ROMAJI_STYLISED,
    UserTitleLanguageContract.ENGLISH_STYLISED,
    UserTitleLanguageContract.NATIVE_STYLISED
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class UserTitleLanguageContract {
    companion object {
        /** The romanization of the native language title **/
        const val ROMAJI = "ROMAJI"

        /** The official english title **/
        const val ENGLISH = "ENGLISH"

        /** Official title in it's native language **/
        const val NATIVE = "NATIVE"

        /** The romanization of the native language title, stylised by media creator **/
        const val ROMAJI_STYLISED = "ROMAJI_STYLISED"

        /** The official english title, stylised by media creator **/
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

@UserTitleLanguageContract
typealias UserTitleLanguage = String