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

package co.anitrend.core.ui.component

import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.data.arch.AniTrendExperimentalFeature
import org.koin.core.context.GlobalContext
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.ScopeID
import timber.log.Timber

@AniTrendExperimentalFeature
class KoinScope : KoinScopeComponent {

    private val scopeID: ScopeID by lazy(UNSAFE) { getScopeId() }

    override val koin by lazy(UNSAFE) {
        GlobalContext.get()
    }

    override val scope by lazy(UNSAFE) {
        createScope(scopeID, getScopeName(), this)
    }
}