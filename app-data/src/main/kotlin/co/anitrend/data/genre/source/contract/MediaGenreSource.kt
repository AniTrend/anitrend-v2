/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.genre.source.contract

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import co.anitrend.arch.data.source.coroutine.SupportCoroutineDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.domain.genre.entities.Genre
import kotlinx.coroutines.flow.Flow

internal abstract class MediaGenreSource(
    supportDispatchers: SupportDispatchers
) : SupportCoroutineDataSource(supportDispatchers) {

    protected abstract val observable: Flow<List<Genre>>

    protected abstract suspend fun getGenres()

    internal operator fun invoke(): LiveData<List<Genre>> =
        liveData(coroutineContext) {
            emitSource(observable.asLiveData())
            retry = { getGenres() }
            getGenres()
        }
}