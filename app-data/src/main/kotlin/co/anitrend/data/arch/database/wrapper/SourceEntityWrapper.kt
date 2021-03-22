/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.arch.database.wrapper

import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.shared.common.Identity

internal sealed class SourceEntityWrapper<T : Identity> {
    protected abstract val source: ILocalSource<T>

    abstract suspend fun save()
    abstract suspend fun upsert()
    abstract suspend fun update()
    abstract suspend fun delete()

    data class Single<T : Identity>(
        override val source: ILocalSource<T>,
        private val entity: T
    ) : SourceEntityWrapper<T>() {
        override suspend fun save() = source.insert(entity)
        override suspend fun upsert() = source.upsert(entity)
        override suspend fun update() = source.update(entity)
        override suspend fun delete() = source.delete(entity)
    }

    data class Many<T : Identity>(
        override val source: ILocalSource<T>,
        private val entities: List<T>
    ) : SourceEntityWrapper<T>() {
        override suspend fun save() = source.insert(entities)
        override suspend fun upsert() = source.upsert(entities)
        override suspend fun update() = source.update(entities)
        override suspend fun delete() = source.delete(entities)
        suspend fun count() = source.count()
    }
}