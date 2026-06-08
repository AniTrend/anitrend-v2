package co.anitrend.data.character.entity.filter

import co.anitrend.domain.character.model.CharacterParam
import kotlin.test.Test
import kotlin.test.assertTrue

class CharacterQueryFilterTest {

    @Test
    fun `search filter SQL references character table and LIKE predicates`() {
        val sql = CharacterQueryFilter.Search().build(CharacterParam.Find(search = "bleach")).sql.lowercase()

        assertTrue(sql.contains("from character"), "Expected character table in SQL, got: $sql")
        assertTrue(sql.contains(" like "), "Expected LIKE predicate for character search, got: $sql")
        assertTrue(sql.contains("name_full") || sql.contains("name_user_preferred"), "Expected character name columns in SQL, got: $sql")
    }
}
