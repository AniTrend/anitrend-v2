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

package co.anitrend.data.customscore.datasource

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.AbstractLocalSource
import co.anitrend.data.customscore.entity.CustomScoreEntity

@Dao
internal abstract class CustomScoreLocalSource : AbstractLocalSource<CustomScoreEntity>() {

    @Query("""
        select count(id) from custom_score
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from custom_score
    """)
    abstract override suspend fun clear()

    @Query("""
        delete from custom_score
        where score_name = :scoreName
    """)
    abstract suspend fun clear(
        scoreName: String
    )
}