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

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.media.model.query.MediaCarouselQuery
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.MediaCarousel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class MediaCarouselSource(
    dispatchers: SupportDispatchers
) : SupportCoreDataSource(dispatchers) {

    protected lateinit var query: MediaCarouselQuery
        private set

    protected abstract fun observable(): Flow<List<MediaCarousel>>

    protected abstract suspend fun getMediaCarouselAnime(requestCallback: RequestCallback)

    protected abstract suspend fun getMediaCarouselManga(requestCallback: RequestCallback)

    operator fun invoke(carouselQuery: IGraphPayload): Flow<List<MediaCarousel>> {
        query = carouselQuery as MediaCarouselQuery
        launch {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.INITIAL
            ) { getMediaCarouselAnime(it) }
        }

       launch {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.AFTER
            ) { getMediaCarouselManga(it) }
        }

        return observable()
    }
}