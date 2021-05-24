/*
 * Copyright (C) 2020  AniTrend
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
import co.anitrend.data.customlist.entity.CustomListEntity
import co.anitrend.data.customscore.entity.CustomScoreEntity
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.jikan.media.entity.JikanEntity
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.entity.view.MediaListEntityView
import co.anitrend.data.relation.entity.RelationEntity
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.tag.entity.TagEntity

internal sealed class MediaEntityView {
    abstract val media: MediaEntity
    abstract val nextAiring: AiringScheduleEntity?
    abstract val mediaListView: MediaListEntityView?
    abstract val jikan: JikanEntity?
    abstract val moe: RelationEntity?

    abstract val tags: List<TagEntity.Extended>
    abstract val genres: List<GenreEntity.Extended>

    abstract val links: List<LinkEntity>
    abstract val ranks: List<RankEntity>

    internal data class Core(
        @Embedded override val media: MediaEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "anilist"
        )
        override val moe: RelationEntity?,
        @Relation(
            parentColumn = "mal_id",
            entityColumn = "id"
        )
        override val jikan: JikanEntity?,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val mediaListView: MediaListEntityView?,
        @Relation(
            parentColumn = "next_airing_id",
            entityColumn = "id"
        )
        override val nextAiring: AiringScheduleEntity?,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val tags: List<TagEntity.Extended> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val genres: List<GenreEntity.Extended> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val links: List<LinkEntity> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val ranks: List<RankEntity> = emptyList(),
    ) : MediaEntityView()

    internal data class Extended(
        @Embedded override val media: MediaEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "anilist"
        )
        override val moe: RelationEntity?,
        @Relation(
            parentColumn = "mal_id",
            entityColumn = "id"
        )
        override val jikan: JikanEntity?,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val mediaListView: MediaListEntityView?,
        @Relation(
            parentColumn = "next_airing_id",
            entityColumn = "id"
        )
        override val nextAiring: AiringScheduleEntity?,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val tags: List<TagEntity.Extended> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val genres: List<GenreEntity.Extended> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val links: List<LinkEntity> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val ranks: List<RankEntity> = emptyList(),
    ) : MediaEntityView()
}