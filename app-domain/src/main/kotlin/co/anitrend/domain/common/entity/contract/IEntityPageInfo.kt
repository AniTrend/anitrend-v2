/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.domain.common.entity.contract

/** PageInfo contract
 *
 * @property currentPage The current page
 * @property hasNextPage If there is another page
 * @property lastPage The last page
 * @property perPage The count on a page
 * @property total The total number of items
 */
interface IEntityPageInfo {
    val currentPage: Int
    val hasNextPage: Boolean
    val lastPage: Int
    val perPage: Int
    val total: Int
}