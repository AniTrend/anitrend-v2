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

package co.anitrend.data.media.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.remote.MediaModel
import co.anitrend.domain.media.entity.Media

internal class MediaEntityConverter(
    override val fromType: (MediaEntity) -> Media = { from().transform(it) },
    override val toType: (Media) -> MediaEntity = { to().transform(it) }
) : SupportConverter<MediaEntity, Media>() {
    companion object {
        private fun from() =
            object : ISupportMapperHelper<MediaEntity, Media> {
                override fun transform(source: MediaEntity): Media {
                    TODO("Not yet implemented")
                }
            }
        
        private fun to() =
            object : ISupportMapperHelper<Media, MediaEntity> {
                override fun transform(source: Media): MediaEntity {
                    throw NotImplementedError()
                }
            }
    }
}

internal class MediaModelConverter(
    override val fromType: (MediaModel) -> MediaEntity = { from().transform(it) },
    override val toType: (MediaEntity) -> MediaModel = { to().transform(it) }
) : SupportConverter<MediaModel, MediaEntity>() {
    companion object {
        private fun from() =
            object : ISupportMapperHelper<MediaModel, MediaEntity> {
                override fun transform(source: MediaModel): MediaEntity {
                    TODO("Not yet implemented")
                }
            }

        private fun to() =
            object : ISupportMapperHelper<MediaEntity, MediaModel> {
                override fun transform(source: MediaEntity): MediaModel {
                    throw NotImplementedError()
                }
            }
    }
}