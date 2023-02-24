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

package co.anitrend.domain.media.entity.contract

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.common.entity.contract.IFavourable
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.common.entity.contract.ISynopsis
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.base.IMediaList

interface IMedia : IEntity, IFavourable, ISynopsis {
    val title: IMediaTitle
    val image: IMediaCover
    val isAdult: Boolean?
    val format: MediaFormat?
    val season: MediaSeason?
    val status: MediaStatus?
    val meanScore: Int
    val averageScore: Int
    val startDate: FuzzyDate
    val endDate: FuzzyDate
    val mediaList: IMediaList?
}