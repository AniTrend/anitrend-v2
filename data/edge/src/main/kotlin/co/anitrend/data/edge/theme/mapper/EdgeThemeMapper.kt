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
package co.anitrend.data.edge.theme.mapper

import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.edge.theme.EdgeThemeEmbedded
import co.anitrend.data.edge.theme.converter.EdgeThemeConverter
import co.anitrend.data.edge.theme.datasource.EdgeThemeLocalSource
import co.anitrend.data.edge.theme.entity.EdgeThemeEntryEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeVideoEntity

internal class EdgeThemeMapper(
    override val localSource: EdgeThemeLocalSource,
    override val converter: EdgeThemeConverter,
) : EmbedMapper<EdgeThemeEmbedded, EdgeThemeEntity>() {
    private var pendingEntries: List<EdgeThemeEntryEntity> = emptyList()
    private var pendingVideos: List<EdgeThemeVideoEntity> = emptyList()

    override suspend fun persist(data: List<EdgeThemeEntity>) {
        localSource.upsert(data)
        if (pendingEntries.isNotEmpty()) {
            localSource.upsertEntries(pendingEntries)
        }
        if (pendingVideos.isNotEmpty()) {
            localSource.upsertVideos(pendingVideos)
        }
        pendingEntries = emptyList()
        pendingVideos = emptyList()
    }

    override suspend fun onResponseMapFrom(source: List<EdgeThemeEmbedded>): List<EdgeThemeEntity> {
        val mapped = source.mapNotNull(converter::convertFromOrNull)
        pendingEntries = mapped.flatMap { it.entries }
        pendingVideos = mapped.flatMap { it.videos }
        return mapped.map { it.theme }
    }
}
