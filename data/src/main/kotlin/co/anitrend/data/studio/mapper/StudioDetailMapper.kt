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
        val studio = requireNotNull(source.studio)

        val studioEntity =
            StudioEntity(
                favourites = studio.favourites ?: 0,
                isAnimationStudio = studio.isAnimationStudio,
                isFavourite = studio.isFavourite,
                isFavouriteBlocked = studio.isFavouriteBlocked ?: false,
                name = studio.name,
                siteUrl = studio.siteUrl,
                id = studio.id,
            )

        val mediaConnections = studio.media?.edges.orEmpty()
            .mapIndexed { index, edge ->
                val node = edge.node
                if (node == null) {
                    null
                } else {
                    MediaStudioConnectionEntity(
                        mediaId = node.id,
                        entryId = edge.id,
                        studioId = studio.id,
                        studioName = studio.name,
                        studioFavourites = studio.favourites,
                        studioIsAnimationStudio = studio.isAnimationStudio,
                        studioSiteUrl = studio.siteUrl,
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
            }
            .filterNotNull()

        return StudioDetailPersistenceData(
            studio = studioEntity,
            mediaConnections = mediaConnections,
        )
    }
}
