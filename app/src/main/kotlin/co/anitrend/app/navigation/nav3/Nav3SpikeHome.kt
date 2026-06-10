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
package co.anitrend.app.navigation.nav3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.anitrend.navigation.nav3.NavigationDispatcher
import co.anitrend.navigation.nav3.AboutNavKey
import co.anitrend.navigation.nav3.ImageViewerNavKey
import co.anitrend.navigation.nav3.SettingsNavKey
import org.koin.compose.koinInject

@Composable
fun Nav3SpikeHome() {
    val dispatcher = koinInject<NavigationDispatcher>()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Navigation 3 Runtime Feature Spike")

        Button(
            onClick = {
                dispatcher.navigate(AboutNavKey)
            },
        ) {
            Text("Open About via Nav3")
        }

        Button(
            onClick = {
                dispatcher.navigate(
                    ImageViewerNavKey(
                        imageSources =
                            listOf(
                                "https://picsum.photos/800/1200",
                                "https://picsum.photos/800/1200?random=1",
                            ),
                        initialIndex = 0,
                    ),
                )
            },
        ) {
            Text("Open ImageViewer via Nav3")
        }

        Button(
            onClick = { dispatcher.navigate(SettingsNavKey()) },
        ) {
            Text("Open Settings via Nav3")
        }
    }
}
