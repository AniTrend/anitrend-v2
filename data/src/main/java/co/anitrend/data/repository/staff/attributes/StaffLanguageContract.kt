package co.anitrend.data.repository.staff.attributes

import androidx.annotation.StringDef

/**
 * The primary language of the voice actor
 */
@StringDef(
    StaffLanguageContract.JAPANESE,
    StaffLanguageContract.ENGLISH,
    StaffLanguageContract.KOREAN,
    StaffLanguageContract.ITALIAN,
    StaffLanguageContract.SPANISH,
    StaffLanguageContract.PORTUGUESE,
    StaffLanguageContract.FRENCH,
    StaffLanguageContract.GERMAN,
    StaffLanguageContract.HEBREW,
    StaffLanguageContract.HUNGARIAN
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class StaffLanguageContract {
    companion object {
        /** Japanese */
        const val JAPANESE = "JAPANESE"

        /** English */
        const val ENGLISH = "ENGLISH"

        /** Korean */
        const val KOREAN = "KOREAN"

        /** Italian */
        const val ITALIAN = "ITALIAN"

        /** Spanish */
        const val SPANISH = "SPANISH"

        /** Portuguese */
        const val PORTUGUESE = "PORTUGUESE"

        /** French */
        const val FRENCH = "FRENCH"

        /** German */
        const val GERMAN = "GERMAN"

        /** Hebrew */
        const val HEBREW = "HEBREW"

        /** Hungarian */
        const val HUNGARIAN = "HUNGARIAN"
        
        val ALL = listOf(
            JAPANESE,
            ENGLISH,
            KOREAN,
            ITALIAN,
            SPANISH,
            PORTUGUESE,
            FRENCH,
            GERMAN,
            HEBREW,
            HUNGARIAN
        )
    }
}

@StaffLanguageContract
typealias StaffLanguage = String