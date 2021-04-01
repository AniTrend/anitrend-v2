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

package co.anitrend.data.jikan.media.datasource.local

import androidx.room.*
import co.anitrend.data.android.source.ILocalSource
import co.anitrend.data.jikan.media.entity.JikanEntity
import co.anitrend.data.jikan.media.entity.projection.JikanWithConnection
import kotlinx.coroutines.flow.Flow

@Dao
abstract class JikanLocalSource : ILocalSource<JikanEntity> {

    @Query("""
        select count(id) from jikan
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from jikan
    """)
    abstract override suspend fun clear()

    @Query("""
        delete from jikan
        where id = :id
    """)
    abstract suspend fun clear(id: Long)

    @Query("""
        select * from jikan
        where id = :id
    """)
    abstract fun byIdFlow(
        id: Long
    ): Flow<JikanEntity?>

    @Query("""
        select * from jikan
        where id = :id
    """)
    @Transaction
    abstract fun withConnectionFlow(
        id: Long
    ): Flow<JikanWithConnection?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertAuthors(entities: List<JikanEntity.AuthorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertProducers(entities: List<JikanEntity.ProducerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertLicensors(entities: List<JikanEntity.LicensorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertStudios(entities: List<JikanEntity.StudioEntity>)

    @Transaction
    open suspend fun upsertWithConnections(
        entity: JikanEntity,
        authors: List<JikanEntity.AuthorEntity>,
        producers: List<JikanEntity.ProducerEntity>,
        licensors: List<JikanEntity.LicensorEntity>,
        studios: List<JikanEntity.StudioEntity>
    ) {
        upsert(entity)
        if (authors.isNotEmpty())
            upsertAuthors(authors)
        if (producers.isNotEmpty())
            upsertProducers(producers)
        if (licensors.isNotEmpty())
            upsertLicensors(licensors)
        if (studios.isNotEmpty())
            upsertStudios(studios)
    }
}