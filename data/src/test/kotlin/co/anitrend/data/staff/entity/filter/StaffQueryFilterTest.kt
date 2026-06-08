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
package co.anitrend.data.staff.entity.filter

import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.domain.staff.model.StaffParam
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class StaffQueryFilterTest {

    @Test
    fun `Paged filter produces non-null SQL`() {
        val param = StaffParam.Paged()
        val sqlQuery: SupportSQLiteQuery = StaffQueryFilter.Paged().build(param)

        assertNotNull(sqlQuery, "Built SQL query must not be null")
        assertNotNull(sqlQuery.sql, "SQL string must not be null")
    }

    @Test
    fun `Paged filter SQL includes staff table`() {
        val param = StaffParam.Paged(
            search = "director",
            sort = null,
            isBirthday = null,
        )
        val sqlQuery = StaffQueryFilter.Paged().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(
            sql.contains("staff"),
            "SQL should reference the staff table, got: ${sqlQuery.sql}",
        )
    }

    @Test
    fun `Paged filter SQL is non-empty`() {
        val param = StaffParam.Paged()
        val sqlQuery = StaffQueryFilter.Paged().build(param)
        val sql = sqlQuery.sql

        assertTrue(sql.isNotEmpty(), "SQL string must not be empty")
    }

    @Test
    fun `Paged filter with search param still references staff table`() {
        val param = StaffParam.Paged(search = "Hayao Miyazaki")
        val sqlQuery = StaffQueryFilter.Paged().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(
            sql.contains("staff"),
            "SQL should reference the staff table with search param, got: ${sqlQuery.sql}",
        )

        assertTrue(
            sql.contains(" like "),
            "SQL should apply LIKE predicate for staff search, got: ${sqlQuery.sql}",
        )
    }

    @Test
    fun `Paged filter with isBirthday still references staff table`() {
        val param = StaffParam.Paged(isBirthday = true)
        val sqlQuery = StaffQueryFilter.Paged().build(param)
        val sql = sqlQuery.sql.lowercase()

        assertTrue(
            sql.contains("staff"),
            "SQL should reference the staff table with isBirthday, got: ${sqlQuery.sql}",
        )
    }
}
