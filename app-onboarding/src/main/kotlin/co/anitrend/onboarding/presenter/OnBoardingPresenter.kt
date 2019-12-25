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

package co.anitrend.onboarding.presenter

import android.content.Context
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.settings.Settings
import co.anitrend.navigation.NavigationTargets
import co.anitrend.onboarding.R
import co.anitrend.onboarding.model.OnBoarding

class OnBoardingPresenter(
    context: Context,
    settings: Settings
) : CorePresenter(context, settings) {

    val onBoardingItems: List<OnBoarding> by lazy {
        MutableList(6, ::initItems)
    }

    private fun initItems(index: Int) = OnBoarding(
        resource = resourceArray[index],
        backgroundColor = backgroundColorArray[index],
        text = titleArray[index],
        textColor = titleColorsArray[index]
    )

    fun onBoardingExperienceCompleted() {
        supportPreference.isNewInstallation = false
        NavigationTargets.Main(context)
    }

    companion object {

        private val backgroundColorArray: ArrayList<Int> = arrayListOf(
            R.drawable.gradient_slide_01,
            R.drawable.gradient_slide_02,
            R.drawable.gradient_slide_03,
            R.drawable.gradient_slide_04,
            R.drawable.gradient_slide_05,
            R.drawable.gradient_slide_06
        )

        private val resourceArray: ArrayList<Int> = arrayListOf(
            R.raw.swipe_left,
            R.raw.bookmark,
            R.raw.search,
            R.raw.rating_system,
            R.raw.writing_message,
            R.raw.cartoon_loading
        )

        private val titleArray: ArrayList<String> = arrayListOf(
            "Hi there! Welcome to AniTrend, let's preview some of the awesome features",
            "Track anime & manga, and favourite characters, actors and more",
            "Search for anime and manga and more all from one search screen",
            "Advanced rating systems for your anime and manga collections",
            "Meet other people, follow them and make friends with messages",
            "Enjoy your stay and feel free to join our discord community and support us! ^_^"
        )

        private val titleColorsArray: ArrayList<Int> = arrayListOf(
            R.color.white_1000,
            R.color.white_1000,
            R.color.white_1000,
            R.color.white_1000,
            R.color.white_1000,
            R.color.white_1000
        )
    }
}