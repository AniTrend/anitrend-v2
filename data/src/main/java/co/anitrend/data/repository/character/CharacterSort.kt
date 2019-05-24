package co.anitrend.data.repository.character

import androidx.annotation.StringDef

/**
 * Character Sort values
 */
@StringDef(
    CharacterSort.ID,
    CharacterSort.ID_DESC,
    CharacterSort.ROLE,
    CharacterSort.ROLE_DESC,
    CharacterSort.SEARCH_MATCH
)
annotation class CharacterSort {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val ROLE = "ROLE"
        const val ROLE_DESC = "ROLE_DESC"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ID_DESC,
            ROLE,
            ROLE_DESC,
            SEARCH_MATCH
        )
    }
}