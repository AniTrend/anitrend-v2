/*
 * Copyright (C) 2021 AniTrend
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

import android.content.Intent
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.TooltipCompat
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import timber.log.Timber

fun View.startViewIntent(url: String) {
    val intent =
        Intent().apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            action = Intent.ACTION_VIEW
            data = url.toUri()
        }
    runCatching {
        context.startActivity(intent)
    }.onFailure { Timber.w(it) }
}

/**
 * Sets the tooltip text for the view.
 *
 * @param tooltipText the tooltip text
 */
fun View.setTooltip(tooltipText: String) {
    TooltipCompat.setTooltipText(this, tooltipText)
}

/**
 * Sets the tooltip text for the view.
 *
 * @param tooltipText the tooltip text
 */
fun View.setTooltip(
    @StringRes tooltipText: Int,
) {
    setTooltip(context.getString(tooltipText))
}

/**
 * Temporary work around for nested scrolling inside a bottom sheet for [ViewPager2]
 *
 * @see co.anitrend.core.android.widget.viewpager.BottomSheetViewPager
 */
fun ViewPager2.enableBottomSheetScrolling() {
    children.find {
        it is RecyclerView
    }?.let { it.isNestedScrollingEnabled = false }
}

/**
 * Get fragment support manager from a view
 *
 * @see Context.fragmentManager
 */
fun View.fragmentManager(): FragmentManager {
    return context.fragmentManager()
}
