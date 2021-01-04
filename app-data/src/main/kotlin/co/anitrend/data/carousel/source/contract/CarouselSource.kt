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
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.carousel.cache.CarouselCache
import co.anitrend.data.carousel.model.query.CarouselQuery
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class CarouselSource(
    protected val cachePolicy: ICacheStorePolicy,
    dispatcher: ISupportDispatcher
) : SupportCoreDataSource(dispatcher) {

    protected lateinit var query: CarouselQuery
        private set

    protected abstract fun observable(): Flow<List<MediaCarousel>?>

    protected abstract suspend fun getMediaCarousel(requestCallback: RequestCallback, mediaType: MediaType): Boolean

    protected abstract suspend fun getMediaCarouselAnime(requestCallback: RequestCallback): Boolean

    protected abstract suspend fun getMediaCarouselManga(requestCallback: RequestCallback): Boolean

    operator fun invoke(carouselQuery: IGraphPayload): Flow<List<MediaCarousel>?> {
        query = carouselQuery as CarouselQuery

        launch {
            requestHelper.runIfNotRunning(
                Request.Default(CarouselCache.Identifier.ANIME_META.key, Request.Type.INITIAL)
            ) {
                if (cachePolicy.shouldRefresh(CarouselCache.Identifier.ANIME_META.id)) {
                    val success = getMediaCarousel(it, MediaType.ANIME)
                    if (success)
                        cachePolicy.updateLastRequest(CarouselCache.Identifier.ANIME_META.id)
                }
            }
        }

        launch {
            requestHelper.runIfNotRunning(
                Request.Default(CarouselCache.Identifier.MANGA_META.key, Request.Type.INITIAL)
            ) {
                if (cachePolicy.shouldRefresh(CarouselCache.Identifier.MANGA_META.id)) {
                    val success = getMediaCarousel(it, MediaType.MANGA)
                    if (success)
                        cachePolicy.updateLastRequest(CarouselCache.Identifier.MANGA_META.id)
                }
            }
        }

        launch {
            requestHelper.runIfNotRunning(
                Request.Default(CarouselCache.Identifier.ANIME.key, Request.Type.BEFORE)
            ) {
                if (cachePolicy.shouldRefresh(CarouselCache.Identifier.ANIME.id)) {
                    val success = getMediaCarouselAnime(it)
                    if (success)
                        cachePolicy.updateLastRequest(CarouselCache.Identifier.ANIME.id)
                }
            }
        }

       launch {
           requestHelper.runIfNotRunning(
               Request.Default(CarouselCache.Identifier.MANGA.key, Request.Type.AFTER)
           ) {
               if (cachePolicy.shouldRefresh(CarouselCache.Identifier.MANGA.id)) {
                   val success = getMediaCarouselManga(it)
                   if (success)
                       cachePolicy.updateLastRequest(CarouselCache.Identifier.MANGA.id)
               }
           }
       }

        return observable()
    }
}