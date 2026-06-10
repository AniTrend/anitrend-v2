/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.app.navigation.nav3

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import co.anitrend.common.navigation.FeatureNavEntryProviderRepository
import co.anitrend.navigation.nav3.AboutNavKey
import co.anitrend.navigation.nav3.AiringNavKey
import co.anitrend.navigation.nav3.AniTrendNavKey
import co.anitrend.navigation.nav3.ImageViewerNavKey
import co.anitrend.navigation.nav3.Nav3SpikeHomeKey
import co.anitrend.navigation.nav3.NavCommand
import org.koin.compose.koinInject

@Composable
fun AniTrendNav3Host(startKey: AniTrendNavKey) {
    val dispatcher = koinInject<AniTrendNavigationDispatcher>()
    val providerRepository = koinInject<FeatureNavEntryProviderRepository>()

    val registry =
        remember(dispatcher) {
            RuntimeFeatureNavRegistry(dispatcher).also { registry ->
                registry.install(providerRepository.providers())
            }
        }

    val backStack =
        rememberNavBackStack(startKey)

    LaunchedEffect(dispatcher, backStack) {
        dispatcher.commands.collect { command ->
            when (command) {
                is NavCommand.Push -> {
                    if (registry.hasEntryFor(command.key)) {
                        backStack.add(command.key)
                    } else {
                        error("No registered Nav3 destination for ${command.key}")
                    }
                }

                NavCommand.Pop -> {
                    if (backStack.size > 1) {
                        backStack.removeLast()
                    }
                }
            }
        }
    }

    BackHandler(enabled = backStack.size > 1) {
        backStack.removeLast()
    }

    NavDisplay(
        backStack = backStack,
        entryProvider =
            entryProvider {
                entry<Nav3SpikeHomeKey> {
                    Nav3SpikeHome()
                }
                entry<AboutNavKey> { key ->
                    registry.ContentFor(key)
                }
                entry<AiringNavKey> { key ->
                    registry.ContentFor(key)
                }
                entry<ImageViewerNavKey> { key ->
                    registry.ContentFor(key)
                }
            },
    )
}
