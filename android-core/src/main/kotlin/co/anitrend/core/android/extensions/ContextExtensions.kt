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
package co.anitrend.core.android.extensions

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import timber.log.Timber

/**
 * Resolve [FragmentManager] from any context
 *
 * @throws NotImplementedError when a context type cannot be handled
 */
@Throws(NotImplementedError::class)
fun Context.fragmentManager() =
    when (this) {
        is FragmentActivity -> supportFragmentManager
        is ContextWrapper -> (baseContext as FragmentActivity).supportFragmentManager
        else -> throw NotImplementedError("This type of context: $this is not handled/supported")
    }

@Throws(IllegalArgumentException::class)
fun Context.requireLifecycleOwner(): LifecycleOwner {
    return when (val context = this) {
        is LifecycleOwner -> context
        else -> throw IllegalArgumentException("$context is not a lifecycle owner")
    }
}

fun Context.lifecycleOwner(): LifecycleOwner? {
    return runCatching(::requireLifecycleOwner)
        .getOrElse {
            Timber.w(it)
            null
        }
}

fun Context.lifecycleScope(): LifecycleCoroutineScope? {
    val lifecycleOwner = lifecycleOwner()
    return lifecycleOwner?.lifecycleScope
}
