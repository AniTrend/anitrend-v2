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

package co.anitrend.data.source.media

import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.dao.query.MediaGenreDao
import co.anitrend.data.mapper.media.MediaGenreMapper
import co.anitrend.data.util.graphql.GraphUtil
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.source.coroutine.SupportCoroutineDataSource
import kotlinx.coroutines.async

class MediaGenreDataSource(
    private val mediaEndPoint: MediaEndPoint,
    private val mediaGenreDao: MediaGenreDao
) : SupportCoroutineDataSource() {

    /**
     * Handles the requesting data from a the network source and return
     * [NetworkState] to the caller after execution.
     *
     * In this context the super.invoke() method will allow a retry action to be set
     */
    override suspend fun invoke(): NetworkState {
        val state = super.invoke()
        if (connectivityHelper.isConnected) {
            val futureResponse = async {
                mediaEndPoint.getMediaGenres(
                    GraphUtil.getDefaultQuery()
                )
            }

            val mapper = MediaGenreMapper(
                mediaGenreDao = mediaGenreDao
            )

            return mapper.handleResponse(futureResponse)
        }
        return state
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        mediaGenreDao.deleteAll()
    }
}