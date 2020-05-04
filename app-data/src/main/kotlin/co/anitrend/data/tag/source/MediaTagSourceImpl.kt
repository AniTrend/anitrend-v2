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

package co.anitrend.data.tag.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.policy.OnlineStrategy
import co.anitrend.data.arch.extension.controller
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.tag.converter.TagEntityConverter
import co.anitrend.data.tag.datasource.local.MediaTagLocalSource
import co.anitrend.data.tag.datasource.remote.MediaTagRemoteSource
import co.anitrend.data.tag.mapper.MediaTagResponseMapper
import co.anitrend.data.tag.source.contract.MediaTagSource
import co.anitrend.domain.tag.entities.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class MediaTagSourceImpl(
    private val remoteSource: MediaTagRemoteSource,
    private val localSource: MediaTagLocalSource,
    private val mapper: MediaTagResponseMapper,
    private val clearDataHelper: ClearDataHelper,
    dispatchers: SupportDispatchers
) : MediaTagSource(dispatchers) {

    @ExperimentalCoroutinesApi
    override val observable =
        object : ISourceObservable<Nothing?, List<Tag>> {

            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<List<Tag>> {
                val tagFlow = localSource.findAllFlow()
                return tagFlow.map {
                    TagEntityConverter().convertFrom(it)
                }.flowOn(
                    dispatchers.computation
                ).asLiveData()
            }
        }

    override suspend fun getTags() {
        val deferred = async {
            remoteSource.getMediaTags()
        }

        val controller =
            mapper.controller(dispatchers, OnlineStrategy.create())

        controller(deferred, networkState)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        clearDataHelper {
            localSource.clear()
        }
    }
}