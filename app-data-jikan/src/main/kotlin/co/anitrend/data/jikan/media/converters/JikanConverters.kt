/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.jikan.media.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.jikan.author.entity.JikanAuthorEntity
import co.anitrend.data.jikan.contract.JikanItem
import co.anitrend.data.jikan.licensor.entity.JikanLicensorEntity
import co.anitrend.data.jikan.media.entity.JikanEntity
import co.anitrend.data.jikan.media.model.anime.JikanMediaModel
import co.anitrend.data.jikan.model.JikanWrapper
import co.anitrend.data.jikan.producer.entity.JikanProducerEntity
import co.anitrend.data.jikan.studio.entity.JikanStudioEntity

internal class JikanModelConverter(
    override val fromType: (JikanWrapper<JikanMediaModel>) -> JikanEntity = ::transform,
    override val toType: (JikanEntity) -> JikanWrapper<JikanMediaModel> = { throw NotImplementedError() }
) : SupportConverter<JikanWrapper<JikanMediaModel>, JikanEntity>() {
    private companion object : ISupportTransformer<JikanWrapper<JikanMediaModel>, JikanEntity> {
        override fun transform(source: JikanWrapper<JikanMediaModel>) = when(val data = source.data) {
            is JikanMediaModel.Anime -> JikanEntity(
                title = JikanEntity.Title(
                    japanese = data.titleJapanese,
                    english = data.titleEnglish,
                    preferred = data.title,
                    synonyms = data.titleSynonyms.orEmpty()
                ),
                info = data.moreInfo,
                source = data.source,
                rating = data.rating,
                episodes = data.episodes,
                duration = data.duration,
                premiered = data.premiered,
                broadcast = data.broadcast,
                trailerUrl = data.trailerUrl,
                openingThemes = data.openingThemes.orEmpty(),
                endingThemes = data.endingThemes.orEmpty(),
                url = data.url,
                imageUrl = data.imageUrl,
                type = data.type,
                releasing = data.releasing,
                synopsis = data.synopsis,
                background = data.background,
                id = data.malId,
            )
            is JikanMediaModel.Manga -> JikanEntity(
                title = JikanEntity.Title(
                    japanese = data.titleJapanese,
                    english = data.titleEnglish,
                    preferred = data.title,
                    synonyms = data.titleSynonyms.orEmpty()
                ),
                info = data.moreInfo,
                volumes = data.volumes,
                chapters = data.chapters,
                url = data.url,
                imageUrl = data.imageUrl,
                type = data.type,
                releasing = data.releasing,
                synopsis = data.synopsis,
                background = data.background,
                id = data.malId,
            )
        }
    }
}

internal class JikanAuthorModelConverter(
    override val fromType: (JikanItem) -> JikanAuthorEntity = ::transform,
    override val toType: (JikanAuthorEntity) -> JikanItem = { throw NotImplementedError() }
) : SupportConverter<JikanItem, JikanAuthorEntity>() {
    private companion object : ISupportTransformer<JikanItem, JikanAuthorEntity> {
        override fun transform(source: JikanItem) = JikanAuthorEntity(
        jikanId = source.jikanId,
        type = source.model.type,
        name = source.model.name,
        id = source.model.malId,
        )
    }
}

internal class JikanLicensorModelConverter(
    override val fromType: (JikanItem) -> JikanLicensorEntity = ::transform,
    override val toType: (JikanLicensorEntity) -> JikanItem = { throw NotImplementedError() }
) : SupportConverter<JikanItem, JikanLicensorEntity>() {
    private companion object : ISupportTransformer<JikanItem, JikanLicensorEntity> {
        override fun transform(source: JikanItem) = JikanLicensorEntity(
        jikanId = source.jikanId,
        type = source.model.type,
        name = source.model.name,
        id = source.model.malId,
        )
    }
}

internal class JikanProducerModelConverter(
    override val fromType: (JikanItem) -> JikanProducerEntity = ::transform,
    override val toType: (JikanProducerEntity) -> JikanItem = { throw NotImplementedError() }
) : SupportConverter<JikanItem, JikanProducerEntity>() {
    private companion object : ISupportTransformer<JikanItem, JikanProducerEntity> {
        override fun transform(source: JikanItem) = JikanProducerEntity(
        jikanId = source.jikanId,
        type = source.model.type,
        name = source.model.name,
        id = source.model.malId,
        )
    }
}

internal class JikanStudioModelConverter(
    override val fromType: (JikanItem) -> JikanStudioEntity = ::transform,
    override val toType: (JikanStudioEntity) -> JikanItem = { throw NotImplementedError() }
) : SupportConverter<JikanItem, JikanStudioEntity>() {
    private companion object : ISupportTransformer<JikanItem, JikanStudioEntity> {
        override fun transform(source: JikanItem) = JikanStudioEntity(
            jikanId = source.jikanId,
            type = source.model.type,
            name = source.model.name,
            id = source.model.malId,
        )
    }
}
