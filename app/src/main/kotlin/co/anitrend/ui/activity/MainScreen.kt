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

package co.anitrend.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.IdRes
import co.anitrend.R
import co.anitrend.arch.extension.ext.LAZY_MODE_UNSAFE
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.extensions.commit
import co.anitrend.core.extensions.injectScoped
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.core.ui.fragment.model.FragmentItem
import co.anitrend.model.ScreenState
import co.anitrend.navigation.NavigationTargets
import co.anitrend.navigation.extensions.forFragment
import co.anitrend.presenter.MainPresenter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainScreen : AnitrendActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val redirectItem by extra(ARG_KEY_REDIRECT, R.id.nav_home)

    private val bottomDrawerBehavior
            by lazy(LAZY_MODE_UNSAFE) {
                BottomSheetBehavior.from(bottomNavigationDrawer)
            }

    private lateinit var state: ScreenState

    private val presenter by injectScoped<MainPresenter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        state = ScreenState()
        if (redirectItem != null)
            state.selectedItem = redirectItem!!
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
        bottomNavigationView.setCheckedItem(state.selectedItem)
        onUpdateUserInterface()
    }

    /**
     * Expects a module helper if one is available for the current scope, otherwise return null
     */
    override fun featureModuleHelper(): Nothing? = null

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(ARG_KEY_NAVIGATION_STATE, state)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(ARG_KEY_NAVIGATION_STATE))
            state = savedInstanceState.getParcelable(ARG_KEY_NAVIGATION_STATE)!!
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
                NavigationTargets.Search(this).invoke()
                return true
            }
            R.id.action_about -> {
                NavigationTargets.About(this).invoke()
                return true
            }
            R.id.action_settings -> {
                NavigationTargets.Settings(this).invoke()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (state.selectedItem != item.itemId) {
            if (item.groupId != R.id.nav_group_support) {
                state.selectedItem = item.itemId
                onNavigate(state.selectedItem)
            } else
                onNavigate(item.itemId)
        }
        return true
    }

    private fun onNavigate(@IdRes menu: Int) {
        val fragmentItem = when (menu) {
            R.id.nav_home -> {
                state.selectedTitle = R.string.nav_home
                //val fragment = NavigationTargets.Discover(applicationContext).forFragment()
                //fragment?.let {
                //    FragmentItem(
                //        fragment = it
                //    )
                //}
                null
            }
            R.id.nav_episode_feed -> {
                state.selectedTitle = R.string.nav_episodes
                null
            }
            R.id.nav_news -> {
                state.selectedTitle = R.string.nav_news
                null
            }
            R.id.nav_feed -> {
                state.selectedTitle = R.string.nav_feed
                null
            }
            R.id.nav_review -> {
                state.selectedTitle = R.string.nav_review
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

        launch { attachSelectedNavigationItem(menu, fragmentItem) }
    }

    /**
     * Replaces the current content with that in the selected fragment
     */
    private fun attachSelectedNavigationItem(@IdRes menu: Int, fragmentItem: FragmentItem<*>?) {
        currentFragmentTag = fragmentItem?.commit(R.id.contentFrame, this) {}
        if (menu != R.id.nav_discord || menu != R.id.nav_donate) {
            bottomAppBar.setTitle(state.selectedTitle)
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun onUpdateUserInterface() {
        onNavigate(state.selectedItem)
    }

    companion object {
        private const val ARG_KEY_REDIRECT = "ARG_KEY_REDIRECT"

        private const val ARG_KEY_NAVIGATION_STATE = "ARG_KEY_NAVIGATION_STATE"
    }
}