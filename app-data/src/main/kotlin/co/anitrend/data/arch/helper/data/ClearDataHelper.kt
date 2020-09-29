/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.arch.helper.data

import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.arch.database.settings.IRefreshBehaviourSettings
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Helper for managing database on clear requests
 *
 * @param connectivity connectivity checker
 * @param settings behaviour related settings
 */
internal class ClearDataHelper(
    private val connectivity: SupportConnectivity,
    private val settings: IRefreshBehaviourSettings
) : IClearDataHelper {

    /**
     * Executes an [action] in the provided [context]
     *
     * @param context A coroutine context to run the [action] in
     * @param action The task that needs to be run
     */
    override suspend operator fun invoke(
        context: CoroutineContext,
        action: suspend () -> Unit
    ) {
        if (settings.clearDataOnSwipeRefresh) {
            if (connectivity.isConnected)
                runCatching {
                    withContext(context) {
                        action()
                    }
                }.onFailure {
                    Timber.tag(moduleTag).e(it)
                }
            return
        }

        Timber.tag(moduleTag).v(
            "Database table will not be cleared, setting prohibiting this"
        )
    }

    companion object {
        private val moduleTag = ClearDataHelper::class.java.simpleName
    }
}