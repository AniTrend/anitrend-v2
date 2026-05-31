package co.anitrend.data.studio.cache

import co.anitrend.domain.studio.enums.StudioSort
import co.anitrend.domain.studio.model.StudioParam
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class StudioCacheIdentityTest {
    @Test
    fun `search identity normalizes whitespace and case`() {
        val left =
            StudioCache.Identity.Search(
                param = StudioParam.Find(search = "  MADHOUSE  "),
            )
        val right =
            StudioCache.Identity.Search(
                param = StudioParam.Find(search = "madhouse"),
            )

        assertEquals(left.id, right.id)
    }

    @Test
    fun `search identity normalizes list ordering`() {
        val left =
            StudioCache.Identity.Search(
                param = StudioParam.Find(id_in = listOf(3, 1, 2)),
            )
        val right =
            StudioCache.Identity.Search(
                param = StudioParam.Find(id_in = listOf(1, 2, 3)),
            )

        assertEquals(left.id, right.id)
    }

    @Test
    fun `search identity differs for meaningful query changes`() {
        val left =
            StudioCache.Identity.Search(
                param = StudioParam.Find(search = "madhouse", sort = listOf(StudioSort.SEARCH_MATCH)),
            )
        val right =
            StudioCache.Identity.Search(
                param = StudioParam.Find(search = "bones", sort = listOf(StudioSort.SEARCH_MATCH)),
            )

        assertNotEquals(left.id, right.id)
    }

    @Test
    fun `search identity is non-negative`() {
        val identity =
            StudioCache.Identity.Search(
                param = StudioParam.Find(search = "madhouse", id_in = listOf(5, 2, 9)),
            )

        assertTrue(identity.id >= 0L)
    }
}
