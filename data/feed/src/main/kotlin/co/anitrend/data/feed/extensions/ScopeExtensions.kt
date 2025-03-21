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
package co.anitrend.data.feed.extensions

import co.anitrend.data.core.extensions.api
import co.anitrend.data.feed.api.FeedApiFactory
import co.anitrend.data.feed.episode.datasource.local.IEpisodeStore
import co.anitrend.data.feed.news.datasource.local.INewsStore
import org.koin.core.scope.Scope

internal inline fun <reified T> Scope.remoteSource(): T {
    val provider = get<FeedApiFactory>()
    return api(provider)
}

internal fun Scope.episodeLocalSource() = get<IEpisodeStore>().episodeDao()

internal fun Scope.newsLocalSource() = get<INewsStore>().newsDao()
