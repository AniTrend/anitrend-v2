/*
 * Copyright (C) 2024 AniTrend
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
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkContinuation
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import co.anitrend.navigation.model.common.IParam
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * [Data.Builder] creator from [IParam] objects, this typically applies to tasks modules
 *
 * @see IParam
 */
inline fun <reified T : IParam> T.toDataBuilder(): Data.Builder {
    val map =
        mapOf(
            T::class.java.simpleName to Gson().toJson(this),
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
inline fun <reified T : IParam> WorkerParameters.fromWorkerParameters(): T {
    val map = inputData.keyValueMap
    return Gson().fromJson(
        map[T::class.java.simpleName] as String,
        object : TypeToken<T>() {}.type,
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
inline fun <reified T : IParam> Class<out ListenableWorker>.createOneTimeUniqueWorker(
    context: Context,
    params: T? = null,
): WorkContinuation {
    val oneTimeRequestBuilder = OneTimeWorkRequest.Builder(this)

    if (params != null) {
        oneTimeRequestBuilder.setInputData(
            params.toDataBuilder()
                .build(),
        )
    }

    return WorkManager.getInstance(context)
        .beginUniqueWork(
            simpleName,
            ExistingWorkPolicy.KEEP,
            oneTimeRequestBuilder.build(),
        )
}

inline fun <reified R : IParam, P> WorkerParameters.transform(crossinline transformer: (R) -> P): Lazy<P> =
    lazy(LazyThreadSafetyMode.NONE) {
        fromWorkerParameters<R>().let(transformer)
    }
