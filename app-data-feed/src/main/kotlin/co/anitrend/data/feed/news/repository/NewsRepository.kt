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

package co.anitrend.data.feed.news.repository

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.feed.news.NewsPagedRepository
import co.anitrend.data.feed.news.NewsSyncRepository
import co.anitrend.data.feed.news.source.contract.NewsSource
import co.anitrend.domain.news.model.NewsParam

internal sealed class NewsRepository(
    source: ISupportCoroutine? = null
) : SupportRepository(source) {

    class Paged(
        private val source: NewsSource
    ) : SupportRepository(source), NewsPagedRepository {
        override fun getPagedNews(
            param: NewsParam
        ) = source create source(param)
    }

    class Sync(
        private val source: NewsSource
    ) : SupportRepository(source), NewsSyncRepository {
        override fun sync(
            param: NewsParam
        ) = source create source.sync(param)
    }
}