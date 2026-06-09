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

import timber.log.Timber

internal interface PushConnector {
    val acknowledgedDistributor: String?
    val savedDistributor: String?
    val availableDistributors: List<String>

    fun saveDistributor(distributor: String)

    fun register(messageForDistributor: String)
}

internal enum class PushRegistrationResult {
    RegisteredExistingDistributor,
    RegisteredSavedDistributor,
    SkippedNoDistributorAvailable,
    SkippedDistributorSelectionRequired,
}

internal class PushRegistrationCoordinator(
    private val connector: PushConnector,
    private val messageForDistributor: String,
) {
    init {
        when (ensureRegistered()) {
            PushRegistrationResult.RegisteredExistingDistributor ->
                Timber.i("UnifiedPush registration refreshed")
            PushRegistrationResult.RegisteredSavedDistributor ->
                Timber.i("UnifiedPush distributor selected automatically and registered")
            PushRegistrationResult.SkippedNoDistributorAvailable ->
                Timber.i("UnifiedPush registration skipped because no distributor is installed")
            PushRegistrationResult.SkippedDistributorSelectionRequired ->
                Timber.i("UnifiedPush registration skipped because distributor selection requires user input")
        }
    }
    fun ensureRegistered(): PushRegistrationResult {
        if (connector.acknowledgedDistributor != null) {
            connector.register(messageForDistributor)
            return PushRegistrationResult.RegisteredExistingDistributor
        }

        val availableDistributors = connector.availableDistributors
        val savedDistributor = connector.savedDistributor

        if (savedDistributor != null && savedDistributor in availableDistributors) {
            connector.register(messageForDistributor)
            return PushRegistrationResult.RegisteredExistingDistributor
        }

        if (availableDistributors.isEmpty()) {
            return PushRegistrationResult.SkippedNoDistributorAvailable
        }

        if (availableDistributors.size > 1) {
            return PushRegistrationResult.SkippedDistributorSelectionRequired
        }

        connector.saveDistributor(availableDistributors.first())
        connector.register(messageForDistributor)
        return PushRegistrationResult.RegisteredSavedDistributor
    }
}
