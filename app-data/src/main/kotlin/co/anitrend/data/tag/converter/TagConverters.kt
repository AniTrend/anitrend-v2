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

package co.anitrend.data.tag.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.data.media.model.remote.MediaTag
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.domain.tag.entity.Tag

internal class TagEntityConverter(
    override val fromType: (TagEntity) -> Tag = { from().transform(it) },
    override val toType: (Tag) -> TagEntity = { to().transform(it) }
) : SupportConverter<TagEntity, Tag>() {
    companion object {
        fun from() =
            object : ISupportMapperHelper<TagEntity, Tag> {
                override fun transform(source: TagEntity) = Tag(
                    id = source.id,
                    name = source.name,
                    description = source.description,
                    category = source.category,
                    rank = source.rank ?: 0,
                    isGeneralSpoiler = source.isGeneralSpoiler,
                    isMediaSpoiler = source.isMediaSpoiler,
                    isAdult = source.isAdult
                )
            }

        fun to() =
            object : ISupportMapperHelper<Tag, TagEntity> {
                override fun transform(source: Tag)= TagEntity(
                    id = source.id,
                    name = source.name,
                    description = source.description,
                    category = source.category,
                    rank = source.rank,
                    isGeneralSpoiler = source.isGeneralSpoiler,
                    isMediaSpoiler = source.isMediaSpoiler,
                    isAdult = source.isAdult
                )
            }
    }
}

internal class TagModelConverter(
    override val fromType: (MediaTag) -> TagEntity = { from().transform(it) },
    override val toType: (TagEntity) -> MediaTag = { to().transform(it) }
) : SupportConverter<MediaTag, TagEntity>() {
    companion object {
        fun from() =
            object : ISupportMapperHelper<MediaTag, TagEntity> {
                override fun transform(source: MediaTag) = TagEntity(
                    id = source.id,
                    name = source.name,
                    description = source.description,
                    category = source.category,
                    rank = source.rank,
                    isGeneralSpoiler = source.isGeneralSpoiler ?: false,
                    isMediaSpoiler = source.isMediaSpoiler ?: false,
                    isAdult = source.isAdult ?: false
                )
            }

        fun to() =
            object : ISupportMapperHelper<TagEntity, MediaTag> {
                override fun transform(source: TagEntity) = MediaTag(
                    id = source.id,
                    name = source.name,
                    description = source.description,
                    category = source.category,
                    rank = source.rank,
                    isGeneralSpoiler = source.isGeneralSpoiler,
                    isMediaSpoiler = source.isMediaSpoiler,
                    isAdult = source.isAdult
                )
            }
    }
}