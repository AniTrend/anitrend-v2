/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.android.source

import androidx.room.*

abstract class AbstractLocalSource<T> {
    abstract suspend fun count(): Int
    abstract suspend fun clear()

    /**
     * Inserts a new item into the database ignoring items with the same primary key,
     * for both insert or update behavior use: [upsert]
     *
     * @param attribute item/s to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(attribute: T): Long

    /**
     * Inserts new items into the database ignoring items with the same primary key,
     * for both insert or update behavior use: [upsert]
     *
     * @param attribute item/s to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(attribute: List<T>): List<Long>

    /**
     * Updates an item in the underlying database
     *
     * @param attribute item/s to update
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(attribute: T)

    /**
     * Updates a list of items in the underlying database
     *
     * @param attribute item/s to update
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(attribute: List<T>)

    /**
     * Deletes an item from the underlying database
     *
     * @param attribute item/s to delete
     */
    @Delete
    abstract suspend fun delete(attribute: T)

    /**
     * Deletes a list of items from the underlying database
     *
     * @param attribute item/s to delete
     */
    @Delete
    abstract suspend fun delete(attribute: List<T>)

    /**
     * Inserts or updates matching attributes on conflict
     *
     * @param attribute item/s to insert
     */
    @Upsert
    abstract suspend fun upsert(attribute: T)

    /**
     * Inserts or updates matching attributes on conflict
     *
     * @param attribute item/s to insert
     */
    @Upsert
    abstract suspend fun upsert(attribute: List<T>)
}