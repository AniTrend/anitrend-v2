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
import androidx.fragment.app.FragmentManager
import co.anitrend.arch.ui.pager.SupportPageAdapter
import co.anitrend.onboarding.model.OnBoarding
import co.anitrend.onboarding.component.content.OnBoardingContent

class OnBoardingPageAdapter(
    private val onBoardingPages: List<OnBoarding>,
    fragmentManager: FragmentManager
) : SupportPageAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    init {
        titles.addAll(onBoardingPages.map { it.title.toString() })
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    override fun getItem(position: Int): Fragment {
        return OnBoardingContent.newInstance(
            onBoardingPages[position]
        )
    }
}