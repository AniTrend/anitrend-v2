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

package co.anitrend.data.android.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.source.ILocalSource

abstract class EmbedMapper<S, D> : DefaultMapper<List<S>, List<D>>() {

    protected var entities: List<D>? = null
    protected abstract val localSource: ILocalSource<D>
    protected abstract val converter: SupportConverter<S, D>

    open suspend fun onEmbedded(source: List<S>) {
        entities = onResponseMapFrom(source)
    }

    open suspend fun onEmbedded(source: S?) {
        if (source != null)
            entities = onResponseMapFrom(listOf(source))
    }

    open suspend fun persistEmbedded() {
        onResponseDatabaseInsert(entities.orEmpty())
        entities = null
    }

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: List<D>) {
        localSource.upsert(data)
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: List<S>): List<D> {
        return converter.convertFrom(source)
    }
}