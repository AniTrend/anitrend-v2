package co.anitrend.data.repository.character.attributes

import androidx.annotation.StringDef

/**
 * Character Sort values
 */
@StringDef(
    CharacterSortContract.ID,
    CharacterSortContract.ROLE,
    CharacterSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class CharacterSortContract {
    companion object {
        const val ID = "ID"
        const val ROLE = "ROLE"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ROLE,
            SEARCH_MATCH
        )
    }
}

@CharacterSortContract
typealias CharacterSort = String