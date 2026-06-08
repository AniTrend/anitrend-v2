package co.anitrend.data.airing.cache

import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.domain.common.sort.SortWithOrder
import co.anitrend.domain.common.sort.order.SortOrder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AiringCacheIdentityTest {
    @Test
    fun `paged identity should be stable for identical params`() {
        val param =
            AiringParam.Find(
                airingAt_greater = 1_716_960_000,
                sort = listOf(SortWithOrder(AiringSort.TIME, SortOrder.ASC)),
            )

        val first = AiringCache.Identity.Paged(param)
        val second = AiringCache.Identity.Paged(param.copy())

        assertEquals(first.id, second.id)
        assertEquals(first.key, second.key)
    }

    @Test
    fun `paged identity should differ for different params`() {
        val firstParam =
            AiringParam.Find(
                airingAt_greater = 1_716_960_000,
                sort = listOf(SortWithOrder(AiringSort.TIME, SortOrder.ASC)),
            )
        val secondParam =
            AiringParam.Find(
                airingAt_greater = 1_716_963_600,
                sort = listOf(SortWithOrder(AiringSort.TIME, SortOrder.ASC)),
            )

        val first = AiringCache.Identity.Paged(firstParam)
        val second = AiringCache.Identity.Paged(secondParam)

        assertNotEquals(first.id, second.id)
        assertEquals(first.key, second.key)
    }
}
