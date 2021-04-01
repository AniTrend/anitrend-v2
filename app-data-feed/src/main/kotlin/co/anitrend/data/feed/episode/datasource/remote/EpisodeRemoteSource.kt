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

package co.anitrend.data.feed.episode.datasource.remote

import co.anitrend.data.core.XML
import co.anitrend.data.feed.episode.model.page.EpisodePageModel
import co.anitrend.data.feed.contract.RssLocale
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface EpisodeRemoteSource {

    @XML
    @GET("/crunchyroll/rss/anime/popular")
    suspend fun getPopularEpisodes(
        @Query("locale") locale: RssLocale
    ): Response<EpisodePageModel>

    @XML
    @GET("/crunchyroll/rss/anime")
    suspend fun getLatestEpisodes(
        @Query("locale") locale: RssLocale
    ): Response<EpisodePageModel>
}