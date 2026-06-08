package co.anitrend.data.staff.cache

import co.anitrend.domain.staff.enums.StaffSort
import co.anitrend.domain.staff.model.StaffParam
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class StaffCacheIdentityTest {
    @Test
    fun `paged identity normalizes whitespace and case`() {
        val left =
            StaffCache.Identity.Paged(
                param = StaffParam.Paged(search = "  Miyazaki  "),
            )
        val right =
            StaffCache.Identity.Paged(
                param = StaffParam.Paged(search = "miyazaki"),
            )

        assertEquals(left.id, right.id)
    }

    @Test
    fun `paged identity normalizes list ordering`() {
        val left =
            StaffCache.Identity.Paged(
                param = StaffParam.Paged(id_in = listOf(8, 3, 1)),
            )
        val right =
            StaffCache.Identity.Paged(
                param = StaffParam.Paged(id_in = listOf(1, 3, 8)),
            )

        assertEquals(left.id, right.id)
    }

    @Test
    fun `paged identity differs for meaningful query changes`() {
        val left =
            StaffCache.Identity.Paged(
                param = StaffParam.Paged(search = "miyazaki", sort = listOf(StaffSort.SEARCH_MATCH)),
            )
        val right =
            StaffCache.Identity.Paged(
                param = StaffParam.Paged(search = "anno", sort = listOf(StaffSort.SEARCH_MATCH)),
            )

        assertNotEquals(left.id, right.id)
    }

    @Test
    fun `paged identity is non-negative`() {
        val identity =
            StaffCache.Identity.Paged(
                param = StaffParam.Paged(search = "miyazaki", id_in = listOf(5, 2, 9)),
            )

        assertTrue(identity.id >= 0L)
    }
}
