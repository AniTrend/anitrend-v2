package co.anitrend.data.util.graphql

import co.anitrend.data.model.query.PageQuery
import org.junit.Test

import org.junit.Assert.*

class GraphUtilTest {

    @Test
    fun getDefaultQuery() {
        val input = PageQuery(
            page = 1
        )

        val actual = GraphUtil.getDefaultQuery(input)

        assertTrue(actual.containsKey("page"))
        assertEquals(1, actual.getVariable("page"))
        assertEquals(GraphUtil.PAGING_LIMIT, actual.getVariable("perPage"))
    }
}