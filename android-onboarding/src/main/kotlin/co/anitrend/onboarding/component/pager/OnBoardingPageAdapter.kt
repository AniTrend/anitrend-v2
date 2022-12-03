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
package co.anitrend.onboarding.component.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import co.anitrend.arch.ui.pager.SupportPageAdapter
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.navigation.OnBoardingRouter
import co.anitrend.navigation.extensions.asBundle

class OnBoardingPageAdapter(
    private val pages: List<OnBoardingRouter.Param>,
    private val fragmentActivity: FragmentActivity,
    fragmentManager: FragmentManager,
) : SupportPageAdapter(fragmentManager) {
    init {
        titles.addAll(
            pages.map {
                it.title.toString()
            },
        )
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    override fun getItem(position: Int): Fragment {
        val fragmentItem =
            FragmentItem(
                fragment = OnBoardingRouter.forFragment(),
                parameter = pages[position].asBundle(),
            )

        return fragmentItem.fragmentByTagOrNew(fragmentActivity)
    }
}
