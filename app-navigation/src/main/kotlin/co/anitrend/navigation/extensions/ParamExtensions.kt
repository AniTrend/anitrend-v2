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
fun IParam.asBundle() =
    bundleOf(idKey to this)


/**
 * Constructs nav payload from [IParam] sub types
 *
 * @return [NavPayload]
 */
fun IParam.asNavPayload() = NavPayload(idKey, this)
