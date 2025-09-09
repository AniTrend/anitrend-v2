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
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.model.remote.EdgeMediaModel

internal class EdgeMediaModelConverter : SupportConverter<EdgeMediaModel.Media, EdgeMediaEntity>() {
    override val fromType: (EdgeMediaModel.Media) -> EdgeMediaEntity = { model ->
        EdgeMediaEntity(
            id = model.id,
            title =
                EdgeMediaEntity.Title(
                    romaji = model.title.romaji,
                    english = model.title.english,
                    japanese = model.title.japanese,
                    canonical = model.title.canonical,
                    harigana = model.title.harigana,
                    synonyms = model.title.synonyms,
                ),
            format = model.format,
            status = model.status,
            banner = model.banner,
            description = model.description,
            fanart = model.fanart,
            homepage = model.homepage,
            airedEpisodes = model.airedEpisodes,
            source = model.source,
            schedule =
                model.schedule?.let {
                    EdgeMediaEntity.Schedule(
                        firstAirDate = it.firstAirDate,
                        lastAirDate = it.lastAirDate,
                        nextEpisodeId = it.nextEpisodeToAir?.id,
                        lastEpisodeId = it.lastAiredEpisode?.id,
                    )
                },
            cover =
                EdgeMediaEntity.Cover(
                    medium = model.cover.medium,
                    large = model.cover.large,
                    extraLarge = model.cover.extraLarge,
                    color = model.cover.color,
                ),
            ageRating = model.ageRating,
            isAdult = model.isAdult,
            externalIds =
                EdgeMediaEntity.ExternalIds(
                    aniDb = model.mediaId.aniDb,
                    aniList = model.mediaId.aniList,
                    animePlanet = model.mediaId.animePlanet,
                    aniSearch = model.mediaId.aniSearch,
                    imdb = model.mediaId.imdb,
                    kitsu = model.mediaId.kitsu,
                    liveChart = model.mediaId.liveChart,
                    myAnimeList = model.mediaId.myAnimeList,
                    notify = model.mediaId.notify,
                    shoboi = model.mediaId.shoboi,
                    slug = model.mediaId.slug,
                    tmdb = model.mediaId.tmdb,
                    trakt = model.mediaId.trakt,
                    tvDb = model.mediaId.tvDb,
                    tvMaze = model.mediaId.tvMaze,
                    tvRage = model.mediaId.tvRage,
                ),
            updatedAt = model.updatedAt,
        )
    }

    override val toType: (EdgeMediaEntity) -> EdgeMediaModel.Media = { _ ->
        throw NotImplementedError()
    }
}
