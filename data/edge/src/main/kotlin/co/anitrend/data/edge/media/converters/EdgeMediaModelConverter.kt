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
package co.anitrend.data.edge.media.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.core.extensions.asEpochSeconds
import co.anitrend.data.edge.core.extensions.requireIntegralInt
import co.anitrend.data.edge.core.extensions.requireIntegralLong
import co.anitrend.data.edge.graphql.GetMediaByIdData
import co.anitrend.data.edge.media.entity.EdgeMediaCoverEntity
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.entity.EdgeMediaExternalIdsEntity
import co.anitrend.data.edge.media.entity.EdgeMediaScheduleEntity
import co.anitrend.data.edge.media.entity.EdgeMediaScheduleEpisodeEntity
import co.anitrend.data.edge.media.entity.EdgeMediaTitleEntity
import co.anitrend.domain.media.enums.MediaType

internal class EdgeMediaModelConverter : SupportConverter<GetMediaByIdData.Series, EdgeMediaEntity>() {
    override val fromType: (GetMediaByIdData.Series) -> EdgeMediaEntity = { model ->
        EdgeMediaEntity(
            id = model.mediaId.asEntityId(),
            title =
                EdgeMediaTitleEntity(
                    romaji = model.title.romaji,
                    english = model.title.english,
                    japanese = model.title.japanese,
                    canonical = model.title.canonical,
                    harigana = model.title.harigana,
                    synonyms = model.title.synonyms?.mapNotNull { it },
                ),
            format = model.format?.name,
            status = model.status?.name,
            banner = model.banner,
            description = model.description,
            fanart = model.fanart,
            homepage = model.homepage,
            airedEpisodes = model.airedEpisodes.requireIntegralInt("media airedEpisodes"),
            broadcast = model.broadcast,
            source = model.source?.name,
            schedule =
                model.schedule?.let {
                    EdgeMediaScheduleEntity(
                        firstAirDate = it.firstAirDate.asEpochSeconds("media schedule firstAirDate"),
                        lastAirDate = it.lastAirDate.asEpochSeconds("media schedule lastAirDate"),
                        nextEpisodeId =
                            it.nextEpisodeToAir
                                ?.id
                                ?.requireIntegralLong("media schedule nextEpisodeToAir id"),
                        lastEpisodeId =
                            it.lastAiredEpisode
                                ?.id
                                ?.requireIntegralLong("media schedule lastAiredEpisode id"),
                        nextEpisode = it.nextEpisodeToAir?.toScheduleEpisode(),
                        lastEpisode = it.lastAiredEpisode?.toScheduleEpisode(),
                    )
                },
            cover =
                EdgeMediaCoverEntity(
                    medium = model.cover.medium,
                    large = model.cover.large,
                    extraLarge = model.cover.extraLarge,
                    color = model.cover.color,
                ),
            ageRating = model.ageRating,
            isAdult = model.isAdult,
            kind = MediaType.valueOf(model.kind.name),
            chapters = model.chapters.requireIntegralInt("media chapters"),
            volumes = model.volumes.requireIntegralInt("media volumes"),
            moreInfo = model.moreInfo,
            publishedFrom = model.publishedFrom.asEpochSeconds("media publishedFrom"),
            publishedTo = model.publishedTo.asEpochSeconds("media publishedTo"),
            externalIds =
                EdgeMediaExternalIdsEntity(
                    aniDb = model.mediaId.anidb.requireIntegralLong("media id anidb"),
                    aniList = model.mediaId.anilist.requireIntegralLong("media id anilist"),
                    animePlanet = model.mediaId.animePlanet,
                    aniSearch = model.mediaId.anisearch.requireIntegralLong("media id anisearch"),
                    imdb = model.mediaId.imdb,
                    kitsu = model.mediaId.kitsu.requireIntegralLong("media id kitsu"),
                    liveChart = model.mediaId.livechart.requireIntegralLong("media id livechart"),
                    myAnimeList = model.mediaId.myanimelist.requireIntegralLong("media id myanimelist"),
                    notify = model.mediaId.notify,
                    shoboi = model.mediaId.shoboi.requireIntegralLong("media id shoboi"),
                    slug = model.mediaId.slug,
                    tmdb = model.mediaId.themoviedb.requireIntegralLong("media id themoviedb"),
                    trakt = model.mediaId.trakt.requireIntegralLong("media id trakt"),
                    tvDb = model.mediaId.tvdb.requireIntegralLong("media id tvdb"),
                    tvMaze = model.mediaId.tvMazeId.requireIntegralLong("media id tvMazeId"),
                    tvRage = model.mediaId.tvrage,
                ),
            updatedAt = model.updatedAt.asEpochSeconds("media updatedAt"),
        )
    }

    override val toType: (EdgeMediaEntity) -> GetMediaByIdData.Series = { _ ->
        throw NotImplementedError()
    }

    private fun GetMediaByIdData.SeriesMediaId.asEntityId(): String =
        notify
            ?: slug
            ?: anilist?.toString()
            ?: myanimelist?.toString()
            ?: throw IllegalStateException("Series payload did not contain a stable identifier in mediaId")

    private fun GetMediaByIdData.SeriesScheduleLastAiredEpisode.toScheduleEpisode() =
        EdgeMediaScheduleEpisodeEntity(
            id = id.requireIntegralLong("media schedule lastAiredEpisode id"),
            airDate = airDate.asEpochSeconds("media schedule lastAiredEpisode airDate"),
            episodeNumber = episodeNumber.requireIntegralInt("media schedule lastAiredEpisode episodeNumber"),
            image = image,
            name = name,
            overview = overview,
            productionCode = productionCode,
            runtime = runtime.requireIntegralInt("media schedule lastAiredEpisode runtime"),
            seasonNumber = seasonNumber.requireIntegralInt("media schedule lastAiredEpisode seasonNumber"),
            tmdbId = tmdbId.requireIntegralLong("media schedule lastAiredEpisode tmdbId"),
        )

    private fun GetMediaByIdData.SeriesScheduleNextEpisodeToAir.toScheduleEpisode() =
        EdgeMediaScheduleEpisodeEntity(
            id = id.requireIntegralLong("media schedule nextEpisodeToAir id"),
            airDate = airDate.asEpochSeconds("media schedule nextEpisodeToAir airDate"),
            episodeNumber = episodeNumber.requireIntegralInt("media schedule nextEpisodeToAir episodeNumber"),
            image = image,
            name = name,
            overview = overview,
            productionCode = productionCode,
            runtime = runtime.requireIntegralInt("media schedule nextEpisodeToAir runtime"),
            seasonNumber = seasonNumber.requireIntegralInt("media schedule nextEpisodeToAir seasonNumber"),
            tmdbId = tmdbId.requireIntegralLong("media schedule nextEpisodeToAir tmdbId"),
        )
}
