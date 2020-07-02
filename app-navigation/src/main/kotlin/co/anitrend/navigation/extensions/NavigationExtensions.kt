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
import androidx.collection.LruCache
import androidx.fragment.app.Fragment
import co.anitrend.navigation.contract.NavigationComponent
import timber.log.Timber


private const val moduleTag = "NavigationExtensions"

private val classCache = object : LruCache<String, Class<*>>(8) {
    /**
     * Called after a cache miss to compute a value for the corresponding key.
     * Returns the computed value or null if no value can be computed. The
     * default implementation returns null.
     *
     * The method is called without synchronization: other threads may
     * access the cache while this method is executing.
     *
     * If a value for [key] exists in the cache when this method
     * returns, the created value will be released with [entryRemoved]
     * and discarded. This can occur when multiple threads request the same key
     * at the same time (causing multiple values to be created), or when one
     * thread calls [put] while another is creating a value for the same
     * key.
     */
    override fun create(key: String): Class<*>? {
        Timber.tag(moduleTag).v(
            "Creating new navigation target class reference for key: $key"
        )
        return Class.forName(key)
    }
}

private inline fun <reified T : Any> Any.castOrNull() = this as? T

private fun Class<*>.forIntent(
    packageName: String
): Intent =
    Intent(Intent.ACTION_VIEW)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .setClassName(packageName, name)

@Throws(ClassNotFoundException::class)
internal fun <T> String.loadClassOrNull(): Class<out T>? {
    return classCache.get(this)?.castOrNull()
}

internal fun <T> String.loadOrNull(): T? =
    try {
        loadClassOrNull<T>()?.newInstance()
    } catch (e: ClassNotFoundException) {
        Timber.tag(moduleTag).e(e)
        null
    }

/**
 * Builds an intent path for the navigation component target
 */
internal fun NavigationComponent.loadIntentOrNull(): Intent? =
    try {
        val packagePath = "$corePackage.$packageName"
        val classPath = "$packagePath.$className"
        val `class` = classPath.loadClassOrNull<Any>()
        `class`?.forIntent(packagePath)
    } catch (e: ClassNotFoundException) {
        Timber.tag(moduleTag).e(e)
        null
    }

/**
 * Build fragment class from the navigation component
 */
fun NavigationComponent.forFragment() =
    loadIntentOrNull()?.component?.className
        ?.loadClassOrNull<Fragment>()

/**
 * Creates a new instance of the given type [T]. Assure that the
 * required type is **constructorless**
 *
 * Suggested use would make heavy use of interfaces on base types
 */
fun <T> NavigationComponent.forInstance() =
    loadIntentOrNull()?.component?.className
        ?.loadOrNull<T>()