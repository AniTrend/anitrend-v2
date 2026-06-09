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

import android.content.Context
import org.unifiedpush.android.connector.UnifiedPush

internal class UnifiedPushConnector(
    private val context: Context,
) : PushConnector {
    override val acknowledgedDistributor: String?
        get() = UnifiedPush.getAckDistributor(context)

    override val savedDistributor: String?
        get() = UnifiedPush.getSavedDistributor(context)

    override val availableDistributors: List<String>
        get() = UnifiedPush.getDistributors(context)

    override fun saveDistributor(distributor: String) {
        UnifiedPush.saveDistributor(context, distributor)
    }

    override fun register(messageForDistributor: String) {
        UnifiedPush.register(
            context = context,
            messageForDistributor = messageForDistributor,
        )
    }
}
