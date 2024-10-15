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
import android.content.Intent
import android.os.Bundle
import android.view.View
import co.anitrend.navigation.model.NavPayload
import co.anitrend.navigation.router.NavigationRouter
import timber.log.Timber

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
    options: Bundle? = null,
) {
    runCatching {
        val intent =
            forActivity(
                requireNotNull(context),
                navPayload,
                flags,
                action,
            )
        context.startActivity(intent, options)
    }.onFailure {
        Timber.tag(moduleTag).e(it)
    }
}

/**
 * Builds an activity intent and starts it
 */
fun NavigationRouter.startActivity(
    view: View?,
    navPayload: NavPayload? = null,
    flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK,
    action: String = Intent.ACTION_VIEW,
    options: Bundle? = null,
) = startActivity(view?.context, navPayload, flags, action, options)
