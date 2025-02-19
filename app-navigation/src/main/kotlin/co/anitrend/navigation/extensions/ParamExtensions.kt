/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.navigation.extensions

import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import co.anitrend.navigation.model.NavPayload
import co.anitrend.navigation.model.common.IParam
import timber.log.Timber

/**
 * Creates a name for parameter arguments
 */
inline fun <reified T : IParam> nameOf(): String =
    (T::class.java.simpleName).also {
        Timber.d("Creating IParam identifier via nameOf<T> -> $it")
    }

/**
 * Constructs bundles from [IParam] sub types
 *
 * @return [Bundle]
 */
inline fun <reified T : IParam> T.asBundle() = bundleOf(nameOf<T>() to this)

/**
 * Constructs types from [Bundle]
 *
 * @return [T]
 */
inline fun <reified T : IParam> Bundle.fromBundle(): T? {
    val name = nameOf<T>()
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getParcelable(name, T::class.java)
        else -> getParcelable(name)
    }
}

/**
 * Constructs nav payload from [IParam] sub types
 *
 * @return [NavPayload]
 */
inline fun <reified T : IParam> T.asNavPayload() = NavPayload(nameOf<T>(), this)
