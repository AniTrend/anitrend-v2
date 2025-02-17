/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.settings.component.screen

import android.os.Bundle
import co.anitrend.core.component.screen.AniTrendBoundScreen
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.settings.component.content.SettingsContent
import co.anitrend.settings.databinding.SettingsActivityBinding

class SettingsScreen : AniTrendBoundScreen<SettingsActivityBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
        setSupportActionBar(requireBinding().bottomAppBar)
    }

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        onUpdateUserInterface()
    }

    private fun onUpdateUserInterface() {
        currentFragmentTag =
            FragmentItem(
                fragment = SettingsContent::class.java,
            ).commit(requireBinding().contentFrame, this)
    }
}
