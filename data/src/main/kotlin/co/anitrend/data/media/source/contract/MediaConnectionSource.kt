/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.media.source.contract

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.media.model.query.MediaConnectionQuery
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class MediaConnectionSource {
    abstract class Relations : AbstractCoreDataSource() {
        protected lateinit var query: MediaConnectionQuery.Relations

        protected abstract val observable: Flow<List<MediaRelationEntry>?>

        protected abstract suspend fun getRelations(requestCallback: RequestCallback)

        operator fun invoke(param: MediaParam.Relations): Flow<List<MediaRelationEntry>> {
            query = MediaConnectionQuery.Relations(param)
            invoke(block = ::getRelations)
            return observable.filterNotNull()
        }
    }

    abstract class Recommendations : AbstractCoreDataSource() {
        protected lateinit var query: MediaConnectionQuery.Recommendations

        protected abstract val observable: Flow<List<MediaRecommendationEntry>?>

        protected abstract suspend fun getRecommendations(requestCallback: RequestCallback)

        operator fun invoke(param: MediaParam.Recommendations): Flow<List<MediaRecommendationEntry>> {
            query = MediaConnectionQuery.Recommendations(param)
            invoke(block = ::getRecommendations)
            return observable.filterNotNull()
        }
    }
}
