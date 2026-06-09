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
package co.anitrend.navigation.nav3

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

interface FeatureNavRegistry {
    fun <T : AniTrendNavKey> register(
        key: KClass<T>,
        content: @Composable FeatureNavEntryScope.(T) -> Unit,
    )
}

interface FeatureNavEntryScope {
    val dispatcher: NavigationDispatcher

    fun pop() {
        dispatcher.pop()
    }

    fun navigate(key: AniTrendNavKey) {
        dispatcher.navigate(key)
    }
}
