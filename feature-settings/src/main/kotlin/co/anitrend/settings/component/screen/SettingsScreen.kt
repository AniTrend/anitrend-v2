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

package co.anitrend.settings.component.screen

import android.os.Bundle
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.ui.commit
import co.anitrend.core.component.screen.AnitrendScreen
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.settings.R
import co.anitrend.settings.databinding.SettingsActivityBinding
import co.anitrend.settings.component.content.SettingsContent

class SettingsScreen : AnitrendScreen<SettingsActivityBinding>() {

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

    /**
     * Handles the updating, binding, creation or state change, depending on the context of views.
     *
     * **N.B.** Where this is called is up to the developer
     */
    private fun onUpdateUserInterface() {
        currentFragmentTag = FragmentItem(
            fragment = SettingsContent::class.java
        ).commit(R.id.contentFrame, this)
    }
}