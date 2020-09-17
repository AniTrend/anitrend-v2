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

package co.anitrend.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.IdRes
import co.anitrend.R
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.core.ui.fragment.model.FragmentItem
import co.anitrend.navigation.AboutRouter
import co.anitrend.navigation.SearchRouter
import co.anitrend.navigation.SettingsRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.presenter.MainPresenter
import co.anitrend.viewmodel.MainScreenViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import co.anitrend.core.ui.inject
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.MediaRouter.Provider.Companion.forCarousel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainScreen : AnitrendActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val bottomDrawerBehavior by lazy(UNSAFE) {
        BottomSheetBehavior.from(bottomNavigationDrawer)
    }

    private val viewModel by viewModel<MainScreenViewModel> {
        parametersOf(this)
    }

    private val presenter by inject<MainPresenter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        bottomAppBar.apply {
            setNavigationOnClickListener {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        floatingShortcutButton.setOnClickListener {
            Toast.makeText(this, "Fab Clicked", Toast.LENGTH_SHORT).show()
        }
        bottomNavigationView.setNavigationItemSelectedListener(this)
        bottomNavigationView.setCheckedItem(viewModel.state.selectedItem)
        onUpdateUserInterface()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.state.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.state.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        when (bottomDrawerBehavior.state) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                return
            }
            BottomSheetBehavior.STATE_COLLAPSED,
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return
            }
            else -> super.onBackPressed()
        }
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
            R.id.action_about -> {
                AboutRouter.startActivity(this)
                return true
            }
            R.id.action_settings -> {
                SettingsRouter.startActivity(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (viewModel.state.selectedItem != item.itemId) {
            if (item.groupId != R.id.nav_group_support) {
                viewModel.state.selectedItem = item.itemId
                onNavigate(viewModel.state.selectedItem)
            } else
                onNavigate(item.itemId)
        }
        return true
    }

    private fun onNavigate(@IdRes menu: Int) {
        val fragmentItem = when (menu) {
            R.id.nav_home -> {
                viewModel.state.selectedTitle = R.string.nav_home
                FragmentItem(
                    fragment = MediaRouter.forCarousel()
                )
            }
            R.id.nav_episode_feed -> {
                viewModel.state.selectedTitle = R.string.nav_episodes
                null
            }
            R.id.nav_news -> {
                viewModel.state.selectedTitle = R.string.nav_news
                null
            }
            R.id.nav_feed -> {
                viewModel.state.selectedTitle = R.string.nav_feed
                null
            }
            R.id.nav_review -> {
                viewModel.state.selectedTitle = R.string.nav_review
                null
            }
            R.id.nav_donate -> {
                Toast.makeText(this, "Donate", Toast.LENGTH_SHORT).show()
                null
            }
            R.id.nav_discord -> {
                Toast.makeText(this, "Discord", Toast.LENGTH_SHORT).show()
                null
            }
            R.id.nav_faq -> {
                Toast.makeText(this, "FAQ", Toast.LENGTH_SHORT).show()
                null
            }
            else -> null
        }

        launch(coroutineContext) { attachSelectedNavigationItem(menu, fragmentItem) }
    }

    /**
     * Replaces the current content with that in the selected fragment
     */
    private fun attachSelectedNavigationItem(@IdRes menu: Int, fragmentItem: FragmentItem?) {
        currentFragmentTag = fragmentItem?.commit(R.id.contentFrame, this) {}
        if (menu != R.id.nav_discord || menu != R.id.nav_donate) {
            bottomAppBar.setTitle(viewModel.state.selectedTitle)
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun onUpdateUserInterface() {
        onNavigate(viewModel.state.selectedItem)
    }
}