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
import co.anitrend.domain.common.extension.INVALID_ID
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.link.MediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import co.anitrend.domain.media.entity.base.IMedia
import co.anitrend.domain.media.entity.base.IMediaExtended
import co.anitrend.domain.media.entity.base.IMediaExtendedWithMediaList
import co.anitrend.domain.media.entity.contract.MediaCategory
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.base.IMediaListCore
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.tag.entity.Tag

data class Media(
    override val sourceId: MediaSourceId,
    override val origin: CountryCode?,
    override val description: CharSequence?,
    override val externalLinks: Collection<MediaExternalLink>,
    override val favouritesCount: Long,
    override val genres: Collection<CharSequence>,
    override val twitterTag: CharSequence?,
    override val isLicensed: Boolean?,
    override val isLocked: Boolean?,
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
    override val scoreFormat: ScoreFormat?,
    override val meanScore: Int,
    override val averageScore: Int,
    override val startDate: FuzzyDate,
    override val endDate: FuzzyDate,
    override val title: MediaTitle,
    override val image: MediaImage,
    override val category: MediaCategory,
    override val isAdult: Boolean?,
    override val isFavourite: Boolean,
    override val id: Long,
    override val mediaListEntry: MediaList
) : IMediaExtendedWithMediaList {

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is IMedia -> id == other.id
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = sourceId.hashCode()
        result = 31 * result + (origin?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + externalLinks.hashCode()
        result = 31 * result + favouritesCount.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + (twitterTag?.hashCode() ?: 0)
        result = 31 * result + (isLicensed?.hashCode() ?: 0)
        result = 31 * result + (isLocked?.hashCode() ?: 0)
        result = 31 * result + isRecommendationBlocked.hashCode()
        result = 31 * result + rankings.hashCode()
        result = 31 * result + (siteUrl?.hashCode() ?: 0)
        result = 31 * result + (source?.hashCode() ?: 0)
        result = 31 * result + synonyms.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + (trailer?.hashCode() ?: 0)
        result = 31 * result + (format?.hashCode() ?: 0)
        result = 31 * result + (season?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (scoreFormat?.hashCode() ?: 0)
        result = 31 * result + meanScore
        result = 31 * result + averageScore
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + (isAdult?.hashCode() ?: 0)
        result = 31 * result + isFavourite.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + mediaListEntry.hashCode()
        return result
    }

    companion object {
        fun empty() = Media(
            sourceId = MediaSourceId.empty(),
            origin = null,
            description = null,
            externalLinks = emptyList(),
            favouritesCount = 0,
            genres = emptyList(),
            twitterTag = null,
            isLicensed = null,
            isLocked = null,
            isRecommendationBlocked = false,
            rankings = emptyList(),
            siteUrl = null,
            source = null,
            synonyms = emptyList(),
            tags = emptyList(),
            trailer = null,
            format = null,
            season = null,
            status = null,
            scoreFormat = null,
            meanScore = 0,
            averageScore = 0,
            startDate = FuzzyDate.empty(),
            endDate = FuzzyDate.empty(),
            title = MediaTitle.empty(),
            image = MediaImage.empty(),
            category = MediaCategory.Anime.empty(),
            isAdult = null,
            isFavourite = false,
            id = INVALID_ID,
            mediaListEntry = MediaList.empty()
        )
    }
}