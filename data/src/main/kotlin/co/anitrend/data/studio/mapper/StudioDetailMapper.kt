/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.studio.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.datasource.local.connection.MediaStudioConnectionLocalSource
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity
import co.anitrend.data.studio.model.remote.StudioDetailContainer

internal class StudioDetailMapper(
    private val studioLocalSource: StudioLocalSource,
    private val connectionLocalSource: MediaStudioConnectionLocalSource,
) : DefaultMapper<StudioDetailContainer, StudioDetailPersistenceData>() {
    override suspend fun persist(data: StudioDetailPersistenceData) {
        studioLocalSource.upsert(data.studio)
        connectionLocalSource.clearByStudioId(data.studio.id)
        connectionLocalSource.upsert(data.mediaConnections)
    }

    override suspend fun onResponseMapFrom(source: StudioDetailContainer): StudioDetailPersistenceData {
        val studioEntity = StudioEntity(
            favourites = source.favourites ?: 0,
            isAnimationStudio = source.isAnimationStudio,
            isFavourite = source.isFavourite,
            isFavouriteBlocked = source.isFavouriteBlocked ?: false,
            name = source.name,
            siteUrl = source.siteUrl,
            id = source.id,
        )

        val mediaConnections = (source.media as? MediaConnection.Studio)?.edges.orEmpty().mapIndexedNotNull { index, edge ->
            val node = edge.node ?: return@mapIndexedNotNull null

            MediaStudioConnectionEntity(
                mediaId = node.id,
                entryId = edge.id,
                studioId = source.id,
                studioName = source.name,
                studioFavourites = source.favourites,
                studioIsAnimationStudio = source.isAnimationStudio,
                studioSiteUrl = source.siteUrl,
                mediaTitle = node.title?.userPreferred ?: node.title?.english ?: node.title?.romaji ?: node.title?.native,
                mediaCoverImageLarge = node.coverImage?.large,
                mediaCoverImageMedium = node.coverImage?.medium,
                mediaFormat = node.format?.name,
                mediaStartYear = node.startDate?.year,
                mediaAverageScore = node.averageScore,
                isMain = edge.isMainStudio,
                sortIndex = index,
            )
        }

        return StudioDetailPersistenceData(
            studio = studioEntity,
            mediaConnections = mediaConnections,
        )
    }
}
