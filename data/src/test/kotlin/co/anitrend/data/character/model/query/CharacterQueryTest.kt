/*
 * Copyright (C) 2026 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.data.character.model.query

import co.anitrend.domain.character.enums.CharacterSort
import co.anitrend.domain.character.model.CharacterParam
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class CharacterQueryTest {

    @Test
    fun `toMap includes search key and value when search is set`() {
        val param = CharacterParam.Find(search = "Goku")
        val query = CharacterQuery(param = param)
        val map = query.toMap()

        assertNotNull(map, "toMap must not return null")
        assertTrue(map.containsKey("search"), "Map must contain 'search' key")
        assertEquals("Goku", map["search"])
    }

    @Test
    fun `toMap includes id key and value when id is set`() {
        val param = CharacterParam.Find(id = 42L)
        val query = CharacterQuery(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("id"), "Map must contain 'id' key")
        assertEquals(42L, map["id"])
    }

    @Test
    fun `toMap includes sort key and value when sort is set`() {
        val param = CharacterParam.Find(sort = listOf(CharacterSort.ROLE, CharacterSort.RELEVANCE))
        val query = CharacterQuery(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("sort"), "Map must contain 'sort' key")
        assertEquals(listOf(CharacterSort.ROLE, CharacterSort.RELEVANCE), map["sort"])
    }

    @Test
    fun `toMap includes all expected keys with non-trivial search param`() {
        val param = CharacterParam.Find(
            id = 1L,
            search = "Vegeta",
            id_not = 2L,
            id_in = listOf(1L, 3L),
            id_not_in = listOf(4L, 5L),
            sort = listOf(CharacterSort.SEARCH_MATCH),
        )
        val query = CharacterQuery(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("id"), "Map must contain 'id'")
        assertTrue(map.containsKey("search"), "Map must contain 'search'")
        assertTrue(map.containsKey("sort"), "Map must contain 'sort'")
        assertTrue(map.containsKey("id_not"), "Map must contain 'id_not'")
        assertTrue(map.containsKey("id_in"), "Map must contain 'id_in'")
        assertTrue(map.containsKey("id_not_in"), "Map must contain 'id_not_in'")

        assertEquals(1L, map["id"])
        assertEquals("Vegeta", map["search"])
        assertEquals(listOf(CharacterSort.SEARCH_MATCH), map["sort"])
        assertEquals(2L, map["id_not"])
        assertEquals(listOf(1L, 3L), map["id_in"])
        assertEquals(listOf(4L, 5L), map["id_not_in"])
    }

    @Test
    fun `toMap maps null fields as null values`() {
        val param = CharacterParam.Find()
        val query = CharacterQuery(param = param)
        val map = query.toMap()

        assertEquals(null, map["id"])
        assertEquals(null, map["search"])
        assertEquals(null, map["sort"])
        assertEquals(null, map["id_not"])
        assertEquals(null, map["id_in"])
        assertEquals(null, map["id_not_in"])
    }
}
