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
package co.anitrend.core.android.settings.extensions

import android.annotation.SuppressLint
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.extension.settings.contract.AbstractSetting
import co.anitrend.data.settings.customize.common.PreferredViewMode

/**
 * Updates [RecyclerView.getLayoutManager] span count and configuration
 * when [this] value emits, using [resolver] for configuration
 *
 * @param recyclerView Current recycler view to update
 * @param resolver [IntegerRes] resolver for [this]
 */
suspend fun AbstractSetting<PreferredViewMode>.flowUpdating(
    recyclerView: RecyclerView?,
    resolver: (PreferredViewMode) -> Int,
) {
    flow.collect { viewMode ->
        requireNotNull(recyclerView)
        val adapter = recyclerView.adapter as RecyclerView.Adapter<*>
        val newSpanCount =
            recyclerView.resources.getInteger(
                resolver(viewMode),
            )

        @SuppressLint("NotifyDataSetChanged")
        when (val layoutManager = recyclerView.layoutManager) {
            is StaggeredGridLayoutManager -> {
                val currentSpanCount = layoutManager.spanCount
                if (currentSpanCount != newSpanCount) {
                    layoutManager.spanCount = newSpanCount
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
            is GridLayoutManager -> {
                val currentSpanCount = layoutManager.spanCount
                if (currentSpanCount != newSpanCount) {
                    layoutManager.spanCount = newSpanCount
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}
