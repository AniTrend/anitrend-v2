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

package co.anitrend.data.character.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.AbstractLocalSource
import co.anitrend.data.character.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class CharacterLocalSource : AbstractLocalSource<CharacterEntity>() {

    @Query("""
        select count(id) from character
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from character
    """)
    abstract override suspend fun clear()

    @Query("""
        delete from character
        where id = :id
        """)
    abstract suspend fun clearById(id: Long)

    @Query("""
        select * from character
        where id = :id
    """)
    abstract suspend fun characterById(id: Long): CharacterEntity

    @Query("""
        select * from character
        where id = :id
    """)
    abstract fun characterByIdFlow(id: Long): Flow<CharacterEntity?>

    @Query("""
        select * from character
    """)
    abstract fun allCharacterFactory(): DataSource.Factory<Int, CharacterEntity>

    @Query("""
        select * from character
        where name_first match :term 
        or name_full match :term 
        or name_last match :term 
        or name_original match :term
        or name_alternative match :term
    """)
    abstract fun searchCharacterFactory(
        term: String
    ): DataSource.Factory<Int, CharacterEntity>
}