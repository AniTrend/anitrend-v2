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

package co.anitrend.domain.media.entity

import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.link.MediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import co.anitrend.domain.media.entity.base.IMediaExtended
import co.anitrend.domain.media.entity.base.IMediaExtendedWithMediaList
import co.anitrend.domain.media.entity.contract.MediaCategory
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.medialist.entity.base.IMediaListCore
import co.anitrend.domain.tag.entity.Tag

data class Media(
    override val sourceId: MediaSourceId,
    override val origin: CountryCode,
    override val description: CharSequence?,
    override val externalLinks: MediaExternalLink,
    override val favouritesCount: UInt,
    override val genres: Collection<CharSequence>,
    override val twitterTag: CharSequence?,
    override val isLicensed: Boolean,
    override val isLocked: Boolean,
    override val isRecommendationBlocked: Boolean,
    override val rankings: Collection<MediaRank>,
    override val siteUrl: CharSequence?,
    override val source: MediaSource?,
    override val synonyms: Collection<CharSequence>,
    override val tags: Collection<Tag>,
    override val trailer: MediaTrailer?,
    override val format: MediaFormat?,
    override val season: MediaSeason?,
    override val status: MediaStatus?,
    override val meanScore: UInt,
    override val averageScore: UInt,
    override val startDate: FuzzyDate,
    override val endDate: FuzzyDate,
    override val title: MediaTitle,
    override val image: MediaImage,
    override val category: MediaCategory,
    override val isAdult: Boolean,
    override val isFavourite: Boolean,
    override val id: Long,
    override val mediaListEntry: IMediaListCore? = null
) : IMediaExtended, IMediaExtendedWithMediaList