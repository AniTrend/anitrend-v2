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

package co.anitrend.app.ui.activity.index

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import co.anitrend.app.R
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.ui.activity.AnitrendActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import co.anitrend.arch.ui.activity.SupportActivity
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.arch.ui.util.SupportUiKeyStore
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AnitrendActivity<Nothing, CorePresenter>(), NavigationView.OnNavigationItemSelectedListener {

    private val bottomDrawerBehavior: BottomSheetBehavior<FrameLayout>?
        get() = BottomSheetBehavior.from(bottomNavigationDrawer)

    @IdRes
    private var selectedItem: Int = R.id.nav_popular_series

    @StringRes
    private var selectedTitle: Int = R.string.nav_popular_series

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter: CorePresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        if (intent.hasExtra(SupportUiKeyStore.arg_redirect))
            selectedItem = intent.getIntExtra(SupportUiKeyStore.arg_redirect, R.id.nav_popular_series)
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        bottomAppBar.apply {
            setNavigationOnClickListener {
                bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        floatingShortcutButton.setOnClickListener {
            Toast.makeText(this, "Fab Clicked", Toast.LENGTH_SHORT).show()
        }
        bottomNavigationView.apply {
            setNavigationItemSelectedListener(this@MainActivity)
            setCheckedItem(selectedItem)
        }
        onUpdateUserInterface()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SupportUiKeyStore.key_navigation_selected, selectedItem)
        outState.putInt(SupportUiKeyStore.key_navigation_title, selectedTitle)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedItem = savedInstanceState.getInt(SupportUiKeyStore.key_navigation_selected)
        selectedTitle = savedInstanceState.getInt(SupportUiKeyStore.key_navigation_title)
    }

    override fun onBackPressed() {
        when (bottomDrawerBehavior?.state) {
            BottomSheetBehavior.STATE_EXPANDED,
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                return
            }
            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(this, "onMenuItemClick", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (selectedItem != item.itemId) {
            if (item.groupId != R.id.nav_group_customization) {
                selectedItem = item.itemId
                onNavigate(selectedItem)
            } else
                onNavigate(item.itemId)
        }
        return true
    }

    private fun onNavigate(@IdRes menu: Int) {
        var supportFragment: SupportFragment<*, *, *>? = null
        when (menu) {
            R.id.nav_theme -> {
                when (AppCompatDelegate.getDefaultNightMode()) {
                    AppCompatDelegate.MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    else ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                recreate()
            }
            R.id.nav_about -> Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
            R.id.nav_popular_series -> {
                selectedTitle = R.string.nav_popular_series
                supportFragment = null
            }
            R.id.nav_popular_movies -> {
                selectedTitle = R.string.nav_popular_movies
                supportFragment = null
            }
        }

        attachSelectedNavigationItem(menu, supportFragment)
    }

    /**
     * Replaces the current content with that in the selected fragment
     */
    private fun attachSelectedNavigationItem(@IdRes menu: Int, supportFragment: SupportFragment<*, *, *>?) {
        supportFragment?.apply {
            supportFragmentActivity = this@apply
            supportFragmentManager.commit {
                replace(R.id.contentFrame, this@apply, tag)
            }
        }

        if (menu != R.id.nav_theme) {
            bottomAppBar.setTitle(selectedTitle)
            bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [co.anitrend.arch.ui.view.contract.ISupportFragmentActivity]
     * will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {
        if (selectedItem != 0)
            onNavigate(selectedItem)
        else
            onNavigate(R.id.nav_popular_series)
    }

    /**
     * Handles the complex logic required to dispatch network request to [SupportViewModel]
     * which uses [SupportRepository] to either request from the network or database cache.
     *
     * The results of the dispatched network or cache call will be published by the
     * [androidx.lifecycle.LiveData] specifically [SupportViewModel.model]
     *
     * @see [SupportViewModel.requestBundleLiveData]
     */
    override fun onFetchDataInitialize() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}