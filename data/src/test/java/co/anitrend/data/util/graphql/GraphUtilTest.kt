package co.anitrend.data.util.graphql

import co.anitrend.data.model.query.PageQuery
import co.anitrend.data.repository.media.attributes.MediaSortContract
import co.anitrend.data.repository.medialist.attributes.MediaListSortContract
import co.anitrend.data.util.Settings
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*

class GraphUtilTest {

    @Test
    fun `default query has required map keys and values`() {
        val input = PageQuery(
            page = 1
        )

        val actual = GraphUtil.getDefaultQuery(input)

        assertTrue(actual.containsKey("page"))
        assertEquals(1, actual.getVariable("page"))
        assertTrue(actual.containsKey("perPage"))
        assertEquals(GraphUtil.PAGING_LIMIT, actual.getVariable("perPage"))
    }
}