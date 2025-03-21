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
package co.anitrend.common.shared.ui.extension

import androidx.recyclerview.widget.RecyclerView
import co.anitrend.core.android.recycler.CarouselRecycler

/**
 * Sets up a recycler view by handling all the boilerplate code associated with it using
 * the given layout manager or the default.
 *
 * @param supportAdapter recycler view adapter which will be used
 */
fun CarouselRecycler.setUpWith(
    supportAdapter: RecyclerView.Adapter<*>,
    recyclerViewPool: RecyclerView.RecycledViewPool?,
) {
    supportAdapter.stateRestorationPolicy =
        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    setHasFixedSize(true)
    setRecycledViewPool(recyclerViewPool)
    isNestedScrollingEnabled = true
    adapter = supportAdapter
}
