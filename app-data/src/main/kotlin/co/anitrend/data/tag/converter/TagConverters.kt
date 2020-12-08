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
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.model.remote.TagModel
import co.anitrend.domain.tag.entity.Tag

internal class TagEntityConverter(
    override val fromType: (TagEntity) -> Tag = { transform(it) },
    override val toType: (Tag) -> TagEntity = { throw NotImplementedError() }
) : SupportConverter<TagEntity, Tag>() {
    private companion object : ISupportTransformer<TagEntity, Tag> {
        override fun transform(source: TagEntity) = Tag(
            id = source.id,
            name = source.name,
            description = source.description,
            rank = 0,
            category = source.category,
            isGeneralSpoiler = source.isGeneralSpoiler,
            isMediaSpoiler = false,
            isAdult = source.isAdult
        )
    }
}

internal class TagModelConverter(
    override val fromType: (TagModel) -> TagEntity = { transform(it) },
    override val toType: (TagEntity) -> TagModel = { throw NotImplementedError() }
) : SupportConverter<TagModel, TagEntity>() {
    private companion object : ISupportTransformer<TagModel, TagEntity> {
        override fun transform(source: TagModel) = TagEntity(
            id = source.id,
            name = source.name,
            description = source.description,
            category = source.category,
            isGeneralSpoiler = source.isGeneralSpoiler ?: false,
            isAdult = source.isAdult ?: false
        )
    }
}