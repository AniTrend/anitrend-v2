/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.push

import kotlin.test.Test
import kotlin.test.assertEquals

class PushRegistrationCoordinatorTest {
    @Test
    fun `given acknowledged distributor when coordinator is created then existing distributor is re-registered`() {
        val connector = FakePushConnector(acknowledgedDistributor = "org.example.ntfy")

        val coordinator = PushRegistrationCoordinator(connector, "AniTrend")
        val result = coordinator.ensureRegistered()

        assertEquals(PushRegistrationResult.RegisteredExistingDistributor, result)
        assertEquals(emptyList(), connector.savedDistributors)
        assertEquals(listOf("AniTrend", "AniTrend"), connector.registrationMessages)
    }

    @Test
    fun `given single available distributor when coordinator is created then distributor is saved and registered`() {
        val connector = FakePushConnector(availableDistributors = listOf("org.example.ntfy"))

        PushRegistrationCoordinator(connector, "AniTrend")

        assertEquals(listOf("org.example.ntfy"), connector.savedDistributors)
        assertEquals(listOf("AniTrend"), connector.registrationMessages)
    }

    @Test
    fun `given no available distributors when coordinator is created then registration is skipped`() {
        val connector = FakePushConnector()

        val result = PushRegistrationCoordinator(connector, "AniTrend").ensureRegistered()

        assertEquals(PushRegistrationResult.SkippedNoDistributorAvailable, result)
        assertEquals(emptyList(), connector.savedDistributors)
        assertEquals(emptyList(), connector.registrationMessages)
    }

    @Test
    fun `given multiple distributors and no saved distributor when coordinator is created then selection is deferred`() {
        val connector =
            FakePushConnector(
                availableDistributors =
                    listOf(
                        "org.example.ntfy",
                        "org.example.nextpush",
                    ),
            )

        val result = PushRegistrationCoordinator(connector, "AniTrend").ensureRegistered()

        assertEquals(PushRegistrationResult.SkippedDistributorSelectionRequired, result)
        assertEquals(emptyList(), connector.savedDistributors)
        assertEquals(emptyList(), connector.registrationMessages)
    }

    @Test
    fun `given saved distributor still available when coordinator is created then saved distributor is registered`() {
        val connector =
            FakePushConnector(
                savedDistributor = "org.example.ntfy",
                availableDistributors = listOf("org.example.ntfy"),
            )

        PushRegistrationCoordinator(connector, "AniTrend")

        assertEquals(emptyList(), connector.savedDistributors)
        assertEquals(listOf("AniTrend"), connector.registrationMessages)
    }

    private class FakePushConnector(
        override val acknowledgedDistributor: String? = null,
        override val savedDistributor: String? = null,
        override val availableDistributors: List<String> = emptyList(),
    ) : PushConnector {
        val savedDistributors = mutableListOf<String>()
        val registrationMessages = mutableListOf<String>()

        override fun saveDistributor(distributor: String) {
            savedDistributors += distributor
        }

        override fun register(messageForDistributor: String) {
            registrationMessages += messageForDistributor
        }
    }
}
