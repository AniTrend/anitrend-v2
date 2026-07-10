/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.staff.source.contract

import androidx.paging.PagingData
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.domain.staff.model.StaffParam
import kotlinx.coroutines.flow.Flow

internal class StaffSource {
    abstract class Paging : AbstractCoreDataSource() {
        protected lateinit var query: StaffParam.Paged

        abstract operator fun invoke(param: StaffParam.Paged): Flow<PagingData<Staff>>

        protected fun assignQuery(param: StaffParam.Paged) {
            query = param
        }
    }
}
