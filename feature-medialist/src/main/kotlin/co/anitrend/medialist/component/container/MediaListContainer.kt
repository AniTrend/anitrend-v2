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

package co.anitrend.medialist.component.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.core.extensions.orEmpty
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.user.entity.User
import co.anitrend.medialist.R
import co.anitrend.medialist.component.container.adapter.MediaListPageAdapter
import co.anitrend.medialist.component.container.viewmodel.UserViewModel
import co.anitrend.medialist.databinding.MediaListContainerBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaListContainer(
    private val stateConfig: StateLayoutConfig,
    override val inflateMenu: Int = R.menu.discover_menu,
    override val inflateLayout: Int = R.layout.media_list_container
) : AniTrendContent<MediaListContainerBinding>() {

    private val viewModel by stateViewModel<UserViewModel>(
        state = { arguments.orEmpty() }
    )

    private fun setUpViewPager(statusList: Array<MediaListStatus>, customLists: List<CharSequence>) {
        if (requireBinding().viewPager.adapter == null) {
            requireBinding().viewPager.adapter = MediaListPageAdapter(
                param = viewModel.param,
                statusTitles = statusList,
                customListTitles = customLists,
                fragmentActivity = requireActivity(),
                fragmentManager = parentFragmentManager,
                lifecycle = lifecycle
            )

            val titles = statusList.map(MediaListStatus::alias) + customLists

            TabLayoutMediator(
                requireBinding().materialTabsLayout,
                requireBinding().viewPager
            ) { tab, index ->
                tab.text = titles[index]
            }.attach()
        }
    }

    private fun updateUI(user: User.Extended) {
        val listOption = user.listOption
        val customLists = when (viewModel.param?.type) {
            MediaType.ANIME -> listOption.animeList.customLists
            MediaType.MANGA -> listOption.mangaList.customLists
            null -> emptyList()
        }.toList()
        setUpViewPager(MediaListStatus.values(), customLists)
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     *
     * @see .onCreateOptionsMenu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            updateUI(it as User.Extended)
        }
        viewModelState().refreshState.observe(viewLifecycleOwner) {
            requireBinding().stateLayout.loadStateFlow.value = it
        }
        viewModelState().loadState.observe(viewLifecycleOwner) {
            requireBinding().stateLayout.loadStateFlow.value = it
        }
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * **N.B.** Calling super of this will register a connectivity change listener, so only
     * call `super.initializeComponents` if you require this behavior
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        super.initializeComponents(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            requireBinding().stateLayout.assureParamNotMissing(viewModel.param) {
                viewModelState().invoke(requireNotNull(viewModel.param))
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
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = MediaListContainerBinding.bind(requireNotNull(view))
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
        //requireBinding().viewPager.offscreenPageLimit = 4
        requireBinding().stateLayout.stateConfigFlow.value = stateConfig
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}