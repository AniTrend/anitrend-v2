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
import co.anitrend.data.jikan.producer.entity.JikanProducerEntity
import co.anitrend.data.jikan.studio.entity.JikanStudioEntity

internal class JikanModelConverter(
    override val fromType: (JikanMediaModel) -> JikanEntity = ::transform,
    override val toType: (JikanEntity) -> JikanMediaModel = { throw NotImplementedError() }
) : SupportConverter<JikanMediaModel, JikanEntity>() {
    private companion object : ISupportTransformer<JikanMediaModel, JikanEntity> {
        override fun transform(source: JikanMediaModel) = when(source) {
            is JikanMediaModel.Anime -> JikanEntity(
                title = JikanEntity.Title(
                    japanese = source.titleJapanese,
                    english = source.titleEnglish,
                    preferred = source.title,
                    synonyms = source.titleSynonyms.orEmpty()
                ),
                info = source.moreInfo,
                source = source.source,
                rating = source.rating,
                episodes = source.episodes,
                duration = source.duration,
                premiered = source.premiered,
                broadcast = source.broadcast,
                trailerUrl = source.trailerUrl,
                openingThemes = source.openingThemes.orEmpty(),
                endingThemes = source.endingThemes.orEmpty(),
                url = source.url,
                imageUrl = source.imageUrl,
                type = source.type,
                releasing = source.releasing,
                synopsis = source.synopsis,
                background = source.background,
                id = source.malId,
            )
            is JikanMediaModel.Manga -> JikanEntity(
                title = JikanEntity.Title(
                    japanese = source.titleJapanese,
                    english = source.titleEnglish,
                    preferred = source.title,
                    synonyms = source.titleSynonyms.orEmpty()
                ),
                info = source.moreInfo,
                volumes = source.volumes,
                chapters = source.chapters,
                url = source.url,
                imageUrl = source.imageUrl,
                type = source.type,
                releasing = source.releasing,
                synopsis = source.synopsis,
                background = source.background,
                id = source.malId,
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