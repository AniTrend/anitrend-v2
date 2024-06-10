package co.anitrend.navigation.extensions

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
inline fun <reified T : IParam> T.asBundle() =
    bundleOf(nameOf<T>() to this)


/**
 * Constructs nav payload from [IParam] sub types
 *
 * @return [NavPayload]
 */
inline fun <reified T : IParam> T.asNavPayload() =
    NavPayload(nameOf<T>(), this)
