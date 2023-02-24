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

package co.anitrend.core.component.content.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.extension.ext.getColorFromAttr
import co.anitrend.arch.extension.network.contract.ISupportConnectivity
import co.anitrend.arch.extension.network.model.ConnectivityState
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.ui.fragment.list.SupportFragmentList
import co.anitrend.arch.ui.fragment.list.presenter.SupportListPresenter
import co.anitrend.core.R
import co.anitrend.core.android.koinOf
import co.anitrend.core.component.adapter.AniTrendLoadStateAdapter
import co.anitrend.core.component.content.list.presenter.AniTrendListContentPresenter
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.scope.fragmentScope
import org.koin.core.component.KoinScopeComponent
import timber.log.Timber

abstract class AniTrendListContent<M>(
    override val inflateLayout: Int = R.layout.shared_list_content,
    override val listPresenter: SupportListPresenter<M> = AniTrendListContentPresenter()
) : SupportFragmentList<M>(), KoinScopeComponent {

    override val scope by fragmentScope()

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            val header = AniTrendLoadStateAdapter(resources, stateConfig).apply {
                registerFlowListener()
            }
            val footer = AniTrendLoadStateAdapter(resources, stateConfig).apply {
                registerFlowListener()
            }

            if (supportViewAdapter is RecyclerView.Adapter<*>) {
                (supportViewAdapter as RecyclerView.Adapter<*>)
                    .stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            recyclerView.adapter = supportViewAdapter.withLoadStateHeaderAndFooter(
                header = header, footer = footer
            )
        }
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        super.initializeComponents(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            koinOf<ISupportConnectivity>().connectivityStateFlow
                .onEach { state ->
                    Timber.v("Connectivity state changed: $state")
                    if (state == ConnectivityState.Connected) viewModelState()?.retry()
                }
                .catch { cause ->
                    Timber.w(cause, "While collecting connectivity state")
                }
                .collect()
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation). This will be called between
     * [onCreate] and [onActivityCreated].
     *
     * If you return a View from here, you will later be called in
     * [onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the [View] for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        listPresenter.swipeRefreshLayout?.setColorSchemeColors(
            requireContext().getColorFromAttr(R.attr.colorPrimary),
            requireContext().getColorFromAttr(R.attr.colorAccent)
        )
        return view
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): AniTrendViewModelState<*>? = null
}