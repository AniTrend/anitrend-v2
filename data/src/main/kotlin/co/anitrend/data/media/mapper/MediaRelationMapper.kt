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
package co.anitrend.data.media.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.toConnectionPreviewEntity
import co.anitrend.data.media.datasource.local.connection.MediaRelationConnectionLocalSource
import co.anitrend.data.media.entity.connection.MediaRelationConnectionEntity
import co.anitrend.data.media.model.container.MediaConnectionModelContainer

internal class MediaRelationMapper(
    private val localSource: MediaRelationConnectionLocalSource,
    private val converter: MediaConverter,
) : DefaultMapper<MediaConnectionModelContainer.Relations, List<MediaRelationConnectionEntity>>() {
    override suspend fun persist(data: List<MediaRelationConnectionEntity>) {
        localSource.upsertConnections(data)
    }

    override suspend fun onResponseMapFrom(source: MediaConnectionModelContainer.Relations): List<MediaRelationConnectionEntity> {
        val mediaId = source.media?.id ?: return emptyList()

        return source.media.relations?.edges.orEmpty().mapIndexedNotNull { index, edge ->
            val media = edge.node ?: return@mapIndexedNotNull null

            MediaRelationConnectionEntity(
                mediaId = mediaId,
                entryId = edge.id,
                relation = edge.mediaRelation?.name,
                sortIndex = index,
                target = converter.convertFrom(media).toConnectionPreviewEntity(),
            )
        }
    }
}
