package co.anitrend.data.common.model.paging.query

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PageQueryTest {

    @Test
    fun `onPagePrevious should not underflow below page 1`() {
        val query = PageQuery(page = 1, perPage = 30)

        // Act
        query.onPagePrevious()

        // Expect: still page 1 (no underflow to 0)
        assertEquals(1, query.page)
    }

    @Test
    fun `onPagePrevious should decrement when page greater than 1`() {
        val query = PageQuery(page = 3, perPage = 30)

        // Act
        query.onPagePrevious()

        assertEquals(2, query.page)
    }
}
