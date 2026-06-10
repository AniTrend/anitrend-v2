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

import androidx.compose.runtime.Composable
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavEntryScope
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.navigation.nav3.AniTrendNavKey
import co.anitrend.navigation.nav3.NavigationDispatcher
import kotlin.reflect.KClass
import timber.log.Timber

class RuntimeFeatureNavRegistry(
    private val dispatcher: NavigationDispatcher,
) : FeatureNavRegistry {
    private val entries =
        linkedMapOf<KClass<out AniTrendNavKey>, @Composable FeatureNavEntryScope.(AniTrendNavKey) -> Unit>()

    private val scope =
        object : FeatureNavEntryScope {
            override val dispatcher: NavigationDispatcher =
                this@RuntimeFeatureNavRegistry.dispatcher
        }

    val entryCount: Int get() = entries.size

    @Suppress("UNCHECKED_CAST")
    override fun <T : AniTrendNavKey> register(
        key: KClass<T>,
        content: @Composable FeatureNavEntryScope.(T) -> Unit,
    ) {
        val previous =
            entries.put(key) { navKey ->
                content(navKey as T)
            }

        if (previous != null) {
            Timber.w("Duplicate Nav3 destination registered for key: ${key.qualifiedName} — overwriting")
            return
        }

        Timber.d("Registered Nav3 entry for key: ${key.qualifiedName}")
    }

    fun install(providers: List<FeatureNavEntryProvider>) {
        Timber.d("Installing ${providers.size} Nav3 feature entry providers")
        providers.forEach { provider ->
            Timber.d("-> ${provider::class.qualifiedName ?: provider::class.java.name}")
            provider.register(this)
        }
        Timber.d("${entries.size} Nav3 entries registered total")
    }

    @Composable
    fun ContentFor(key: AniTrendNavKey) {
        val content =
            entries[key::class]
                ?: error("No Navigation3 entry registered for key: ${key::class.qualifiedName}")

        with(scope) {
            content(key)
        }
    }

    fun hasEntryFor(key: AniTrendNavKey): Boolean = entries.containsKey(key::class)
}
