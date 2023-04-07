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

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.carousel.cache.CarouselCache
import co.anitrend.data.carousel.model.query.CarouselQuery
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.carousel.model.CarouselParam
import kotlinx.coroutines.flow.Flow

internal abstract class CarouselSource : SupportCoreDataSource() {

    protected lateinit var query: CarouselQuery
        private set

    protected abstract val cachePolicy: ICacheStorePolicy

    protected val animeCacheIdentity: CacheIdentity = CarouselCache.Identifier.ANIME
    protected val animeMetaCacheIdentity: CacheIdentity = CarouselCache.Identifier.ANIME_META
    protected val mangaCacheIdentity: CacheIdentity = CarouselCache.Identifier.MANGA
    protected val mangaMetaCacheIdentity: CacheIdentity = CarouselCache.Identifier.MANGA_META

    protected abstract fun observable(): Flow<List<MediaCarousel>>

    protected abstract suspend fun getMediaCarouselAnimeMeta(requestCallback: RequestCallback): Boolean

    protected abstract suspend fun getMediaCarouselMangaMeta(requestCallback: RequestCallback): Boolean

    protected abstract suspend fun getMediaCarouselAnime(requestCallback: RequestCallback): Boolean

    protected abstract suspend fun getMediaCarouselManga(requestCallback: RequestCallback): Boolean

    operator fun invoke(param: CarouselParam.Find): Flow<List<MediaCarousel>> {
        query = CarouselQuery(param)

        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = mangaCacheIdentity,
            block = ::getMediaCarouselManga
        )

        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = animeCacheIdentity,
            block = ::getMediaCarouselAnime
        )

        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = mangaMetaCacheIdentity,
            block = ::getMediaCarouselMangaMeta
        )

        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = animeMetaCacheIdentity,
            block = ::getMediaCarouselAnimeMeta
        )

        return observable()
    }
}
