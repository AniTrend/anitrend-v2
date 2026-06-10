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

import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.navigation.nav3.AniTrendNavKey
import co.anitrend.navigation.nav3.NavigationDispatcher
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RuntimeFeatureNavRegistryTest {

    private val fakeDispatcher =
        object : NavigationDispatcher {
            override fun navigate(key: AniTrendNavKey) {}
            override fun pop() {}
        }

    private fun createRegistry() =
        RuntimeFeatureNavRegistry(fakeDispatcher)

    @Test
    fun `given provider with AboutNavKey when installed then registry has entry for AboutNavKey`() {
        val registry = createRegistry()
        val provider =
            object : FeatureNavEntryProvider {
                override fun register(registry: FeatureNavRegistry) {
                    registry.register(co.anitrend.navigation.nav3.AboutNavKey::class) {
                        // content not needed for registration test
                    }
                }
            }

        registry.install(listOf(provider))

        assertTrue(registry.hasEntryFor(co.anitrend.navigation.nav3.AboutNavKey))
    }

    @Test
    fun `given duplicate key registration then throws IllegalStateException`() {
        val registry = createRegistry()

        val key = co.anitrend.navigation.nav3.AboutNavKey::class
        registry.register(key) {}

        assertFailsWith<IllegalStateException> {
            registry.register(key) {}
        }
    }

    @Test
    fun `given unknown key then hasEntryFor returns false`() {
        val registry = createRegistry()

        assertTrue(!registry.hasEntryFor(co.anitrend.navigation.nav3.AboutNavKey))
    }

    @Test
    fun `given empty registry then providers returns zero entries and no provider crashes`() {
        val registry = createRegistry()

        registry.install(emptyList())

        assertEquals(0, registry.entryCount)
    }

    @Test
    fun `given two providers with different keys then both keys are registered`() {
        val registry = createRegistry()

        val providerA =
            object : FeatureNavEntryProvider {
                override fun register(registry: FeatureNavRegistry) {
                    registry.register(co.anitrend.navigation.nav3.AboutNavKey::class) {}
                }
            }

        val providerB =
            object : FeatureNavEntryProvider {
                override fun register(registry: FeatureNavRegistry) {
                    registry.register(co.anitrend.navigation.nav3.Nav3SpikeHomeKey::class) {}
                }
            }

        registry.install(listOf(providerA, providerB))

        assertTrue(registry.hasEntryFor(co.anitrend.navigation.nav3.AboutNavKey))
        assertTrue(registry.hasEntryFor(co.anitrend.navigation.nav3.Nav3SpikeHomeKey))
        assertEquals(2, registry.entryCount)
    }
}
