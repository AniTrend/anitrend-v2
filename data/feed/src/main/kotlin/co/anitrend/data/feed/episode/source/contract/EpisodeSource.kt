/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.feed.episode.source.contract

import androidx.paging.PagingData
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.feed.episode.model.query.EpisodeQuery
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import kotlinx.coroutines.flow.Flow

internal sealed class EpisodeSource {
    internal abstract class Detail : AbstractCoreDataSource() {
        protected abstract fun observable(param: EpisodeParam.Detail): Flow<Episode>

        operator fun invoke(param: EpisodeParam.Detail) = observable(param)
    }

    internal abstract class Paging : AbstractCoreDataSource() {
        protected lateinit var query: EpisodeQuery

        abstract operator fun invoke(param: EpisodeParam.Paged): Flow<PagingData<Episode>>

        abstract fun sync(param: EpisodeParam.Paged): Flow<Boolean>
    }
}
