package co.anitrend.data.repository.character

import androidx.annotation.StringDef

/**
 * The role the character plays in the media
 */
@StringDef(
    CharacterRole.MAIN,
    CharacterRole.SUPPORTING,
    CharacterRole.BACKGROUND
)
annotation class CharacterRole {
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