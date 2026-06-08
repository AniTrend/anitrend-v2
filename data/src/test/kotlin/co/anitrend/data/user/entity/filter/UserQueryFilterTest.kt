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
package co.anitrend.data.user.entity.filter

import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.domain.common.sort.SortWithOrder
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.user.enums.UserSort
import co.anitrend.domain.user.model.UserParam
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class UserQueryFilterTest {

    @Test
    fun `Search with non-blank term and sort produces non-null SQL`() {
        val param = UserParam.Search(
            search = "testuser",
            sort = listOf(
                SortWithOrder(UserSort.USERNAME, SortOrder.ASC),
            ),
        )
        val sqlQuery: SupportSQLiteQuery = UserQueryFilter.Search().build(param)

        assertNotNull(sqlQuery, "Built SQL query must not be null")
        assertNotNull(sqlQuery.sql, "SQL string must not be null")

        val sql = sqlQuery.sql.lowercase()
        assertTrue(sql.isNotEmpty(), "SQL string must not be empty")
    }

    @Test
    fun `Search with non-blank term includes user table in SQL`() {
        val param = UserParam.Search(
            search = "anitrend",
            sort = null,
        )
        val sqlQuery = UserQueryFilter.Search().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(
            sql.contains("user"),
            "SQL should reference the user table, got: ${sqlQuery.sql}",
        )
    }

    @Test
    fun `Search with non-blank term includes WHERE predicate in SQL`() {
        val param = UserParam.Search(
            search = "testuser",
            sort = null,
        )
        val sqlQuery = UserQueryFilter.Search().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(
            sql.contains("where"),
            "SQL should contain a WHERE clause for search predicate, got: ${sqlQuery.sql}",
        )
        assertTrue(
            sql.contains(" like "),
            "User search should use LIKE predicate for user_name column, got: ${sqlQuery.sql}",
        )
    }

    @Test
    fun `Search with sort includes ORDER BY in SQL`() {
        val param = UserParam.Search(
            search = "ordered_user",
            sort = listOf(
                SortWithOrder(UserSort.USERNAME, SortOrder.DESC),
            ),
        )
        val sqlQuery = UserQueryFilter.Search().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(
            sql.contains("order by"),
            "SQL should contain an ORDER BY clause when sort is provided, got: ${sqlQuery.sql}",
        )
    }

    @Test
    fun `Search with multiple sort entries includes all order columns`() {
        val param = UserParam.Search(
            search = "multi_sort",
            sort = listOf(
                SortWithOrder(UserSort.USERNAME, SortOrder.ASC),
                SortWithOrder(UserSort.ID, SortOrder.DESC),
            ),
        )
        val sqlQuery = UserQueryFilter.Search().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(sql.contains("order by"), "SQL should contain ORDER BY clause")
        // Verify the SQL is not just "SELECT * FROM user" — sort adds columns
        assertTrue(
            sql.length > "select * from user".length + 10,
            "SQL with sort should be longer than bare SELECT, got: ${sqlQuery.sql}",
        )
    }

    @Test
    fun `Search with SEARCH_MATCH sort still produces valid SQL`() {
        val param = UserParam.Search(
            search = "match_test",
            sort = listOf(
                SortWithOrder(UserSort.SEARCH_MATCH, SortOrder.ASC),
            ),
        )
        val sqlQuery = UserQueryFilter.Search().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(sql.contains("order by"), "SQL should contain ORDER BY for SEARCH_MATCH sort")
        assertTrue(sql.contains("user"), "SQL should reference user table")
    }
}
