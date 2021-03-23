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
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.use
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import androidx.core.view.iterator
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.ui.view.widget.SupportStateLayout
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.navigation.model.common.IParam
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.Instant
import timber.log.Timber
import java.util.*

/**
 * Helper to resolve koin dependencies
 *
 * @param qualifier Help qualify a component
 * @param parameters Help define a DefinitionParameters
 *
 * @return [T]
 */
inline fun <reified T> koinOf(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = GlobalContext.get()
    return koin.get(qualifier, parameters)
}

/**
 * Retrieve a style from the current [android.content.res.Resources.Theme].
 */
@StyleRes
fun Context.themeStyle(@AttrRes attributeResource: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(
        attributeResource,
        typedValue,
        true
    )
    return typedValue.data
}

fun Context.themeInterpolator(
    @AttrRes attributeResource: Int,
    @InterpolatorRes interpolator: Int
): Interpolator {
    return AnimationUtils.loadInterpolator(
        this,
        obtainStyledAttributes(intArrayOf(attributeResource)).use {
            it.getResourceId(0, interpolator)
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
        loadStateMutableStateFlow.value = LoadState.Error(
            RequestError(
                topic = context.getString(R.string.app_controller_heading_missing_param),
                description = context.getString(R.string.app_controller_message_missing_param),
            )
        )
    else block()
}

/**
 * Add [menu] items to [this] menu
 *
 * @param menu Menu containing items to add
 * @param group Group that [menu] belongs to
 * @param flagFor Delegate to dictate which action flags are applied
 */
inline fun Menu.addItems(
    menu: Menu,
    group: Int = Menu.NONE,
    flagFor: (MenuItem) -> Int = { MenuItem.SHOW_AS_ACTION_IF_ROOM }
) {
    menu.iterator().forEach { item ->
        add(group, item.itemId, item.order, item.title)
            .setIcon(item.icon)
            .setShowAsActionFlags(
                flagFor(item)
            )
    }
}

/**
 * Remove [menu] items from [this] menu
 *
 * @param menu Menu containing items to remove
 * @param group Group that [menu] belongs to
 */
fun Menu.removeItems(
    menu: Menu,
    group: Int = Menu.NONE
) {
    menu.iterator().forEach { item ->
        removeItem(item.itemId)
    }
    if (group != Menu.NONE)
        removeGroup(group)
}


/**
 * Change visibility of all items for [this] menu
 *
 * @param shouldShow Visibility
 * @param filter Predicate for which menu items to hid
 */
fun Menu.setVisibilityForAllItems(
    shouldShow: Boolean,
    filter: (MenuItem) -> Boolean
) {
    iterator().asSequence()
        .filter(filter)
        .forEach {
            it.isVisible = shouldShow
        }
}

fun View.startViewIntent(url: String) {
    val intent = Intent().apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        action = Intent.ACTION_VIEW
        data = url.toUri()
    }
    runCatching {
        context.startActivity(intent)
    }.onFailure { Timber.w(it) }
}


/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * Also images loaded from this method apply the [Drawable.mutate] to assure
 * that the state of each drawable is not shared
 *
 * @param resource The resource id of the drawable or vector drawable
 * @param colorTint A specific color to tint the drawable
 *
 * @return [Drawable] tinted with the tint color
 *
 * @see Drawable
 * @see DrawableRes
 *
 * TODO: Merge this into support-arch
 */
fun Context.getCompatDrawable(@DrawableRes resource : Int, @ColorInt colorTint : Int): Drawable? {
    val drawableResource = AppCompatResources.getDrawable(this, resource)
    if (drawableResource != null) {
        val drawableResult = DrawableCompat.wrap(drawableResource).mutate()
        if (colorTint != 0)
            DrawableCompat.setTint(drawableResult, colorTint)
        return drawableResource
    }
    return null
}