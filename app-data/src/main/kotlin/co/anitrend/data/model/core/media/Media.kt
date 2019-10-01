/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.model.core.media

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.data.model.core.airing.AiringSchedule
import co.anitrend.domain.media.entities.IMedia
import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.common.model.FuzzyDate
import co.anitrend.domain.common.model.MediaCover
import co.anitrend.domain.media.enums.*

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 *
 *
 */
@Entity
data class Media(
    @PrimaryKey
    override val id: Long,
    override val averageScore: Int?,
    override val bannerImage: String?,
    override val chapters: Int?,
    override val countryOfOrigin: CountryCode?,
    override val coverImage: MediaCover?,
    override val description: String?,
    override val duration: Int?,
    override val endDate: FuzzyDate?,
    override val episodes: Int?,
    override val externalLinks: List<MediaExternalLink>?,
    override val favourites: Int,
    override val format: MediaFormat?,
    override val genres: List<String>?,
    override val hashtag: String?,
    override val idMal: Long?,
    override val isAdult: Boolean?,
    override val isFavourite: Boolean,
    override val isLicensed: Boolean?,
    override val isLocked: Boolean?,
    override val meanScore: Int?,
    override val nextAiringEpisode: AiringSchedule?,
    override val popularity: Int?,
    override val rankings: List<MediaRank>?,
    override val season: MediaSeason?,
    override val siteUrl: String?,
    override val source: MediaSource?,
    override val startDate: FuzzyDate?,
    override val status: MediaStatus?,
    override val synonyms: List<String>?,
    override val tags: List<MediaTag>?,
    override val title: MediaTitle?,
    override val trailer: MediaTrailer?,
    override val trending: Int?,
    override val type: MediaType?,
    override val updatedAt: Long?,
    override val volumes: Int?
) : IMedia