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
package co.anitrend.data.favourite.source.contract

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.domain.favourite.model.FavouriteInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class FavouriteSource {
    abstract class Toggle : AbstractCoreDataSource() {
        protected lateinit var mutation: FavouriteInput

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun toggleFavourite(requestCallback: RequestCallback)

        operator fun invoke(param: FavouriteInput): Flow<Boolean> {
            mutation = param
            invoke(block = ::toggleFavourite)
            return observable.filterNotNull()
        }
    }
}
