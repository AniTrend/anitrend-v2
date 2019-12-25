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

package co.anitrend.navigation.extensions

import android.content.Intent
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.navigation.contract.INavigationTarget
import timber.log.Timber


private const val APPLICATION_PACKAGE_NAME = "co.anitrend"

private val classMap = mutableMapOf<String, Class<*>>()

private inline fun <reified T : Any> Any.castOrNull() = this as? T

private fun forIntent(className: String): Intent =
    Intent(Intent.ACTION_VIEW)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .setClassName(APPLICATION_PACKAGE_NAME, className)

internal fun String.loadIntentOrNull(): Intent? =
    try {
        Class.forName(this).run {
            forIntent(this@loadIntentOrNull)
        }
    } catch (e: ClassNotFoundException) {
        Timber.tag("loadIntentOrNull").e(e)
        null
    }

@Throws(ClassNotFoundException::class)
internal fun <T> String.loadClassOrNull(): Class<out T>? =
    classMap.getOrPut(this) {
        Class.forName(this)
    }.castOrNull()

internal fun <T : SupportFragment<*, *, *>> String.loadFragmentOrNull(): T? =
    try {
        this.loadClassOrNull<T>()?.newInstance()
    } catch (e: ClassNotFoundException) {
        Timber.tag("loadFragmentOrNull").e(e)
        null
    }

/**
 * Builds a an intent path for the target
 */
fun INavigationTarget.forIntent(): Intent? {
    return "$APPLICATION_PACKAGE_NAME.$packageName.$className".loadIntentOrNull()
}

fun INavigationTarget.forFragment(): SupportFragment<*, *, *>? {
    return forIntent()?.component?.className?.loadFragmentOrNull()
}