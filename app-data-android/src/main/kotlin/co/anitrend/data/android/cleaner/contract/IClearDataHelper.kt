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

package co.anitrend.data.android.cleaner.contract

import kotlin.coroutines.CoroutineContext

/**
 * Contract helper for delegating clear data tasks
 */
interface IClearDataHelper {

    /**
     * Executes an [action] in the provided [context]
     *
     * @param context A coroutine context to run the [action] in
     * @param action The task that needs to be run
     */
    suspend operator fun invoke(
        context: CoroutineContext,
        action: suspend () -> Unit
    )
}