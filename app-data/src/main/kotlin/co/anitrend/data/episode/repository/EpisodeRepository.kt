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

package co.anitrend.data.episode.repository

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.episode.EpisodeDetailRepository
import co.anitrend.data.episode.EpisodePagedRepository
import co.anitrend.data.episode.source.contract.EpisodeSource
import co.anitrend.domain.episode.model.EpisodeParam

internal sealed class EpisodeRepository(
    source: ISupportCoroutine
) : SupportRepository(source) {

    internal class Detail(
        private val source: EpisodeSource.Detail
    ) : EpisodeRepository(source), EpisodeDetailRepository {
        override fun getEpisode(
            param: EpisodeParam.Detail
        ) = source create source(param)
    }

    internal class Paged(
        private val source: EpisodeSource.Paged
    ) : EpisodeRepository(source), EpisodePagedRepository {
        override fun getPagedEpisode(
            param: EpisodeParam.Paged
        ) = source create source(param)
    }
}