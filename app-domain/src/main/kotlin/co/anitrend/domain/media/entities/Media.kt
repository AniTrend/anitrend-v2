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

package co.anitrend.domain.media.entities

import co.anitrend.domain.airing.entities.AiringSchedule
import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.common.entity.IEntity
import co.anitrend.domain.common.model.FuzzyDate
import co.anitrend.domain.media.contract.IMediaExternalLink
import co.anitrend.domain.media.contract.IMediaRank
import co.anitrend.domain.media.contract.IMediaTitle
import co.anitrend.domain.media.contract.IMediaTrailer
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.tag.entities.Tag

data class Media(
    override val id: Long,
    val attributes: MediaAttributes,
    val countryOfOrigin: CountryCode?,
    val coverImage: MediaImage?,
    val description: String?,
    val endDate: FuzzyDate?,
    val externalLinks: Collection<IMediaExternalLink>,
    val favourites: Int,
    val genres: Collection<String>,
    val hashtag: String?,
    val isAdult: Boolean?,
    val isFavourite: Boolean,
    val isLicensed: Boolean?,
    val isLocked: Boolean?,
    val nextAiringEpisode: AiringSchedule?,
    val rankings: Collection<IMediaRank>,
    val season: MediaSeason?,
    val siteUrl: String?,
    val source: MediaSource?,
    val startDate: FuzzyDate?,
    val status: MediaStatus?,
    val synonyms: Collection<String>,
    val tags: Collection<Tag>,
    val title: IMediaTitle?,
    val trailer: IMediaTrailer?,
    val trending: Int?,
    val updatedAt: Long?
) : IEntity