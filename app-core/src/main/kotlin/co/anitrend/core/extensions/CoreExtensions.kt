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

package co.anitrend.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.UriCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.anitrend.core.AniTrendApplication
import co.anitrend.core.android.extensions.Keys
import co.anitrend.core.android.extensions.Tags
import co.anitrend.core.android.extensions.analytics
import co.anitrend.core.android.koinOf
import co.anitrend.core.component.viewmodel.AniTrendViewModelState
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import timber.log.Timber

/**
 * Text separator character
 */
const val CHARACTER_SEPARATOR: Char = '•'

fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

fun FragmentActivity.recreateModules() {
    runCatching {
        val app = applicationContext as AniTrendApplication
        app.restartDependencyInjection()
    }.onFailure {
        Timber.e(it)
    }
}

/**
 * Prints failures to the logger
 */
fun <T> Result<T>.stackTrace(): T? {
    onFailure { throwable ->
        Timber.w(throwable)
    }
    return getOrNull()
}

/**
 * Runs [block] if the receiver's context is of type [FragmentActivity]
 */
inline fun Context.runIfActivityContext(
    block: FragmentActivity.() -> Unit
) {
    if (this is FragmentActivity)
        block(this)
    else
        Timber.w("$this context is not type of Fragment activity")
}

/**
 * Runs [block] if the receiver's context is of type [FragmentActivity]
 */
inline fun <T : View> T.runIfActivityContext(
    block: FragmentActivity.() -> Unit
) = context.runIfActivityContext(block)

/**
 * Hooks [ViewModel.viewModelScope] context to [states]
 */
fun ViewModel.hook(vararg states: AniTrendViewModelState<*>) {
    val coroutineContext = viewModelScope.coroutineContext
    states.forEach { it.context = coroutineContext }
}

/**
 * Starts a view intent action given the [uri] as data
 */
fun Context.startViewIntent(uri: Uri) {
    val intent = Intent().apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        action = Intent.ACTION_VIEW
        data = uri
    }
    Timber.analytics {
        logCurrentState(
            tag = Tags.ACTION_PREFIX.plus("start_view_intent"),
            bundle = bundleOf(
                Keys.DATA to UriCompat.toSafeString(uri)
            )
        )
    }
    runCatching {
        startActivity(intent)
    }.stackTrace()
}

/**
 * Start custom tabs or standard intent launcher for matching resolver
 *
 * @param url
 */
fun View.handleViewIntent(url: String) {
    val uri = url.toUri()
    Timber.analytics {
        logCurrentState(
            tag = Tags.ACTION_PREFIX.plus("handle_view_intent"),
            bundle = bundleOf(
                Keys.DATA to UriCompat.toSafeString(uri)
            )
        )
    }
    if (url.startsWith("https://img1.ak.crunchyroll")) {
        ViewCompat.setTransitionName(this, url)
        ImageViewerRouter.startActivity(
            context = context,
            navPayload = ImageViewerRouter.Param(url).asNavPayload()
        )
    } else {
        runCatching {
            val customTabs = koinOf<CustomTabsIntent.Builder>().build()
            customTabs.launchUrl(context, uri)
        }.onFailure {
            Timber.w(it, "Unable to open url with custom tabs, using default")
            context.startViewIntent(uri)
        }
    }
}