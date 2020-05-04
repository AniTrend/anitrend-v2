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
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.data.source.coroutine.SupportCoroutineDataSource
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.data.util.graphql.GraphUtil
import co.anitrend.domain.genre.entities.Genre
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.launch

internal abstract class MediaGenreSource(
    supportDispatchers: SupportDispatchers
) : SupportCoroutineDataSource<Any, Unit>(supportDispatchers) {

    protected abstract val observable:
            ISourceObservable<Nothing?, List<Genre>>

    protected abstract suspend fun getGenres()

    internal operator fun invoke(): LiveData<List<Genre>> {
        retry = {
            getGenres()
        }
        /** Initial initial call when [MediaGenreSource] is invoked */
        launch { getGenres() }
        return observable(null)
    }
}