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

package co.anitrend.data.airing.repository

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.airing.AiringSchedulePagedRepository
import co.anitrend.data.airing.source.contract.AiringScheduleSource
import co.anitrend.domain.airing.model.AiringParam

internal sealed class AiringScheduleRepository(
    source: ISupportCoroutine? = null
) : SupportRepository(source) {

    class Paged(
        private val source: AiringScheduleSource.Paged
    ) : AiringScheduleRepository(), AiringSchedulePagedRepository {
        override fun getPaged(
            param: AiringParam.Find
        ) = source create source(param)
    }
}