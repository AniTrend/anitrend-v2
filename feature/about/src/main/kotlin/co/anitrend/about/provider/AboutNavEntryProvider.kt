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
package co.anitrend.about.provider

import co.anitrend.about.component.compose.AboutScreenContent
import co.anitrend.navigation.nav3.FeatureNavEntryProvider
import co.anitrend.navigation.nav3.FeatureNavRegistry
import co.anitrend.navigation.nav3.AboutNavKey

internal class AboutNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(AboutNavKey::class) {
            AboutScreenContent(
                onBackPress = ::pop,
            )
        }
    }
}
