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

package co.anitrend.data.media.entity.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.reference.MediaTagRefEntity
import co.anitrend.data.tag.entity.TagEntity

internal data class MediaCoreEntity(
    @Embedded val media: MediaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id"
    )
    val airingSchedules: List<AiringScheduleEntity>
)

internal data class MediaExtendedEntity(
    @Embedded val media: MediaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mediaId"
    )
    val schedules: List<AiringScheduleEntity>,
    @Relation(
        parentColumn = "id",
        entity = TagEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            MediaTagRefEntity::class,
            parentColumn = "media_id",
            entityColumn = "media_tag_id"
        )
    )
    val tags: List<TagEntity>,
    @Relation(
        parentColumn = "id",
        entity = GenreEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            MediaTagRefEntity::class,
            parentColumn = "media_id",
            entityColumn = "media_genre_id"
        )
    )
    val genres: List<GenreEntity>
)

internal data class MediaFullEntity(
    @Embedded val media: MediaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mediaId"
    )
    val schedules: List<AiringScheduleEntity>,
    @Relation(
        parentColumn = "id",
        entity = TagEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            MediaTagRefEntity::class,
            parentColumn = "media_id",
            entityColumn = "media_tag_id"
        )
    )
    val tags: List<TagEntity>,
    @Relation(
        parentColumn = "id",
        entity = GenreEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            MediaTagRefEntity::class,
            parentColumn = "media_id",
            entityColumn = "media_genre_id"
        )
    )
    val genres: List<GenreEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id"
    )
    val links: List<MediaEntity.Link>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id"
    )
    val ranks: List<MediaEntity.Rank>
)