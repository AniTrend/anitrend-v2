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
package co.anitrend.data.staff.model.query

import co.anitrend.domain.staff.enums.StaffSort
import co.anitrend.domain.staff.model.StaffParam
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class StaffQueryTest {

    // ── StaffQuery.Find ──────────────────────────────────────────────

    @Test
    fun `Find toMap includes search key and value when search is set`() {
        val param = StaffParam.Find(
            id = 1L,
            search = "Yoshinori",
            id_not = 0L,
            id_in = null,
            id_not_in = null,
            sort = null,
        )
        val query = StaffQuery.Find(param = param)
        val map = query.toMap()

        assertNotNull(map, "toMap must not return null")
        assertTrue(map.containsKey("search"), "Map must contain 'search' key")
        assertEquals("Yoshinori", map["search"])
    }

    @Test
    fun `Find toMap includes id key and value`() {
        val param = StaffParam.Find(
            id = 99L,
            search = "",
            id_not = 0L,
            id_in = null,
            id_not_in = null,
            sort = null,
        )
        val query = StaffQuery.Find(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("id"), "Map must contain 'id' key")
        assertEquals(99L, map["id"])
    }

    @Test
    fun `Find toMap includes sort key and value when sort is set`() {
        val param = StaffParam.Find(
            id = 1L,
            search = "test",
            id_not = 0L,
            id_in = null,
            id_not_in = null,
            sort = listOf(StaffSort.FAVOURITES, StaffSort.LANGUAGE),
        )
        val query = StaffQuery.Find(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("sort"), "Map must contain 'sort' key")
        assertEquals(listOf(StaffSort.FAVOURITES, StaffSort.LANGUAGE), map["sort"])
    }

    @Test
    fun `Find toMap includes all expected keys with non-trivial param`() {
        val param = StaffParam.Find(
            id = 1L,
            search = "Shinichiro",
            id_not = 2L,
            id_in = listOf(1L, 3L, 5L),
            id_not_in = listOf(4L),
            sort = listOf(StaffSort.SEARCH_MATCH),
        )
        val query = StaffQuery.Find(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("id"), "Map must contain 'id'")
        assertTrue(map.containsKey("search"), "Map must contain 'search'")
        assertTrue(map.containsKey("sort"), "Map must contain 'sort'")
        assertTrue(map.containsKey("id_not"), "Map must contain 'id_not'")
        assertTrue(map.containsKey("id_in"), "Map must contain 'id_in'")
        assertTrue(map.containsKey("id_not_in"), "Map must contain 'id_not_in'")

        assertEquals(1L, map["id"])
        assertEquals("Shinichiro", map["search"])
        assertEquals(listOf(StaffSort.SEARCH_MATCH), map["sort"])
        assertEquals(2L, map["id_not"])
        assertEquals(listOf(1L, 3L, 5L), map["id_in"])
        assertEquals(listOf(4L), map["id_not_in"])
    }

    // ── StaffQuery.Paged ─────────────────────────────────────────────

    @Test
    fun `Paged toMap includes search key and value when search is set`() {
        val param = StaffParam.Paged(search = "Ufotable")
        val query = StaffQuery.Paged(param = param)
        val map = query.toMap()

        assertNotNull(map, "toMap must not return null")
        assertTrue(map.containsKey("search"), "Map must contain 'search' key")
        assertEquals("Ufotable", map["search"])
    }

    @Test
    fun `Paged toMap includes sort key and value when sort is set`() {
        val param = StaffParam.Paged(sort = listOf(StaffSort.FAVOURITES))
        val query = StaffQuery.Paged(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("sort"), "Map must contain 'sort' key")
        assertEquals(listOf(StaffSort.FAVOURITES), map["sort"])
    }

    @Test
    fun `Paged toMap includes isBirthday key and value when set`() {
        val param = StaffParam.Paged(isBirthday = true)
        val query = StaffQuery.Paged(param = param)
        val map = query.toMap()

        assertTrue(map.containsKey("isBirthday"), "Map must contain 'isBirthday' key")
        assertEquals(true, map["isBirthday"])
    }

    @Test
    fun `Paged toMap maps null fields as null`() {
        val param = StaffParam.Paged()
        val query = StaffQuery.Paged(param = param)
        val map = query.toMap()

        assertEquals(null, map["search"])
        assertEquals(null, map["sort"])
        assertEquals(null, map["isBirthday"])
        assertEquals(null, map["id_in"])
        assertEquals(null, map["id_not"])
        assertEquals(null, map["id_not_in"])
    }
}
