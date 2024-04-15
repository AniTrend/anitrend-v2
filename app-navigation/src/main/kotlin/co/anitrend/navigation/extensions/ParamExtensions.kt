package co.anitrend.navigation.extensions

import android.os.Bundle
import androidx.core.os.bundleOf
import co.anitrend.navigation.model.NavPayload
import co.anitrend.navigation.model.common.IParam


/**
 * Constructs bundles from [IParam] sub types
 *
 * @return [Bundle]
 */
inline fun <reified T : IParam> T.asBundle() =
    bundleOf(T::class.java.simpleName to this)


/**
 * Constructs nav payload from [IParam] sub types
 *
 * @return [NavPayload]
 */
inline fun <reified T : IParam> T.asNavPayload() =
    NavPayload(T::class.java.simpleName, this)
