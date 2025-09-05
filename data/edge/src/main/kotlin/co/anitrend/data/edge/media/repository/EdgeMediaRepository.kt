/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.media.repository

import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.edge.media.source.contract.EdgeMediaSource
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.media.repository.IMediaRepository

internal class EdgeMediaRepository(
    private val source: EdgeMediaSource,
) : IMediaRepository.Detail<DataState<Media>> {
    override fun getMedia(param: MediaParam.Detail): DataState<Media> = source create source(param.id.toInt())
}
