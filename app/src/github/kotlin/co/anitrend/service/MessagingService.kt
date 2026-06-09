/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.service

import co.anitrend.data.settings.push.IUnifiedPushSettings
import co.anitrend.push.PushRegistrationCoordinator
import org.koin.android.ext.android.inject
import org.unifiedpush.android.connector.FailedReason
import org.unifiedpush.android.connector.PushService
import org.unifiedpush.android.connector.data.PushEndpoint
import org.unifiedpush.android.connector.data.PushMessage
import timber.log.Timber

class MessagingService : PushService() {
    private val settings by inject<IUnifiedPushSettings>()
    private val coordinator by inject<PushRegistrationCoordinator>()

    override fun onMessage(
        message: PushMessage,
        instance: String,
    ) {
        Timber.i(
            "UnifiedPush payload received for instance=%s size=%d decrypted=%s",
            instance,
            message.content.size,
            message.decrypted,
        )
    }

    override fun onNewEndpoint(
        endpoint: PushEndpoint,
        instance: String,
    ) {
        val previousEndpoint = settings.endpointFor(instance)
        settings.updateEndpoint(instance, endpoint.url)

        if (previousEndpoint == endpoint.url) {
            Timber.i("UnifiedPush endpoint acknowledged for instance=%s", instance)
        } else {
            Timber.i(
                "UnifiedPush endpoint updated for instance=%s temporary=%s",
                instance,
                endpoint.temporary,
            )
        }
    }

    override fun onRegistrationFailed(
        reason: FailedReason,
        instance: String,
    ) {
        Timber.w("UnifiedPush registration failed for instance=%s reason=%s", instance, reason)

        if (reason == FailedReason.INTERNAL_ERROR) {
            coordinator.ensureRegistered()
        }
    }

    override fun onTempUnavailable(instance: String) {
        super.onTempUnavailable(instance)
        Timber.w("UnifiedPush distributor temporarily unavailable for instance=%s", instance)
    }

    override fun onUnregistered(instance: String) {
        settings.clearEndpoint(instance)
        Timber.w("UnifiedPush registration removed for instance=%s", instance)
        coordinator.ensureRegistered()
    }
}
