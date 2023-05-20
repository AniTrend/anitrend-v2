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
