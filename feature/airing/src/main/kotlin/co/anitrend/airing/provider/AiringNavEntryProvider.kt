/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.airing.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import co.anitrend.airing.component.compose.AiringRoute
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.settings.Settings
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.common.media.ui.controller.extensions.handleMediaItemNavigation
import co.anitrend.navigation.nav3.AiringNavKey
import org.koin.compose.koinInject

internal class AiringNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(AiringNavKey::class) { _ ->
            AiringNavContent(onBackPress = ::pop)
        }
    }
}

@Composable
private fun AiringNavContent(onBackPress: () -> Unit) {
    val settings = koinInject<Settings>()
    val dateHelper = koinInject<AniTrendDateHelper>()
    val context = LocalContext.current

    AiringRoute(
        settings = settings,
        userSettings = settings,
        dateHelper = dateHelper,
        onBackPress = onBackPress,
        onMediaItemClick = { param ->
            val activity = context as? FragmentActivity ?: return@AiringRoute
            activity.handleMediaItemNavigation(param, settings)
        },
    )
}
