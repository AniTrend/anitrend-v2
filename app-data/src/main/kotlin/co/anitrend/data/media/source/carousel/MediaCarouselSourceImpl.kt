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

package co.anitrend.data.media.source.carousel

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.arch.extension.controller
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.mapper.carousel.MediaCarouselAnimeMapper
import co.anitrend.data.media.mapper.carousel.MediaCarouselMangaMapper
import co.anitrend.data.media.source.carousel.contract.MediaCarouselSource
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.MediaCarousel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class MediaCarouselSourceImpl(
    private val remoteSource: MediaRemoteSource,
    private val animeMapper: MediaCarouselAnimeMapper,
    private val mangaMapper: MediaCarouselMangaMapper,
    private val strategy: ControllerStrategy<List<MediaCarousel>>,
    dispatchers: SupportDispatchers
) : MediaCarouselSource(dispatchers) {

    override suspend fun getMediaCarouselAnime(requestCallback: RequestCallback): List<MediaCarousel> {
        val queryBuilder = query.toQueryContainerBuilder(
            ignoreNulls = false
        )
        val deferred = async {
            remoteSource.getMediaCarouselAnime(queryBuilder)
        }
        val controller = animeMapper.controller(dispatchers, strategy)

        return controller(deferred, requestCallback).orEmpty()
    }

    override suspend fun getMediaCarouselManga(requestCallback: RequestCallback): List<MediaCarousel> {
        val queryBuilder = query.toQueryContainerBuilder(
            ignoreNulls = false
        )
        val deferred = async {
            remoteSource.getMediaCarouselManga(queryBuilder)
        }
        val controller = mangaMapper.controller(dispatchers, strategy)

        return controller(deferred, requestCallback).orEmpty()
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {

    }
}