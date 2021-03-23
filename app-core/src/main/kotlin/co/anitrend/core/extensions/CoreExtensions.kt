/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.core.extensions

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.AniTrendApplication
import co.anitrend.data.arch.AniTrendExperimentalFeature
import timber.log.Timber

/**
 * Text separator character
 */
const val CHARACTER_SEPARATOR: Char = '•'

fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

@AniTrendExperimentalFeature
fun FragmentActivity.recreateModules() {
    runCatching {
        val app = applicationContext as AniTrendApplication
        app.restartDependencyInjection()
    }.onFailure {
        Timber.e(it)
    }
}

/**
 * Prints failures to the logger
 */
fun <T> Result<T>.stackTrace(tag: String): T? {
    onFailure { throwable ->
        Timber.tag(tag).v(throwable)
    }
    return getOrNull()
}

/**
 * Runs [block] if the receiver's context is of type [FragmentActivity]
 */
inline fun Context.runIfActivityContext(
    block: FragmentActivity.() -> Unit
) {
    if (this is FragmentActivity)
        block(this)
    else
        Timber.w("View context is not type of Fragment activity")
}

/**
 * Runs [block] if the receiver's context is of type [FragmentActivity]
 */
inline fun <T : View> T.runIfActivityContext(
    block: FragmentActivity.() -> Unit
) = context.runIfActivityContext(block)