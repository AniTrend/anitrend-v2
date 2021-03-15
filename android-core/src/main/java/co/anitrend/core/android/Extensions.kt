/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.core.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.res.use
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.view.widget.SupportStateLayout
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.navigation.model.common.IParam
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.context.GlobalContext
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.Instant
import java.util.*

/**
 * Helper to resolve koin dependencies
 */
inline fun <reified T> koinOf(): T =
    GlobalContext.get().get()

/**
 * Retrieve a style from the current [android.content.res.Resources.Theme].
 */
@StyleRes
fun Context.themeStyle(@AttrRes attr: Int): Int {
    val tv = TypedValue()
    theme.resolveAttribute(attr, tv, true)
    return tv.data
}

fun Context.themeInterpolator(@AttrRes attr: Int): Interpolator {
    return AnimationUtils.loadInterpolator(
        this,
        obtainStyledAttributes(intArrayOf(attr)).use {
            it.getResourceId(0, android.R.interpolator.fast_out_slow_in)
        }
    )
}

/**
 * Creates a callback flow a [BroadcastReceiver] using the given [IntentFilter]
 *
 * @param intentFilter The intent to subscribe to
 *
 * @return [Flow] of [Intent]
 */
fun Context.flowOfBroadcast(
    intentFilter: IntentFilter
): Flow<Intent> = callbackFlow {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            offer(intent)
        }
    }
    registerReceiver(receiver, intentFilter)
    awaitClose {
        unregisterReceiver(receiver)
    }
}

/**
 * Helper value formatter
 *
 * @param digits Number of decimal places
 *
 * @author [Andrey Breslav](https://stackoverflow.com/a/23088000/1725347)
 */
fun Double.format(digits: Int) = "%.${digits}f".format(this)

/**
 * Helper value formatter for [Float] types
 *
 * @param digits Number of decimal places
 */
fun Float.format(digits: Int) = "%.${digits}f".format(this)

/**
 * Creates a string with locale applied from pretty time
 *
 * @return 2 hours from now, 3 hours ago .e.t.c
 */
fun AiringSchedule.asPrettyTime(): String {
    val prettyTime = koinOf<PrettyTime>()
    return prettyTime.format(
        Date(airingAt * 1000)
    )
}

/**
 * Creates a string with locale applied from pretty time
 *
 * @return 2 hours from now, 3 hours ago .e.t.c
 */
fun Instant.asPrettyTime(): String {
    val prettyTime = koinOf<PrettyTime>()
    return prettyTime.format(Date(toEpochMilli()))
}

/**
 * Displays an error message for missing parameters otherwise runs [block]
 */
inline fun SupportStateLayout.assureParamNotMissing(param: IParam?, block: () -> Unit) {
    if (param == null)
        networkMutableStateFlow.value = NetworkState.Error(
            heading = context.getString(R.string.app_controller_heading_missing_param),
            message = context.getString(R.string.app_controller_message_missing_param),
        )
    else block()
}