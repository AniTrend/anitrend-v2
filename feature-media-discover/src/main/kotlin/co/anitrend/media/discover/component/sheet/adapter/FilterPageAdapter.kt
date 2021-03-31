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

package co.anitrend.media.discover.component.sheet.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asBundle
import timber.log.Timber

internal class FilterPageAdapter(
    private val param: MediaDiscoverRouter.Param?,
    private val titles: List<String>,
    private val fragmentActivity: FragmentActivity,
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = titles.size

    /**
     * Provide a new Fragment associated with the specified position.
     *
     * The adapter will be responsible for the Fragment lifecycle:
     *
     *  * The Fragment will be used to display an item.
     *  * The Fragment will be destroyed when it gets too far from the viewport, and its state
     * will be saved. When the item is close to the viewport again, a new Fragment will be
     * requested, and a previously saved state will be used to initialize it.
     *
     * @see ViewPager2.setOffscreenPageLimit
     */
    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> FragmentItem(
                fragment = MediaDiscoverFilterRouter.forSorting()
            ).fragmentByTagOrNew(fragmentActivity)
            1 -> FragmentItem(
                fragment = MediaDiscoverFilterRouter.forGeneral()
            ).fragmentByTagOrNew(fragmentActivity)
            2 -> FragmentItem(
                fragment = MediaDiscoverFilterRouter.forGenre()
            ).fragmentByTagOrNew(fragmentActivity)
            3 -> FragmentItem(
                fragment = MediaDiscoverFilterRouter.forTag()
            ).fragmentByTagOrNew(fragmentActivity)
            else -> Fragment()
        }
        fragment.arguments = param?.asBundle()
        Timber.v("Created fragment, named: ${titles[position]} -> $fragment")
        return fragment
    }
}