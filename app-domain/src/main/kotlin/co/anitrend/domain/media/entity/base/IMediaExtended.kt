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

package co.anitrend.domain.media.entity.base

import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.attribute.link.MediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.IMediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.entity.attribute.trailer.IMediaTrailer
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.tag.entity.Tag

interface IMediaExtended : IMediaCore {
    val sourceId: IMediaSourceId
    val countryCode: CharSequence?
    val description: CharSequence?
    val externalLinks: Collection<MediaExternalLink>
    val favouritesCount: Long
    val genres: Collection<Genre>
    val twitterTag: CharSequence?
    val isLicensed: Boolean?
    val isLocked: Boolean?
    val isRecommendationBlocked: Boolean
    val rankings: Collection<IMediaRank>
    val siteUrl: CharSequence?
    val source: MediaSource?
    val synonyms: Collection<CharSequence>
    val tags: Collection<Tag>
    val trailer: IMediaTrailer?
}