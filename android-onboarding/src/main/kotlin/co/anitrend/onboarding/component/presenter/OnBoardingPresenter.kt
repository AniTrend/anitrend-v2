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
package co.anitrend.onboarding.component.presenter

import android.content.Context
import androidx.core.text.buildSpannedString
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.navigation.MainRouter
import co.anitrend.navigation.OnBoardingRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.onboarding.R

class OnBoardingPresenter(
    context: Context,
    settings: Settings,
) : CorePresenter(context, settings) {
    val onBoardingItems =
        listOf(
            OnBoardingRouter.Param(
                resource = R.raw.anitrend,
                background = R.drawable.gradient_slide_01,
                title =
                    buildSpannedString {
                        append("Welcome to AniTrend!")
                    },
                subTitle =
                    buildSpannedString {
                        append("Track, watch and connect with fans around the world")
                    },
                description =
                    buildSpannedString {
                        append("Let's preview some of the awesome things AniTrend can do! Swipe left or right to get started")
                    },
                textColor = co.anitrend.arch.theme.R.color.primaryTextColor,
            ),
            OnBoardingRouter.Param(
                resource = R.raw.bookmark,
                background = R.drawable.gradient_slide_02,
                title =
                    buildSpannedString {
                        append("Track and lookup")
                    },
                subTitle =
                    buildSpannedString {
                        append("See what's trending in the world of anime and manga")
                    },
                description =
                    buildSpannedString {
                        append("Stay up to date with trends in the anime or manga community, keep your lists up to date from one place")
                    },
                textColor = co.anitrend.arch.theme.R.color.primaryTextColor,
            ),
            OnBoardingRouter.Param(
                resource = R.raw.search,
                background = R.drawable.gradient_slide_01,
                title =
                    buildSpannedString {
                        append("Comprehensive search engine")
                    },
                subTitle =
                    buildSpannedString {
                        append("Search for your favourite anime, manga, characters and more")
                    },
                description =
                    buildSpannedString {
                        append("Find whatever you're looking for with language agnostic search for anime and manga")
                    },
                textColor = co.anitrend.arch.theme.R.color.primaryTextColor,
            ),
            OnBoardingRouter.Param(
                resource = R.raw.rating_system,
                background = R.drawable.gradient_slide_02,
                title =
                    buildSpannedString {
                        append("Flexible scoring system")
                    },
                subTitle =
                    buildSpannedString {
                        append("Choose from several different scoring systems")
                    },
                description =
                    buildSpannedString {
                        append("Customize your experience using one of the many rating systems for your lists")
                    },
                textColor = co.anitrend.arch.theme.R.color.primaryTextColor,
            ),
            OnBoardingRouter.Param(
                resource = R.raw.cartoon_loading,
                background = R.drawable.gradient_slide_01,
                title =
                    buildSpannedString {
                        append("Intelligent by design")
                    },
                subTitle =
                    buildSpannedString {
                        append("Low battery, metered connection? AniTrend has you covered")
                    },
                description =
                    buildSpannedString {
                        append("AniTrend can automatically change it's behaviour based on system wide settings to give you the best experience")
                    },
                textColor = co.anitrend.arch.theme.R.color.primaryTextColor,
            ),
            OnBoardingRouter.Param(
                resource = R.raw.open_book,
                background = R.drawable.gradient_slide_02,
                title =
                    buildSpannedString {
                        append("Start your adventure")
                    },
                subTitle =
                    buildSpannedString {
                        append("We hope you enjoy your stay")
                    },
                description =
                    buildSpannedString {
                        append("Feel free to join our discord community and support the development of AniTrend on patreon! ^_^")
                    },
                textColor = co.anitrend.arch.theme.R.color.primaryTextColor,
            ),
        )

    val pages = onBoardingItems.size.minus(1)

    fun onBoardingExperienceCompleted() {
        settings.isNewInstallation.value = false
        MainRouter.startActivity(context)
    }
}
