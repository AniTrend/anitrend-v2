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

package co.anitrend.domain.thread.enums

import co.anitrend.domain.common.enums.contract.IAliasable
import co.anitrend.domain.common.enums.contract.ISortable

/**
 * Thread sort enums
 */
enum class ThreadSort(override val alias: CharSequence) : IAliasable, ISortable {
    CREATED_AT("Created at"),
    ID("Id"),
    IS_STICKY("Is sticky"),
    REPLIED_AT("Replied at"),
    REPLY_COUNT("Reply count"),
    SEARCH_MATCH("Search match"),
    TITLE("Title"),
    UPDATED_AT("Updated at"),
    VIEW_COUNT("View count")
}