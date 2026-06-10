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
package co.anitrend.settings.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.navigation.nav3.SettingsNavKey
import co.anitrend.settings.component.builder.PreferenceBuilder
import co.anitrend.settings.component.compose.SettingsContentScreen
import co.anitrend.settings.component.presenter.SettingsPresenter
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext

internal class SettingsNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(SettingsNavKey::class) { key ->
            SettingsNavContent(
                startDestination = key.destination,
                onBackPress = ::pop,
            )
        }
    }
}

@Composable
private fun SettingsNavContent(
    startDestination: co.anitrend.navigation.nav3.SettingsDestination,
    onBackPress: () -> Unit,
) {
    val koin = GlobalContext.get()
    val presenter =
        remember {
            SettingsPresenter(
                context = koin.get(),
                settings = koin.get(),
                preferenceBuilder = PreferenceBuilder(),
            )
        }

    val navController = rememberNavController()
    val settingsItems =
        remember(presenter) {
            presenter.getSettingsItems(
                navigateTo = { destination ->
                    navController.navigate(route = destination.name)
                },
            )
        }

    SettingsContentScreen(
        navigationController = navController,
        settingsItems = settingsItems,
        presenter = presenter,
        onBackPress = {
            if (!navController.popBackStack()) {
                onBackPress()
            }
        },
    )
}
