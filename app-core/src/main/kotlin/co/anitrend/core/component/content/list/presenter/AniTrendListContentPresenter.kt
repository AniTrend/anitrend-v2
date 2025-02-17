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
package co.anitrend.core.component.content.list.presenter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.ui.fragment.list.contract.ISupportFragmentList
import co.anitrend.arch.ui.fragment.list.presenter.SupportListPresenter
import co.anitrend.arch.ui.view.widget.contract.ISupportStateLayout
import co.anitrend.core.android.binding.IBindingView
import co.anitrend.core.databinding.SharedListContentBinding

/**
 * Custom content list presenter
 */
class AniTrendListContentPresenter<M> :
    SupportListPresenter<M>(),
    IBindingView<SharedListContentBinding> {
    override val recyclerView: RecyclerView
        get() = requireBinding().recyclerView

    override val stateLayout: ISupportStateLayout
        get() = requireBinding().stateLayout

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = requireBinding().swipeRefreshLayout

    override var binding: SharedListContentBinding? = null

    override fun onCreateView(
        fragmentList: ISupportFragmentList<M>,
        view: View?,
    ) {
        binding = SharedListContentBinding.bind(requireNotNull(view))
        super.onCreateView(fragmentList, view)
    }
}
