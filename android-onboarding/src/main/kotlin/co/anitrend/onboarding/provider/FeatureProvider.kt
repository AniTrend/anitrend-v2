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
package co.anitrend.onboarding.provider

import android.content.Context
import android.content.Intent
import co.anitrend.navigation.OnBoardingRouter
import co.anitrend.onboarding.component.content.OnBoardingContent
import co.anitrend.onboarding.component.screen.OnBoardingScreen

internal class FeatureProvider : OnBoardingRouter.Provider {
    override fun fragment() = OnBoardingContent::class.java

    override fun activity(context: Context?) = Intent(context, OnBoardingScreen::class.java)
}
