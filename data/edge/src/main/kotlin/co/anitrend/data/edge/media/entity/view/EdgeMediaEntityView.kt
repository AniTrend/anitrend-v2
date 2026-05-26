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
package co.anitrend.data.edge.media.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.edge.episode.entity.EdgeEpisodeEntity
import co.anitrend.data.edge.image.entity.EdgeMediaImageEntity
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.network.entity.EdgeNetworkEntity
import co.anitrend.data.edge.season.entity.EdgeSeasonEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeEntryEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeVideoEntity
import co.anitrend.data.edge.trailer.entity.EdgeTrailerEntity

data class EdgeThemeEntryEntityView(
    @Embedded val entry: EdgeThemeEntryEntity,
    @Relation(
        parentColumn = "entry_id",
        entity = EdgeThemeVideoEntity::class,
        entityColumn = "entry_id",
    )
    val videos: List<EdgeThemeVideoEntity>,
)

data class EdgeThemeEntityView(
    @Embedded val theme: EdgeThemeEntity,
    @Relation(
        parentColumn = "theme_id",
        entity = EdgeThemeEntryEntity::class,
        entityColumn = "theme_id",
    )
    val entries: List<EdgeThemeEntryEntityView>,
)

data class EdgeMediaEntityView(
    @Embedded val media: EdgeMediaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val networks: List<EdgeNetworkEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val trailers: List<EdgeTrailerEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val images: List<EdgeMediaImageEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val episodes: List<EdgeEpisodeEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val seasons: List<EdgeSeasonEntity>,
    @Relation(
        parentColumn = "id",
        entity = EdgeThemeEntity::class,
        entityColumn = "media_id",
    )
    val themes: List<EdgeThemeEntityView>,
)
