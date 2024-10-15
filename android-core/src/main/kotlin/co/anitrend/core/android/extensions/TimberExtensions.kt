/*
 * Copyright (C) 2022 AniTrend
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
package co.anitrend.core.android.extensions

import co.anitrend.arch.analytics.contract.ISupportAnalytics
import timber.log.Timber.Forest

private const val UNDEFINED_INDEX = -1
private var treeSize = UNDEFINED_INDEX
private var analyticsIndex = UNDEFINED_INDEX

object Tags {
    private const val ACTION_PREFIX = "action_"
    private const val VIEW_PREFIX = "view_"
    private const val STATE_PREFIX = "state_"

    fun action(postfix: String) = ACTION_PREFIX.plus(postfix)

    fun view(postfix: String) = VIEW_PREFIX.plus(postfix)

    fun state(postfix: String) = STATE_PREFIX.plus(postfix)
}

object Keys {
    const val DATA = "data"
    const val EVENT = "event"
    const val SOURCE = "source"
}

private inline fun <reified T> Forest.indexOf(): Int {
    return forest().indexOfFirst { it is T }
}

val Forest.tags get() = Tags
val Forest.keys get() = Keys

fun Forest.analytics(action: ISupportAnalytics.() -> Unit) {
    if (analyticsIndex == UNDEFINED_INDEX || treeSize != treeCount) {
        treeSize = treeCount
        analyticsIndex = indexOf<ISupportAnalytics>()
    }

    val tree = forest().getOrNull(analyticsIndex)

    val analytics = tree as? ISupportAnalytics
    analytics?.also { action(it) }
}
