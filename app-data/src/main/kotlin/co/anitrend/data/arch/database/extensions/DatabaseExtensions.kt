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

package co.anitrend.data.arch.database.extensions

import androidx.room.RoomDatabase
import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.arch.extension.koinOf
import kotlin.jvm.Throws

internal fun List<*>.toCommaSeparatedValues(): String {
    return if (isNotEmpty()) {
        joinToString(separator = ",")
    } else
        String.empty()
}

internal fun String.fromCommaSeparatedValues(): List<String> {
    return if (isNotBlank())
        split(',')
    else
        emptyList()
}

internal inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]

internal inline fun <reified T: Enum<*>> String.toEnum(): T {
    val `class` = T::class.java
    return `class`.enumConstants?.first { it.name == this }!!
}

internal fun Enum<*>.fromEnum() = name

@Throws(Exception::class)
@Suppress("DEPRECATION")
internal inline fun runInTransaction(action: () -> Unit) {
    val store = koinOf<IAniTrendStore>() as RoomDatabase
    store.beginTransaction()
    try {
        action()
        store.setTransactionSuccessful()
    } catch (exception: Exception) {
        throw exception
    } finally {
        store.endTransaction()
    }
}