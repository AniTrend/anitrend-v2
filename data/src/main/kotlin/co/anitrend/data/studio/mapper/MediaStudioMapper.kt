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
import co.anitrend.data.media.model.container.MediaSidecarModelContainer
import co.anitrend.data.studio.datasource.local.connection.MediaStudioConnectionLocalSource
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity

internal class MediaStudioMapper(
    private val localSource: MediaStudioConnectionLocalSource,
) : DefaultMapper<MediaSidecarModelContainer.Studios, List<MediaStudioConnectionEntity>>() {
    override suspend fun persist(data: List<MediaStudioConnectionEntity>) {
        localSource.upsert(data)
    }

    override suspend fun onResponseMapFrom(source: MediaSidecarModelContainer.Studios): List<MediaStudioConnectionEntity> {
        val mediaId = source.media?.id ?: return emptyList()

        return source.media.studios?.edges.orEmpty().mapIndexedNotNull { index, edge ->
            val studio = edge.node ?: return@mapIndexedNotNull null

            MediaStudioConnectionEntity(
                mediaId = mediaId,
                entryId = edge.id,
                studioId = studio.id,
                studioName = studio.name,
                studioFavourites = studio.favourites,
                studioIsAnimationStudio = studio.isAnimationStudio,
                studioSiteUrl = studio.siteUrl,
                isMain = edge.isMain,
                sortIndex = index,
            )
        }
    }
}
