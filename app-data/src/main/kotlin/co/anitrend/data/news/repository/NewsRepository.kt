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

package co.anitrend.data.news.repository

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.news.NewsPagedRepository
import co.anitrend.data.news.source.contract.NewsSource
import co.anitrend.domain.news.model.NewsParam

internal class NewsRepository(
    private val source: NewsSource
) : SupportRepository(source), NewsPagedRepository {
    override fun getPagedNews(
        param: NewsParam
    ) = source create source(param)
}