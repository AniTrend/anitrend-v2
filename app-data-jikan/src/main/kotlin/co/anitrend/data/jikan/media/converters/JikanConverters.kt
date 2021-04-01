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
import co.anitrend.data.jikan.media.entity.JikanEntity
import co.anitrend.data.jikan.media.model.anime.JikanMediaModel

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
    jikanId: Long,
    override val fromType: (JikanMediaModel.Attribute) -> JikanEntity.AuthorEntity = { transform(it, jikanId) },
    override val toType: (JikanEntity.AuthorEntity) -> JikanMediaModel.Attribute = { throw NotImplementedError() }
) : SupportConverter<JikanMediaModel.Attribute, JikanEntity.AuthorEntity>() {
    private companion object {
        fun transform(source: JikanMediaModel.Attribute, jikanId: Long) = JikanEntity.AuthorEntity(
            jikanId = jikanId,
            type = source.type,
            name = source.name,
            id = source.malId
        )
    }
}

internal class JikanLicensorModelConverter(
    jikanId: Long,
    override val fromType: (JikanMediaModel.Attribute) -> JikanEntity.LicensorEntity = { transform(it, jikanId) },
    override val toType: (JikanEntity.LicensorEntity) -> JikanMediaModel.Attribute = { throw NotImplementedError() }
) : SupportConverter<JikanMediaModel.Attribute, JikanEntity.LicensorEntity>() {
    private companion object {
        fun transform(source: JikanMediaModel.Attribute, jikanId: Long) = JikanEntity.LicensorEntity(
            jikanId = jikanId,
            type = source.type,
            name = source.name,
            id = source.malId
        )
    }
}

internal class JikanProducerModelConverter(
    jikanId: Long,
    override val fromType: (JikanMediaModel.Attribute) -> JikanEntity.ProducerEntity = { transform(it, jikanId) },
    override val toType: (JikanEntity.ProducerEntity) -> JikanMediaModel.Attribute = { throw NotImplementedError() }
) : SupportConverter<JikanMediaModel.Attribute, JikanEntity.ProducerEntity>() {
    private companion object {
        fun transform(source: JikanMediaModel.Attribute, jikanId: Long) = JikanEntity.ProducerEntity(
            jikanId = jikanId,
            type = source.type,
            name = source.name,
            id = source.malId
        )
    }
}

internal class JikanStudioModelConverter(
    jikanId: Long,
    override val fromType: (JikanMediaModel.Attribute) -> JikanEntity.StudioEntity = { transform(it, jikanId) },
    override val toType: (JikanEntity.StudioEntity) -> JikanMediaModel.Attribute = { throw NotImplementedError() }
) : SupportConverter<JikanMediaModel.Attribute, JikanEntity.StudioEntity>() {
    private companion object {
        fun transform(source: JikanMediaModel.Attribute, jikanId: Long) = JikanEntity.StudioEntity(
            jikanId = jikanId,
            type = source.type,
            name = source.name,
            id = source.malId
        )
    }
}