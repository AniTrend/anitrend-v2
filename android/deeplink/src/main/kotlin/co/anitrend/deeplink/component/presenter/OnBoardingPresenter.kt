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
package co.anitrend.deeplink.component.presenter

import android.content.Context
import androidx.compose.ui.graphics.Color
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.deeplink.component.model.OnboardingPage

class OnBoardingPresenter(
    context: Context,
    settings: Settings,
) : CorePresenter(context, settings) {
    val onBoardingPages =
        listOf(
            OnboardingPage(
                resource = co.anitrend.deeplink.R.drawable.welcome,
                background = listOf(Color(0xFFEADDFF), Color(0xFF6750A4)),
                title = co.anitrend.deeplink.R.string.onboarding_title_welcome,
                description = co.anitrend.deeplink.R.string.onboarding_desc_welcome,
            ),
            OnboardingPage(
                resource = co.anitrend.deeplink.R.drawable.trends,
                background = listOf(Color(0xFFE0F2F1), Color(0xFF009688)),
                title = co.anitrend.deeplink.R.string.onboarding_title_trends,
                description = co.anitrend.deeplink.R.string.onboarding_desc_trends,
            ),
            OnboardingPage(
                resource = co.anitrend.deeplink.R.drawable.search,
                background = listOf(Color(0xFFE8EAF6), Color(0xFF7986CB)),
                title = co.anitrend.deeplink.R.string.onboarding_title_search,
                description = co.anitrend.deeplink.R.string.onboarding_desc_search,
            ),
            OnboardingPage(
                resource = co.anitrend.deeplink.R.drawable.rating,
                background = listOf(Color(0xFFEFEBE9), Color(0xFFA1887F)),
                title = co.anitrend.deeplink.R.string.onboarding_title_rating,
                description = co.anitrend.deeplink.R.string.onboarding_desc_rating,
            ),
            OnboardingPage(
                resource = co.anitrend.deeplink.R.drawable.smart,
                background = listOf(Color(0xFFECEFF1), Color(0xFF607D8B)),
                title = co.anitrend.deeplink.R.string.onboarding_title_smart,
                description = co.anitrend.deeplink.R.string.onboarding_desc_smart,
            ),
            OnboardingPage(
                resource = co.anitrend.deeplink.R.drawable.adventure,
                background = listOf(Color(0xFFFBE9E7), Color(0xFFFF8A65)),
                title = co.anitrend.deeplink.R.string.onboarding_title_adventure,
                description = co.anitrend.deeplink.R.string.onboarding_desc_adventure,
            ),
        )

    fun updateInstallationStatus() {
        settings.isNewInstallation.value = false
    }
}
