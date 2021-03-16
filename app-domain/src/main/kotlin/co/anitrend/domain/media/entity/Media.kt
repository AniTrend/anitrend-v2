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

import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.extension.INVALID_ID
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.link.IMediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.IMediaSourceId
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.IMediaTrailer
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.base.IMediaList
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.tag.entity.Tag

sealed class Media : IMedia {

    abstract val trailer: IMediaTrailer?
    abstract val sourceId: IMediaSourceId
    abstract val countryCode: CharSequence?
    abstract val description: CharSequence?
    abstract val favouritesCount: Long
    abstract val genres: Collection<Genre.Extended>
    abstract val twitterTag: CharSequence?
    abstract val isLicensed: Boolean?
    abstract val isLocked: Boolean?
    abstract val isRecommendationBlocked: Boolean
    abstract val siteUrl: CharSequence?
    abstract val source: MediaSource?
    abstract val synonyms: Collection<CharSequence>
    abstract val tags: Collection<Tag>
    abstract val category: Category

    /**
     * A category type of media
     *
     * @param type A [MediaType] enum
     */
    sealed class Category(val type: MediaType) {

        /**
         * Japanese Anime
         *
         * @param episodes The amount of episodes the anime has when complete
         * @param duration The general length of each anime episode in minutes
         * @param schedule The media's next episode airing schedule
         */
        data class Anime(
            val episodes: Int,
            val duration: Int,
            val schedule: AiringSchedule?
        ) : Category(MediaType.ANIME) {
            companion object {
                fun empty() = Anime(
                    0,
                    0,
                    null
                )
            }
        }

        /**
         * Asian comic
         *
         * @param chapters The amount of chapters the manga has when complete
         * @param volumes The amount of volumes the manga has when complete
         */
        data class Manga(
            val chapters: Int,
            val volumes: Int
        ) : Category(MediaType.MANGA) {
            companion object {
                fun empty() = Manga(
                    0,
                    0
                )
            }
        }
    }

    data class Core(
        override val title: IMediaTitle,
        override val image: IMediaCover,
        override val category: Category,
        override val isAdult: Boolean?,
        override val isFavourite: Boolean,
        override val format: MediaFormat?,
        override val season: MediaSeason?,
        override val status: MediaStatus?,
        override val meanScore: Int,
        override val averageScore: Int,
        override val startDate: FuzzyDate,
        override val endDate: FuzzyDate,
        override val mediaList: IMediaList?,
        override val id: Long,
        override val sourceId: IMediaSourceId,
        override val countryCode: CharSequence?,
        override val description: CharSequence?,
        override val favouritesCount: Long,
        override val genres: Collection<Genre.Extended>,
        override val twitterTag: CharSequence?,
        override val isLicensed: Boolean?,
        override val isLocked: Boolean?,
        override val isRecommendationBlocked: Boolean,
        override val siteUrl: CharSequence?,
        override val source: MediaSource?,
        override val synonyms: Collection<CharSequence>,
        override val tags: Collection<Tag>,
        override val trailer: IMediaTrailer?
    ) : Media() {
        companion object {
            fun empty() = Core(
                sourceId = MediaSourceId.empty(),
                countryCode = null,
                description = null,
                favouritesCount = 0,
                genres = emptyList(),
                twitterTag = null,
                isLicensed = null,
                isLocked = null,
                isRecommendationBlocked = false,
                siteUrl = null,
                source = null,
                synonyms = emptyList(),
                tags = emptyList(),
                format = null,
                season = null,
                status = null,
                meanScore = 0,
                averageScore = 0,
                startDate = FuzzyDate.empty(),
                endDate = FuzzyDate.empty(),
                title = MediaTitle.empty(),
                image = MediaImage.empty(),
                category = Category.Anime.empty(),
                isAdult = null,
                isFavourite = false,
                id = INVALID_ID,
                mediaList = null,
                trailer = null
            )
        }
    }

    data class Extended(
        val externalLinks: Collection<IMediaExternalLink>,
        val rankings: Collection<IMediaRank>,
        override val trailer: IMediaTrailer?,
        override val title: IMediaTitle,
        override val image: IMediaCover,
        override val category: Category,
        override val isAdult: Boolean?,
        override val isFavourite: Boolean,
        override val format: MediaFormat?,
        override val season: MediaSeason?,
        override val status: MediaStatus?,
        override val meanScore: Int,
        override val averageScore: Int,
        override val startDate: FuzzyDate,
        override val endDate: FuzzyDate,
        override val mediaList: IMediaList?,
        override val id: Long,
        override val sourceId: IMediaSourceId,
        override val countryCode: CharSequence?,
        override val description: CharSequence?,
        override val favouritesCount: Long,
        override val genres: Collection<Genre.Extended>,
        override val twitterTag: CharSequence?,
        override val isLicensed: Boolean?,
        override val isLocked: Boolean?,
        override val isRecommendationBlocked: Boolean,
        override val siteUrl: CharSequence?,
        override val source: MediaSource?,
        override val synonyms: Collection<CharSequence>,
        override val tags: Collection<Tag>
    ) : Media() {
        companion object {
            fun empty() = Extended(
                sourceId = MediaSourceId.empty(),
                countryCode = null,
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
                meanScore = 0,
                averageScore = 0,
                startDate = FuzzyDate.empty(),
                endDate = FuzzyDate.empty(),
                title = MediaTitle.empty(),
                image = MediaImage.empty(),
                category = Category.Anime.empty(),
                isAdult = null,
                isFavourite = false,
                id = INVALID_ID,
                mediaList = null
            )
        }
    }
}