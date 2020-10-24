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

package co.anitrend.data.carousel.source.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.carousel.model.query.CarouselQuery
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.MediaCarousel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class CarouselSource(
    protected val cachePolicy: ICacheStorePolicy,
    dispatchers: SupportDispatchers
) : SupportCoreDataSource(dispatchers) {

    protected val carouselAnimeId = 10L
    protected val carouselMangaId = 11L

    protected lateinit var query: CarouselQuery
        private set

    protected abstract fun observable(): Flow<List<MediaCarousel>>

    protected abstract suspend fun getMediaCarouselAnime(requestCallback: RequestCallback): Boolean

    protected abstract suspend fun getMediaCarouselManga(requestCallback: RequestCallback): Boolean

    operator fun invoke(carouselQuery: IGraphPayload): Flow<List<MediaCarousel>> {
        query = carouselQuery as CarouselQuery
        launch {
            if (cachePolicy.shouldRefresh(carouselAnimeId)) {
                requestHelper.runIfNotRunning(
                    IRequestHelper.RequestType.INITIAL
                ) {
                    val success = getMediaCarouselAnime(it)
                    if (success)
                        cachePolicy.updateLastRequest(carouselAnimeId)
                }
            }
        }

       launch {
           if (cachePolicy.shouldRefresh(carouselMangaId)) {
               requestHelper.runIfNotRunning(
                   IRequestHelper.RequestType.AFTER
               ) {
                   val success = getMediaCarouselManga(it)
                   if (success)
                       cachePolicy.updateLastRequest(carouselMangaId)
               }
           }
        }

        return observable()
    }
}