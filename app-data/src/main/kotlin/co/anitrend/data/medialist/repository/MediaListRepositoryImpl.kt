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

package co.anitrend.data.medialist.repository

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.medialist.MediaListPagedRepo
import co.anitrend.data.medialist.source.paged.contract.MediaListPagedSource
import co.anitrend.domain.common.graph.IGraphPayload

internal sealed class MediaListRepositoryImpl(source: ISupportCoroutine) : SupportRepository(source) {

    class Paged(
        private val source: MediaListPagedSource
    ) : MediaListRepositoryImpl(source), MediaListPagedRepo {
        override fun getMediaListPaged(
            query: IGraphPayload
        ) = source create source(query)
    }
}