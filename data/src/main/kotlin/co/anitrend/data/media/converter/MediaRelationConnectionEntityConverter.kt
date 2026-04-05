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
package co.anitrend.data.media.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.media.entity.connection.MediaRelationConnectionEntity
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.enums.MediaRelation

private fun String?.toMediaRelation(): MediaRelation? =
    this?.let { value ->
        runCatching { MediaRelation.valueOf(value) }.getOrNull()
    }

internal class MediaRelationConnectionEntityConverter(
    override val fromType: (MediaRelationConnectionEntity) -> MediaRelationEntry = ::transform,
    override val toType: (MediaRelationEntry) -> MediaRelationConnectionEntity = { throw NotImplementedError() },
) : SupportConverter<MediaRelationConnectionEntity, MediaRelationEntry>() {
    private companion object : ISupportTransformer<MediaRelationConnectionEntity, MediaRelationEntry> {
        override fun transform(source: MediaRelationConnectionEntity) =
            MediaRelationEntry(
                relation = source.relation.toMediaRelation(),
                media = source.target.toMedia(),
                id = source.entryId,
            )
    }
}
