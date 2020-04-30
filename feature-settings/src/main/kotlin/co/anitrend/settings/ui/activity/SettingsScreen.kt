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

package co.anitrend.settings.ui.activity

import android.os.Bundle
import androidx.fragment.app.commit
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.settings.R
import co.anitrend.settings.databinding.SettingsActivityBinding
import co.anitrend.settings.ui.fragment.SettingsFragment
import kotlinx.android.synthetic.main.settings_activity.*
import org.koin.android.ext.android.inject

class SettingsScreen : AnitrendActivity<Nothing, CorePresenter>() {

    private val binding by lazy(LAZY_MODE_UNSAFE) {
        SettingsActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(bottomAppBar)
    }

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter by inject<CorePresenter>()

    /**
     * Additional initialization to be done in this method, if the overriding class is type of
     * [androidx.fragment.app.Fragment] then this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate]. Otherwise
     * [androidx.fragment.app.FragmentActivity.onPostCreate] invokes this function
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        onUpdateUserInterface()
    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [ISupportFragmentActivity] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {
        val fragment = supportFragmentManager.findFragmentByTag(
            SettingsFragment.FRAGMENT_TAG
        ) ?: SettingsFragment.newInstance()

        supportFragmentManager.commit {
            replace(
                R.id.contentFrame,
                fragment,
                SettingsFragment.FRAGMENT_TAG
            )
        }
    }
}