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
package co.anitrend.core.component.adapter

import android.content.res.Resources
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter

internal class AniTrendLoadStateAdapter(
    resources: Resources,
    stateConfiguration: IStateLayoutConfig,
) : SupportLoadStateAdapter(resources, stateConfiguration) {
    /**
     * Returns true if the LoadState should be displayed as a list item when active.
     *
     * By default, [LoadState.Loading] and [LoadState.Error] present as list items, others do not.
     */
    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState) || loadState is LoadState.Success
    }
}
