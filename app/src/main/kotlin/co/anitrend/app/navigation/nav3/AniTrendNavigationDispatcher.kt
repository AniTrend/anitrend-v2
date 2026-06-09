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
package co.anitrend.app.navigation.nav3

import co.anitrend.navigation.nav3.AniTrendNavKey
import co.anitrend.navigation.nav3.NavCommand
import co.anitrend.navigation.nav3.NavigationDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AniTrendNavigationDispatcher : NavigationDispatcher {
    private val mutableCommands =
        MutableSharedFlow<NavCommand>(
            replay = 0,
            extraBufferCapacity = 64,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

    val commands: SharedFlow<NavCommand> =
        mutableCommands.asSharedFlow()

    override fun navigate(key: AniTrendNavKey) {
        mutableCommands.tryEmit(NavCommand.Push(key))
    }

    override fun pop() {
        mutableCommands.tryEmit(NavCommand.Pop)
    }
}
