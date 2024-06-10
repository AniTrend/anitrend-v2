/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.feed.episode.repository

import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.feed.episode.EpisodeDetailRepository
import co.anitrend.data.feed.episode.EpisodePagedRepository
import co.anitrend.data.feed.episode.EpisodeSyncRepository
import co.anitrend.data.feed.episode.source.contract.EpisodeSource
import co.anitrend.domain.episode.model.EpisodeParam

internal sealed class EpisodeRepository {

    class Detail(
        private val source: EpisodeSource.Detail
    ) : EpisodeRepository(), EpisodeDetailRepository {
        override fun getEpisode(
            param: EpisodeParam.Detail
        ) = source create source(param)
    }

    class Paged(
        private val source: EpisodeSource.Paged
    ) : EpisodeRepository(), EpisodePagedRepository {
        override fun getPagedEpisode(
            param: EpisodeParam.Paged
        ) = source create source(param)
    }

    class Sync(
        private val source: EpisodeSource.Paged
    ) : EpisodeRepository(), EpisodeSyncRepository {
        override fun sync(
            param: EpisodeParam.Paged
        ) = source create source.sync(param)
    }
}
