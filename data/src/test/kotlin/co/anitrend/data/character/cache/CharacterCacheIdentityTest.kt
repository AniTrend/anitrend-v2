package co.anitrend.data.character.cache

import co.anitrend.domain.character.enums.CharacterSort
import co.anitrend.domain.character.model.CharacterParam
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class CharacterCacheIdentityTest {
    @Test
    fun `search identity normalizes whitespace and case`() {
        val left =
            CharacterCache.Identity.Search(
                param = CharacterParam.Find(search = "  Luffy  "),
            )
        val right =
            CharacterCache.Identity.Search(
                param = CharacterParam.Find(search = "luffy"),
            )

        assertEquals(left.id, right.id)
    }

    @Test
    fun `search identity normalizes list ordering`() {
        val left =
            CharacterCache.Identity.Search(
                param = CharacterParam.Find(id_in = listOf(9, 1, 4)),
            )
        val right =
            CharacterCache.Identity.Search(
                param = CharacterParam.Find(id_in = listOf(1, 4, 9)),
            )

        assertEquals(left.id, right.id)
    }

    @Test
    fun `search identity differs for meaningful query changes`() {
        val left =
            CharacterCache.Identity.Search(
                param = CharacterParam.Find(search = "luffy", sort = listOf(CharacterSort.SEARCH_MATCH)),
            )
        val right =
            CharacterCache.Identity.Search(
                param = CharacterParam.Find(search = "zoro", sort = listOf(CharacterSort.SEARCH_MATCH)),
            )

        assertNotEquals(left.id, right.id)
    }

    @Test
    fun `search identity is non-negative`() {
        val identity =
            CharacterCache.Identity.Search(
                param = CharacterParam.Find(search = "luffy", id_in = listOf(5, 2, 9)),
            )

        assertTrue(identity.id >= 0L)
    }
}
