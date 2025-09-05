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
package co.anitrend.data.edge.media.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.entity.Cover
import co.anitrend.data.edge.media.entity.Title
import co.anitrend.data.edge.media.entity.ExternalIds
import co.anitrend.data.edge.media.model.remote.EdgeMediaModel
import co.anitrend.arch.data.converter.SupportConverter

internal class EdgeMediaMapper(
    private val localSource: EdgeMediaLocalSource,
    private val converter: EdgeMediaModelConverter,
) : DefaultMapper<EdgeMediaModel, EdgeMediaEntity>() {
    override suspend fun onResponseMapFrom(source: EdgeMediaModel): EdgeMediaEntity {
        val media = source.media ?: error("Media payload missing")
        return converter.convertFrom(media)
    }

    override suspend fun persist(data: EdgeMediaEntity) {
        localSource.upsert(data)
    }
}

internal class EdgeMediaModelConverter : SupportConverter<EdgeMediaModel.Media, EdgeMediaEntity>() {
    override val fromType: (EdgeMediaModel.Media) -> EdgeMediaEntity = { model ->
        EdgeMediaEntity(
            id = model.mediaId?.aniList ?: model.id.toIntOrNull() ?: 0,
            // Titles
            title =
                Title(
                    romaji = model.title?.romaji,
                    english = model.title?.english,
                    native = model.title?.native,
                ),
            // Presentation
            format = model.format,
            status = model.status,
            bannerImage = model.banner,
            description = model.description,
            fanart = model.fanart,
            // Season snapshot
            season = model.seasons?.firstOrNull()?.name,
            seasonYear = model.seasons?.firstOrNull()?.year,
            // Cover
            cover =
                Cover(
                    medium = model.cover?.medium,
                    large = model.cover?.large,
                    extraLarge = model.cover?.extraLarge,
                    color = model.cover?.color,
                ),
            // Ratings / flags
            ageRating = model.ageRating,
            isAdult = model.isAdult,
            // External IDs
            externalIds =
                ExternalIds(
                    aniList = model.mediaId?.aniList,
                    myAnimeList = model.mediaId?.myAnimeList,
                    notify = model.mediaId?.notify,
                    trakt = model.mediaId?.trakt,
                    tvdb = model.mediaId?.tvdb,
                    tmdb = model.mediaId?.tmdb,
                ),
            // Audit
            updatedAt = model.updatedAt,
        )
    }

    override val toType: (EdgeMediaEntity) -> EdgeMediaModel.Media = { _ ->
        throw NotImplementedError()
    }
}
