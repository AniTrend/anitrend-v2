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
package co.anitrend.data.staff.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.staff.converter.StaffModelConverter
import co.anitrend.data.staff.datasource.local.StaffLocalSource
import co.anitrend.data.staff.entity.StaffEntity
import co.anitrend.data.staff.model.container.StaffModelContainer
import timber.log.Timber

internal sealed class StaffMapper<S, D> : DefaultMapper<S, D>() {
    class Paged(
        private val localSource: StaffLocalSource,
        private val converter: StaffModelConverter,
    ) : StaffMapper<StaffModelContainer.Paged, List<StaffEntity>>() {
        override suspend fun persist(data: List<StaffEntity>) {
            localSource.upsert(data)
        }

        override suspend fun onResponseMapFrom(source: StaffModelContainer.Paged): List<StaffEntity> =
            try {
                converter.convertFrom(source.page.staff)
            } catch (throwable: Throwable) {
                Timber.w(throwable, "Failed to map staff models to entities")
                emptyList()
            }
    }
}
