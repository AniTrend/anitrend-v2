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

package co.anitrend.onboarding.ui.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import co.anitrend.arch.ui.pager.SupportPageAdapter
import co.anitrend.onboarding.model.OnBoarding

class OnBoardingPageAdapter(
    private val onBoardingPages: List<OnBoarding>,
    context: FragmentActivity
) : SupportPageAdapter(context, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    init {

    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    override fun getItem(position: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}