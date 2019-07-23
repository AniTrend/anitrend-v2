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

package co.anitrend.data.usecase.media.meta

import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.dao.query.MediaTagDao
import co.anitrend.data.source.media.MediaTagDataSource
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.usecase.coroutine.ISupportCoroutineUseCase

class MediaTagFetchUseCase(
    private val mediaEndPoint: MediaEndPoint,
    private val mediaTagDao: MediaTagDao
) : ISupportCoroutineUseCase<Nothing?, NetworkState> {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override suspend fun invoke(param: Nothing?): NetworkState {
        // TODO: Implement a checking mechanism for the refresh interval for genres to
        //  avoid refreshing the database every time on request

        val mediaTagDataSource = MediaTagDataSource(
            mediaEndPoint = mediaEndPoint,
            mediaTagDao = mediaTagDao
        )

        return mediaTagDataSource()
    }
}