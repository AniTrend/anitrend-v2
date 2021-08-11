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

package co.anitrend.data.staff.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.common.extension.asFuzzyDate
import co.anitrend.data.staff.entity.StaffEntity
import co.anitrend.data.staff.model.StaffModel
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName
import co.anitrend.domain.staff.entity.Staff

internal class StaffConverter(
    override val fromType: (StaffModel) -> Staff = ::transform,
    override val toType: (Staff) -> StaffModel = { throw NotImplementedError() }
) : SupportConverter<StaffModel, Staff>() {
    private companion object : ISupportTransformer<StaffModel, Staff> {
        override fun transform(source: StaffModel) = when (source) {
            is StaffModel.Core -> Staff.Core(
                age = source.age,
                dateOfBirth = source.dateOfBirth?.asFuzzyDate(),
                dateOfDeath = source.dateOfDeath?.asFuzzyDate(),
                gender = source.gender,
                homeTown = source.homeTown,
                bloodType = source.bloodType,
                primaryOccupations = source.primaryOccupations.orEmpty(),
                yearsActive = Staff.ActiveYearPeriod(
                    start = source.yearsActive.firstOrNull(),
                    end = source.yearsActive.lastOrNull(),
                ),
                description = source.description,
                favourites = source.favourites,
                image = source.image?.let { image ->
                    CoverImage(
                        large = image.large,
                        medium = image.medium
                    )
                },
                isFavourite = source.isFavourite,
                isFavouriteBlocked = source.isFavouriteBlocked,
                language = source.language,
                name = source.name?.let { name ->
                    CoverName(
                        alternative = name.alternative.orEmpty(),
                        alternativeSpoiler = name.alternativeSpoiler.orEmpty(),
                        first = name.first,
                        full = name.full,
                        last = name.last,
                        middle = name.middle,
                        native = name.native,
                        userPreferred = name.userPreferred
                    )
                },
                siteUrl = source.siteUrl,
                id = source.id
            )
            is StaffModel.Extended -> Staff.Extended(
                age = source.age,
                dateOfBirth = source.dateOfBirth?.asFuzzyDate(),
                dateOfDeath = source.dateOfDeath?.asFuzzyDate(),
                gender = source.gender,
                homeTown = source.homeTown,
                bloodType = source.bloodType,
                primaryOccupations = source.primaryOccupations.orEmpty(),
                yearsActive = Staff.ActiveYearPeriod(
                    start = source.yearsActive.firstOrNull(),
                    end = source.yearsActive.lastOrNull(),
                ),
                description = source.description,
                favourites = source.favourites,
                image = source.image?.let { image ->
                    CoverImage(
                        large = image.large,
                        medium = image.medium
                    )
                },
                isFavourite = source.isFavourite,
                isFavouriteBlocked = source.isFavouriteBlocked,
                language = source.language,
                name = source.name?.let { name ->
                    CoverName(
                        alternative = name.alternative.orEmpty(),
                        alternativeSpoiler = name.alternativeSpoiler.orEmpty(),
                        first = name.first,
                        full = name.full,
                        last = name.last,
                        middle = name.middle,
                        native = name.native,
                        userPreferred = name.userPreferred
                    )
                },
                siteUrl = source.siteUrl,
                id = source.id
            )
        }
    }
}

internal class StaffModelConverter(
    override val fromType: (StaffModel) -> StaffEntity = ::transform,
    override val toType: (StaffEntity) -> StaffModel = { throw NotImplementedError() }
) : SupportConverter<StaffModel, StaffEntity>() {
    private companion object : ISupportTransformer<StaffModel, StaffEntity> {
        override fun transform(source: StaffModel): StaffEntity {
            TODO("Not yet implemented")
        }
    }
}

internal class StaffEntityConverter(
    override val fromType: (StaffEntity) -> Staff = ::transform,
    override val toType: (Staff) -> StaffEntity = { throw NotImplementedError() }
) : SupportConverter<StaffEntity, Staff>() {
    private companion object : ISupportTransformer<StaffEntity, Staff> {
        override fun transform(source: StaffEntity): Staff {
            TODO("Not yet implemented")
        }
    }
}