/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.core.component.content.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.attachComponent
import co.anitrend.arch.extension.ext.detachComponent
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.paging.legacy.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.extensions.isEmpty
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter
import co.anitrend.arch.ui.fragment.list.contract.ISupportFragmentList
import co.anitrend.core.android.recycler.decorator.DefaultSpacingDecorator
import co.anitrend.core.component.content.AniTrendContent
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class AniTrendSelectionContent<B : ViewBinding, M> :
    AniTrendContent<B>(), ISupportFragmentList<M> {

    protected abstract val bindingMapper: (View) -> B

    override val onRefreshObserver = Observer<LoadState> {

    }

    override val onNetworkObserver = Observer<LoadState> {
        changeLayoutState(it)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun ISupportAdapter<*>.registerFlowListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                clickableFlow
                    .debounce(16)
                    .filterIsInstance<ClickableItem.State>()
                    .onEach {
                        if (it.state !is LoadState.Loading)
                            viewModelState()?.retry()
                        else
                            Timber.d("retry -> state is loading? current state: ${it.state}")
                    }.collect()
            }
        }
    }

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onResume] state
     *
     * @see initializeComponents
     */
    abstract fun onFetchDataInitialize()

    /**
     * Configures recycler with an adapter, layout manager and registers it for lifecycle events
     *
     * @see setRecyclerAdapter
     * @see setRecyclerLayoutManager
     * @see attachComponent
     */
    protected fun SupportRecyclerView.configure() {
        setRecyclerAdapter(this)
        setRecyclerLayoutManager(this)
        attachComponent(this)
    }

    /**
     * Handles post view model result after extraction or processing
     *
     * @param model list holding data
     */
    protected open fun onPostModelChange(model: Collection<M>?) {
        /** Since pagedList is a type of list we check it first */
        when (model) {
            is PagedList -> {
                with (supportViewAdapter as SupportPagedListAdapter) {
                    submitList(model)
                }
            }
            is List -> {
                with (supportViewAdapter as SupportListAdapter) {
                    submitList(model)
                }
            }
            else -> {}
        }
    }

    /**
     * Provides a layout manager that should be used by [setRecyclerLayoutManager]
     */
    override fun provideLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        return layoutManager
    }

    /**
     * Sets a layout manager to the recycler view
     */
    override fun setRecyclerLayoutManager(recyclerView: SupportRecyclerView) {
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = provideLayoutManager()
            if (recyclerView.itemDecorationCount == 0)
                recyclerView.addItemDecoration(DefaultSpacingDecorator())
        }
    }

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            val header = SupportLoadStateAdapter(resources, stateConfig).apply {
                registerFlowListener()
            }

            if (supportViewAdapter is RecyclerView.Adapter<*>) {
                (supportViewAdapter as RecyclerView.Adapter<*>)
                    .stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            recyclerView.adapter = supportViewAdapter.withLoadStateHeader(header = header)
        }
    }

    /**
     * Informs the underlying [SupportStateLayout] of changes to the [LoadState]
     *
     * @param loadState New state from the application
     */
    private fun changeLayoutState(loadState: LoadState) {
        supportViewAdapter.setLoadState(loadState)
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        super.initializeComponents(savedInstanceState)
        attachComponent(supportViewAdapter)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (supportViewAdapter.isEmpty())
                    onFetchDataInitialize()
            }
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and
     * non-graphical fragments can return null. This will be called between
     * [onCreate] & [onActivityCreated].
     *
     * A default View can be returned by calling [Fragment] in your
     * constructor. Otherwise, this method returns null.
     *
     * It is recommended to __only__ inflate the layout in this method and move
     * logic that operates on the returned View to [onViewCreated].
     *
     * If you return a View from here, you will later be called in [onDestroyView]
     * when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     * attached to. The fragment should not add the view itself, but this can be used to generate
     * the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingMapper(requireNotNull(view))
        return view
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.
     *
     * The fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelState()?.loadState?.observe(viewLifecycleOwner, onNetworkObserver)
        viewModelState()?.refreshState?.observe(viewLifecycleOwner, onRefreshObserver)
        setUpViewModelObserver()
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        lifecycleScope.launch {
            viewModelState()?.refresh()
        }
    }

    /**
     * Called when the fragment is no longer in use. This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        detachComponent(supportViewAdapter)
        super.onDestroy()
    }
}
