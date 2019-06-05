package co.anitrend.data.repository.character.attributes

import androidx.annotation.StringDef

/**
 * The role the character plays in the media
 */
@StringDef(
    CharacterRoleContract.MAIN,
    CharacterRoleContract.SUPPORTING,
    CharacterRoleContract.BACKGROUND
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class CharacterRoleContract {
    companion object {
        /** A primary character role in the media */
        const val MAIN = "MAIN"

        /** A supporting character role in the media */
        const val SUPPORTING = "SUPPORTING"

        /** A background character in the media */
        const val BACKGROUND = "BACKGROUND"

        val ALL = listOf(
            MAIN,
            SUPPORTING,
            BACKGROUND
        )
    }
}

@CharacterRoleContract
typealias CharacterRole = String