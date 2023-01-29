/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.component.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import co.anitrend.R
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.component.action.ChangeSettingsMenuStateAction
import co.anitrend.component.action.ShowHideFabStateAction
import co.anitrend.component.presenter.MainPresenter
import co.anitrend.component.viewmodel.MainScreenViewModel
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.extensions.orEmpty
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.inject
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.databinding.MainScreenBinding
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.*
import co.anitrend.navigation.drawer.component.content.BottomDrawerContent
import co.anitrend.navigation.drawer.component.content.contract.INavigationDrawer
import co.anitrend.navigation.drawer.model.navigation.Navigation
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.model.sorting.Sorting
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class MainScreen : AniTrendScreen<MainScreenBinding>() {

    private val drawerFragmentItem by lazy(UNSAFE) {
        FragmentItem(
            fragment = NavigationDrawerRouter.forFragment(),
            parameter = intent.extras
        )
    }

    private val navigationDrawer: INavigationDrawer
        get() = drawerFragmentItem.fragmentByTagOrNew(
            this
        ) as BottomDrawerContent

    private val viewModel by stateViewModel<MainScreenViewModel>(
        state = { intent.extras.orEmpty() }
    )

    private val presenter by inject<MainPresenter>()

    private val showHideFabStateAction by lazy(UNSAFE) {
        ShowHideFabStateAction(
            requireBinding().floatingShortcutButton
        )
    }

    private val changeSettingsMenuStateAction by lazy(UNSAFE) {
        ChangeSettingsMenuStateAction { showDrawerMenu ->
            lifecycleScope.launch {
                // Toggle between the current destination's FAB menu and the menu which should
                // be displayed when the BottomNavigationDrawer is open.
                navigationDrawer.toggleMenuVisibility(showDrawerMenu)
                // add a delay to hide specific menu items from this controller
                requireBinding().bottomAppBar.toggleVisibility(!showDrawerMenu) {
                    it.itemId == R.id.action_search
                }
            }
        }
    }

    private val onBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedDelegate()
            }
        }

    private suspend fun observeNavigationDrawer() {
        navigationDrawer.navigationFlow
            .onEach(::onNavigationItemSelected)
            .catch { throwable: Throwable ->
                Timber.e(throwable, "Navigation drawer flow threw an uncaught exception")
            }.collect()
    }

    /**
     * Replaces the current content with that in the selected fragment
     */
    private fun attachSelectedNavigationItem(fragmentItem: FragmentItem<Fragment>?) {
        currentFragmentTag = fragmentItem?.commit(
            requireBinding().contentMain,
            this
        )
    }

    private fun setUpNavigationDrawer() {
        requireBinding().bottomAppBar.setNavigationOnClickListener {
            navigationDrawer.toggleDrawer()
        }
        requireBinding().mainCoordinator.setOnClickListener {
            navigationDrawer.dismiss()
        }
        requireBinding().floatingShortcutButton.setOnClickListener {

        }
        navigateToUsing(viewModel.state.selectedItem)
    }

    private fun onUpdateUserInterface() {
        requireBinding().floatingShortcutButton.apply {
            setShowMotionSpecResource(R.animator.floating_action_show)
            setHideMotionSpecResource(R.animator.floating_action_hide)
        }
        inflateNavigationBottomDrawer()
    }

    private fun inflateNavigationBottomDrawer() {
        supportFragmentManager.commit {
            replace<BottomDrawerContent>(
                R.id.bottomNavigation,
                drawerFragmentItem.parameter,
                drawerFragmentItem.tag()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
        setSupportActionBar(requireBinding().bottomAppBar)
        onBackPressedDispatcher.addCallback(
            this,
            onBackPressedCallback
        )
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            navigationDrawer.setCheckedItem(viewModel.state.selectedItem)
            setUpNavigationDrawer()
            observeNavigationDrawer()
        }
        onUpdateUserInterface()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                SearchRouter.startActivity(this)
                return true
            }
            R.id.action_settings -> {
                SettingsRouter.startActivity(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * This method is called after [onStart] when the activity is
     * being re-initialized from a previously saved state, given here in
     * [savedInstanceState].  Most implementations will simply use [onCreate]
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by [onSaveInstanceState].
     *
     * This method is called between [onStart] and
     * [onPostCreate]. This method is called only when recreating
     * an activity; the method isn't invoked if [onStart] is called for
     * any other reason.
     *
     * @param savedInstanceState the data most recently supplied in [onSaveInstanceState].
     *
     * @see onCreate
     * @see onPostCreate
     * @see onResume
     * @see onSaveInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.state.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.state.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    private fun onBackPressedDelegate() {
        if (navigationDrawer.isShowing()) {
            navigationDrawer.dismiss()
            return
        }
        if (!requireBinding().bottomAppBar.isScrolledUp) {
            requireBinding().bottomAppBar.performShow()
            return
        }
        if (viewModel.state.shouldExit)
            ActivityCompat.finishAfterTransition(this)
        else {
            Toast.makeText(this, R.string.message_confirm_exit_app, Toast.LENGTH_LONG).show()
            viewModel.state.shouldExit = true
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    override fun onPause() {
        navigationDrawer.removeOnStateChangedAction(showHideFabStateAction)
        navigationDrawer.removeOnStateChangedAction(changeSettingsMenuStateAction)
        super.onPause()
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.
     */
    override fun onResume() {
        super.onResume()
        navigationDrawer.addOnStateChangedAction(showHideFabStateAction)
        navigationDrawer.addOnStateChangedAction(changeSettingsMenuStateAction)
    }

    override fun onDestroy() {
        binding?.floatingShortcutButton?.setOnClickListener(null)
        binding?.bottomAppBar?.setNavigationOnClickListener(null)
        binding?.mainCoordinator?.setOnClickListener(null)
        super.onDestroy()
    }

    private fun onNavigationItemSelected(menu: Navigation.Menu) {
        if (viewModel.state.selectedItem != menu.id) {
            navigateToUsing(menu.id)
            if (menu.isCheckable) {
                supportActionBar?.setTitle(viewModel.state.selectedTitle)
                viewModel.state.selectedItem = menu.id
                navigationDrawer.dismiss()
            }
        }
    }

    private fun navigateToUsing(@IdRes menuId: Int) {
        val fragmentItem = when (menuId) {
            R.id.navigation_home -> {
                viewModel.state.selectedTitle = R.string.navigation_home
                FragmentItem(
                    fragment = MediaCarouselRouter.forFragment()
                )
            }
            R.id.navigation_discover -> {
                viewModel.state.selectedTitle = R.string.navigation_discover
                FragmentItem(
                    fragment = MediaDiscoverRouter.forFragment(),
                    parameter = MediaDiscoverRouter.Param(
                        sort = listOf(
                            Sorting(
                                MediaSort.TRENDING,
                                SortOrder.DESC
                            )
                        )
                    ).asBundle()
                )
            }
            R.id.navigation_social -> {
                viewModel.state.selectedTitle = R.string.navigation_social
                FragmentItem(
                    fragment = FeedRouter.forFragment()
                )
            }
            R.id.navigation_reviews -> {
                viewModel.state.selectedTitle = R.string.navigation_review
                FragmentItem(
                    fragment = ReviewDiscoverRouter.forFragment()
                )
            }
            R.id.navigation_suggestions -> {
                viewModel.state.selectedTitle = R.string.navigation_suggestions
                FragmentItem(
                    fragment = SuggestionRouter.forFragment()
                )
            }
            R.id.navigation_news -> {
                viewModel.state.selectedTitle = R.string.navigation_news
                FragmentItem(
                    fragment = NewsRouter.forFragment()
                )
            }
            R.id.navigation_forum -> {
                viewModel.state.selectedTitle = R.string.navigation_forums
                FragmentItem(
                    fragment = ForumRouter.forFragment()
                )
            }
            R.id.navigation_episodes -> {
                viewModel.state.selectedTitle = R.string.navigation_episodes
                FragmentItem(
                    fragment = EpisodeRouter.forFragment()
                )
            }
            R.id.navigation_anime_list -> {
                viewModel.state.selectedTitle = R.string.navigation_anime_list
                FragmentItem(
                    fragment = MediaListRouter.forFragment(),
                    parameter = MediaListRouter.Param(
                        userId = presenter.settings.authenticatedUserId.value,
                        type = MediaType.ANIME
                    ).asBundle(),
                    tag = MediaType.ANIME.name
                )
            }
            R.id.navigation_manga_list -> {
                viewModel.state.selectedTitle = R.string.navigation_manga_list
                FragmentItem(
                    fragment = MediaListRouter.forFragment(),
                    parameter = MediaListRouter.Param(
                        userId = presenter.settings.authenticatedUserId.value,
                        type = MediaType.MANGA
                    ).asBundle(),
                    tag = MediaType.MANGA.name
                )
            }
            R.id.navigation_donate -> {
                presenter.redirectToPatreon()
                null
            }
            R.id.navigation_discord -> {
                presenter.redirectToDiscord()
                null
            }
            R.id.navigation_faq -> {
                presenter.redirectToFAQ()
                null
            }
            else -> null
        }

        lifecycleScope.launch {
            attachSelectedNavigationItem(fragmentItem)
        }
    }
}