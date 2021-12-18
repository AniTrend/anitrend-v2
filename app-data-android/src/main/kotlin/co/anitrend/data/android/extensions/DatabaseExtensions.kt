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

package co.anitrend.data.android.extensions

import androidx.room.RoomDatabase
import co.anitrend.data.core.extensions.koinOf
import timber.log.Timber
import kotlin.jvm.Throws

@Throws(Exception::class)
@Deprecated(
    message = "Highly error prone, rather use the @Transaction annotation",
    replaceWith = ReplaceWith(""),
    level = DeprecationLevel.ERROR
)
@Suppress("DEPRECATION")
inline fun runInTransaction(action: () -> Unit) {
    val store = koinOf<RoomDatabase>()
    try {
        store.beginTransaction()
        action()
        store.setTransactionSuccessful()
    } catch (exception: Exception) {
        Timber.e(exception)
    } finally {
        store.endTransaction()
    }
    action()
}