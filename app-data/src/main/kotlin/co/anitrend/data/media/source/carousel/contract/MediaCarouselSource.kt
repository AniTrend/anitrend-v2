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

package co.anitrend.data.media.source.carousel.contract

import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.helper.RequestHelper
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.media.model.query.MediaCarouselQuery
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.MediaCarousel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

internal abstract class MediaCarouselSource(
    dispatchers: SupportDispatchers
) : SupportCoreDataSource(dispatchers) {

    protected lateinit var query: MediaCarouselQuery
        private set

    protected abstract suspend fun getMediaCarouselAnime(
        requestCallback: RequestCallback
    ): List<MediaCarousel>

    protected abstract suspend fun getMediaCarouselManga(
        requestCallback: RequestCallback
    ): List<MediaCarousel>

    operator fun invoke(carouselQuery: IGraphPayload): Flow<List<MediaCarousel>> {
        query = carouselQuery as MediaCarouselQuery
        val anime = channelFlow {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.INITIAL
            ) {
                val result = getMediaCarouselAnime(it)
                offer(result)
            }
        }

       val manga = channelFlow {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.AFTER
            ) {
                val result = getMediaCarouselManga(it)
                offer(result)
            }
        }

        return anime.combine(manga) { a, m ->
            a + m
        }.flowOn(dispatchers.io)
    }
}