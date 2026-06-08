package co.anitrend.data.studio.entity.filter

import co.anitrend.domain.studio.model.StudioParam
import kotlin.test.Test
import kotlin.test.assertTrue

class StudioQueryFilterTest {

    @Test
    fun `search filter SQL references studio table with LIKE and order`() {
        val sql = StudioQueryFilter.Search().build(StudioParam.Find(search = "a-1")).sql.lowercase()

        assertTrue(sql.contains("from studio"), "Expected studio table in SQL, got: $sql")
        assertTrue(sql.contains(" like "), "Expected LIKE predicate for studio search, got: $sql")
        assertTrue(sql.contains("order by"), "Expected order clause for studio search, got: $sql")
    }
}
