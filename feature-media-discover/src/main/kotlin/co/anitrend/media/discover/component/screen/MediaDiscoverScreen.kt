/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.media.discover.component.screen

import android.os.Bundle
import co.anitrend.core.component.screen.AniTrendBoundScreen
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.media.discover.databinding.MediaDiscoverScreenBinding
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.SearchRouter
import co.anitrend.navigation.extensions.startActivity

class MediaDiscoverScreen : AniTrendBoundScreen<MediaDiscoverScreenBinding>() {
    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        requireBinding().floatingAction.setOnClickListener {
            SearchRouter.startActivity(context = this)
        }
        updateUserInterface()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MediaDiscoverScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
        setSupportActionBar(requireBinding().bottomAppBar)
    }

    private fun updateUserInterface() {
        currentFragmentTag =
            FragmentItem(
                fragment = MediaDiscoverRouter.forFragment(),
                parameter = intent.extras,
            ).commit(requireBinding().content, this)
    }
}
