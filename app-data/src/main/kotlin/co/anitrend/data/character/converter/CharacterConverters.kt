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

package co.anitrend.data.character.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.character.entity.CharacterEntity
import co.anitrend.data.character.model.remote.CharacterModel
import co.anitrend.data.common.extension.asFuzzyDate
import co.anitrend.data.common.extension.toFuzzyDateInt
import co.anitrend.domain.character.entity.Character
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName

internal class CharacterConverter(
    override val fromType: (CharacterModel) -> Character = ::transform,
    override val toType: (Character) -> CharacterModel = { throw NotImplementedError() }
) : SupportConverter<CharacterModel,Character>() {
    private companion object : ISupportTransformer<CharacterModel,Character> {
        override fun transform(source: CharacterModel) = when (source) {
            is CharacterModel.Core -> Character.Core(
                age = source.age,
                dateOfBirth = source.dateOfBirth.asFuzzyDate(),
                gender = source.gender,
                bloodType = source.bloodType,
                description = source.description,
                image = CoverImage(
                    large = source.image?.large,
                    medium = source.image?.medium,
                ),
                name = CoverName(
                    middle = source.name?.middle,
                    alternativeSpoiler = source.name?.alternativeSpoiler.orEmpty(),
                    alternative = source.name?.alternative.orEmpty(),
                    first = source.name?.first,
                    full = source.name?.full,
                    last = source.name?.last,
                    native = source.name?.native,
                    userPreferred = source.name?.userPreferred,
                ),
                siteUrl = source.siteUrl,
                favourites = source.favourites ?: 0,
                isFavourite = source.isFavourite,
                isFavouriteBlocked = source.isFavouriteBlocked,
                id = source.id
            )
            is CharacterModel.Extended -> TODO()
        }
    }
}

internal class CharacterModelConverter(
    override val fromType: (CharacterModel) -> CharacterEntity = ::transform,
    override val toType: (CharacterEntity) -> CharacterModel = { throw NotImplementedError() }
) : SupportConverter<CharacterModel,CharacterEntity>() {
    private companion object : ISupportTransformer<CharacterModel, CharacterEntity> {
        override fun transform(source: CharacterModel) = CharacterEntity(
            age = source.age,
            dateOfBirth = source.dateOfBirth?.toFuzzyDateInt(),
            gender = source.gender,
            bloodType = source.bloodType,
            description = source.description,
            favourites = source.favourites ?: 0,
            image = CharacterEntity.CoverImage(
                large = source.image?.large,
                medium = source.image?.medium
            ),
            isFavourite = source.isFavourite,
            isFavouriteBlocked = source.isFavouriteBlocked,
            name = CharacterEntity.Name(
                alternatives = source.name?.alternative,
                alternativeSpoiler = source.name?.alternativeSpoiler,
                first = source.name?.first,
                full = source.name?.full,
                last = source.name?.last,
                middle = source.name?.middle,
                original = source.name?.native,
                userPreferred = source.name?.userPreferred,
            ),
            siteUrl = source.siteUrl,
            id = source.id,
        )
    }
}

internal class CharacterEntityConverter(
    override val fromType: (CharacterEntity) -> Character = ::transform,
    override val toType: (Character) -> CharacterEntity = { throw NotImplementedError() }
) : SupportConverter<CharacterEntity,Character>() {
    private companion object : ISupportTransformer<CharacterEntity,Character> {
        override fun transform(source: CharacterEntity) = Character.Core(
            age = source.age,
            dateOfBirth = source.dateOfBirth.asFuzzyDate(),
            gender = source.gender,
            bloodType = source.bloodType,
            description = source.description,
            image = CoverImage(
                large = source.image.large,
                medium = source.image.medium,
            ),
            name = CoverName(
                middle = source.name.middle,
                alternativeSpoiler = source.name.alternativeSpoiler.orEmpty(),
                alternative = source.name.alternatives.orEmpty(),
                first = source.name.first,
                full = source.name.full,
                last = source.name.last,
                native = source.name.original,
                userPreferred = source.name.userPreferred,
            ),
            siteUrl = source.siteUrl,
            favourites = source.favourites,
            isFavourite = source.isFavourite,
            isFavouriteBlocked = source.isFavouriteBlocked,
            id = source.id
        )

    }
}