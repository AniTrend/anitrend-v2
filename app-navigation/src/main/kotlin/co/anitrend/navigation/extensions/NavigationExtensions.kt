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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.collection.LruCache
import androidx.core.os.bundleOf
import androidx.work.*
import co.anitrend.navigation.model.NavPayload
import co.anitrend.navigation.model.common.IParam
import co.anitrend.navigation.router.NavigationRouter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

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
        Timber.v("Creating new navigation target class reference for key: $key")
        return Class.forName(key)
    }
}

private inline fun <reified T : Any> Any.castOrNull() = this as? T

private fun Class<*>.forIntent(packageName: String): Intent =
    Intent(Intent.ACTION_VIEW)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .setClassName(packageName, name)

@Throws(ClassNotFoundException::class)
internal fun <T> String.loadClassOrNull(): Class<out T>? =
    classCache.get(this)?.castOrNull()

internal fun String.loadIntentOrNull(packageName: String): Intent? =
    runCatching {
        classCache.get(this)?.run {
            forIntent(packageName)
        }
    }.onFailure {
        Timber.tag("loadIntentOrNull").e(it)
    }.getOrNull()

/**
 * Builds an activity intent from the navigation component
 */
fun NavigationRouter.forActivity(
    context: Context,
    navPayload: NavPayload? = null,
    flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK,
    action: String = Intent.ACTION_VIEW,
): Intent? {
    val intent = provider.activity(context)
    intent?.flags = flags
    intent?.action = action
    navPayload?.also {
        intent?.putExtra(it.key, it.param)
    }
    return intent
}

/**
 * Builds an activity intent and starts it
 */
fun NavigationRouter.startActivity(
    context: Context?,
    navPayload: NavPayload? = null,
    flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK,
    action: String = Intent.ACTION_VIEW,
    options: Bundle? = null
) {
    runCatching {
        val intent = forActivity(
            requireNotNull(context),
            navPayload,
            flags,
            action
        )
        context.startActivity(intent, options)
    }.onFailure {
        Timber.tag(moduleTag).e(it)
    }
}

/**
 * Constructs bundles from [IParam] sub types
 *
 * @return [Bundle]
 */
fun IParam.asBundle() =
    bundleOf(idKey to this)


/**
 * Constructs nav payload from [IParam] sub types
 *
 * @return [NavPayload]
 */
fun IParam.asNavPayload() = NavPayload(idKey, this)


/**
 * [Data.Builder] creator from [IParam] objects, this typically applies to tasks modules
 *
 * @see IParam
 */
fun <T : IParam> T.toDataBuilder(): Data.Builder {
    val map = mapOf(
        idKey to Gson().toJson(this)
    )
    return Data.Builder().putAll(map)
}

/**
 * Object creator of type [IParam] that uses [WorkerParameters] to retrieve data
 *
 * @param contract The key from [IParam.IKey]
 *
 * @see IParam
 */
inline fun <reified T: IParam> WorkerParameters.fromWorkerParameters(contract: IParam.IKey): T {
    val map = inputData.keyValueMap
    return Gson().fromJson(
        map[contract.KEY] as String,
        object : TypeToken<T>(){}.type
    )
}

/**
 * Creates a [OneTimeWorkRequest] for this worker
 *
 * @param context Any valid application context
 * @param params Parameters for this worker
 *
 * @return [WorkContinuation]
 */
fun <T: IParam> Class<out ListenableWorker>.createOneTimeUniqueWorker(
    context: Context,
    params: T
): WorkContinuation {
    val oneTimeRequest = OneTimeWorkRequest.Builder(this)
        .setInputData(params.toDataBuilder().build())
        .build()

    return WorkManager.getInstance(context)
        .beginUniqueWork(
            simpleName,
            ExistingWorkPolicy.KEEP,
            oneTimeRequest
    )
}