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

package co.anitrend.data.repository.media

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.data.datasource.remote.media.contract.MediaGenreSource
import co.anitrend.data.model.core.media.MediaGenre
import co.anitrend.domain.repositories.media.IMediaGenreRepository

class MediaGenreRepository(
    private val dataSource: MediaGenreSource
) : SupportRepository(dataSource), IMediaGenreRepository<UserInterfaceState<List<MediaGenre>>> {

    /**
     * @return media genres
     */
    override fun getMediaGenres(): UserInterfaceState<List<MediaGenre>> {
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        val modelObserver = dataSource.getMediaGenres()

        return UserInterfaceState(
            model = modelObserver,
            networkState = dataSource.networkState,
            refresh = {
                dataSource.invalidateAndRefresh()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                dataSource.retryRequest()
            }
        )
    }
}