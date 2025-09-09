/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.media.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.edge.media.entity.view.EdgeMediaEntityView
import co.anitrend.data.genre.entity.connection.GenreConnectionEntity
import co.anitrend.data.genre.entity.view.GenreEntityView
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.entity.view.MediaListEntityView
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.tag.entity.connection.TagConnectionEntity
import co.anitrend.data.tag.entity.view.TagEntityView

internal sealed class MediaEntityView {
    abstract val media: MediaEntity
    abstract val nextAiring: AiringScheduleEntity?
    abstract val mediaList: MediaListEntityView.Core?
    abstract val genres: List<GenreEntityView>
    abstract val edge: EdgeMediaEntityView?

    internal data class Core(
        @Embedded override val media: MediaEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id",
            entity = MediaListEntity::class,
        )
        override val mediaList: MediaListEntityView.Core?,
        @Relation(
            parentColumn = "next_airing_id",
            entityColumn = "id",
        )
        override val nextAiring: AiringScheduleEntity?,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id",
            entity = GenreConnectionEntity::class,
        )
        override val genres: List<GenreEntityView> = emptyList(),
        /*@Relation(
            parentColumn = "id",
            entityColumn = "id_ani_list",
        )*/
        override val edge: EdgeMediaEntityView?,
    ) : MediaEntityView()

    internal data class Extended(
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id",
        )
        val links: List<LinkEntity> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id",
        )
        val ranks: List<RankEntity> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id",
            entity = TagConnectionEntity::class,
        )
        val tags: List<TagEntityView> = emptyList(),
        @Embedded override val media: MediaEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id",
            entity = MediaListEntity::class,
        )
        override val mediaList: MediaListEntityView.Core?,
        @Relation(
            parentColumn = "next_airing_id",
            entityColumn = "id",
        )
        override val nextAiring: AiringScheduleEntity?,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id",
            entity = GenreConnectionEntity::class,
        )
        override val genres: List<GenreEntityView> = emptyList(),
        /*@Relation(
            parentColumn = "id",
            entityColumn = "id_ani_list",
        )*/
        override val edge: EdgeMediaEntityView?,
    ) : MediaEntityView()
}
