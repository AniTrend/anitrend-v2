/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.media.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.common.extension.asFuzzyDate
import co.anitrend.data.core.extensions.koinOf
import co.anitrend.data.edge.media.entity.EdgeMediaExternalIdsEntity
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.entity.EdgeMediaScheduleEntity
import co.anitrend.data.edge.media.entity.EdgeMediaScheduleEpisodeEntity
import co.anitrend.data.edge.media.entity.view.EdgeMediaEntityView
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.medialist.converter.MediaListEntityViewConverter
import co.anitrend.data.medialist.entity.view.MediaListEntityView
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.tag.entity.view.TagEntityView
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaGalleryImage
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.link.MediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.theme.MediaTheme
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.tag.entity.Tag

internal class MediaEntityViewConverter(
    override val fromType: (MediaEntityView) -> Media = ::transform,
    override val toType: (Media) -> MediaEntityView = { throw NotImplementedError() },
) : SupportConverter<MediaEntityView, Media>() {
    private companion object : ISupportTransformer<MediaEntityView, Media> {
        private fun String?.asMediaSource(): MediaSource? =
            this?.let {
                runCatching {
                    MediaSource.valueOf(it.uppercase())
                }.getOrNull()
            }

        private fun MediaListEntityView.createMediaList(): MediaList = koinOf<MediaListEntityViewConverter>().convertFrom(this)

        private fun MediaEntity.createSiteUrl(): Media.SiteUrl =
            Media.SiteUrl(
                aniList = siteUrl,
                myAnimeList =
                    malId?.let {
                        "https://myanimelist.net/${type.name.lowercase()}/$it"
                    },
            )

        private fun MediaEntityView.edge(): EdgeMediaEntityView? =
            when (this) {
                is MediaEntityView.Core -> edge
                is MediaEntityView.Extended -> edge
            }

        private fun MediaEntityView.links(): List<LinkEntity> =
            when (this) {
                is MediaEntityView.Extended -> links
                else -> emptyList()
            }

        private fun MediaEntityView.ranks(): List<RankEntity> =
            when (this) {
                is MediaEntityView.Extended -> ranks
                else -> emptyList()
            }

        private fun MediaEntityView.tags(): List<TagEntityView> =
            when (this) {
                is MediaEntityView.Extended -> tags
                else -> emptyList()
            }

        private fun EdgeMediaExternalIdsEntity?.toSourceId(fallbackAniList: Long) =
            MediaSourceId(
                aniDb = this?.aniDb,
                aniList = this?.aniList ?: fallbackAniList,
                animePlanet = this?.animePlanet,
                aniSearch = this?.aniSearch,
                imdb = this?.imdb,
                kitsu = this?.kitsu,
                liveChart = this?.liveChart,
                myAnimeList = this?.myAnimeList,
                notify = this?.notify,
                shoboi = this?.shoboi,
                slug = this?.slug,
                tmdb = this?.tmdb,
                trakt = this?.trakt,
                tvDb = this?.tvDb,
                tvMaze = this?.tvMaze,
                tvRage = this?.tvRage,
            )

        private fun String?.normalizedText(): String? =
            this
                ?.trim()
                ?.takeIf(String::isNotBlank)

        private fun MediaEntity.Trailer.toDomainTrailer() =
            MediaTrailer(
                id = id,
                site = site?.normalizedText(),
                thumbnail = thumbnail?.normalizedText(),
            )

        private fun EdgeMediaEntityView.createTrailers(mediaTrailer: MediaEntity.Trailer?): List<MediaTrailer> {
            val preferredTrailer = mediaTrailer?.toDomainTrailer()
            return buildList {
                preferredTrailer?.let(::add)
                trailers.forEach { trailer ->
                    add(
                        MediaTrailer(
                            id = trailer.trailerId,
                            site = trailer.site.normalizedText(),
                            thumbnail = trailer.thumbnail?.normalizedText(),
                        ),
                    )
                }
            }.distinctBy { it.site?.toString()?.lowercase() to it.id?.toString() }
        }

        private fun EdgeMediaEntityView.createGallery(): List<MediaGalleryImage> {
            val fanart =
                media.fanart
                    ?.normalizedText()
                    ?.let {
                        MediaGalleryImage(
                            url = it,
                            type = MediaGalleryImage.Type.BACKDROP,
                            width = 0,
                            height = 0,
                            locale = null,
                        )
                    }

            return buildList {
                fanart?.let(::add)
                images.forEach { image ->
                    add(
                        MediaGalleryImage(
                            url = image.url,
                            type =
                                when (image.type) {
                                    co.anitrend.data.edge.image.entity.EdgeMediaImageEntity.ImageType.BACKDROP -> MediaGalleryImage.Type.BACKDROP
                                    co.anitrend.data.edge.image.entity.EdgeMediaImageEntity.ImageType.LOGO -> MediaGalleryImage.Type.LOGO
                                    co.anitrend.data.edge.image.entity.EdgeMediaImageEntity.ImageType.POSTER -> MediaGalleryImage.Type.POSTER
                                },
                            width = image.width,
                            height = image.height,
                            locale = image.locale?.normalizedText(),
                        ),
                    )
                }
            }.distinctBy(MediaGalleryImage::url)
        }

        private fun EdgeMediaScheduleEpisodeEntity?.toDomainEpisode() =
            this?.let {
                Media.Category.Anime.ScheduleDetails.Episode(
                    id = it.id,
                    airDate = it.airDate,
                    episodeNumber = it.episodeNumber,
                    image = it.image?.normalizedText(),
                    name = it.name?.normalizedText(),
                    overview = it.overview?.normalizedText(),
                    productionCode = it.productionCode?.normalizedText(),
                    runtime = it.runtime,
                    seasonNumber = it.seasonNumber,
                    tmdbId = it.tmdbId,
                )
            }

        private fun co.anitrend.data.edge.episode.entity.EdgeEpisodeEntity.toDomainEpisode() =
            Media.Category.Anime.ScheduleDetails.Episode(
                id = id,
                airDate = airDate,
                episodeNumber = episodeNumber,
                image = image?.normalizedText() ?: poster?.normalizedText(),
                name = name?.normalizedText(),
                overview = overview?.normalizedText(),
                productionCode = null,
                runtime = runtime,
                seasonNumber = seasonNumber,
                tmdbId = null,
            )

        private fun EdgeMediaEntityView.createScheduleEpisodes(
            schedule: EdgeMediaScheduleEntity?,
        ): List<Media.Category.Anime.ScheduleDetails.Episode> =
            buildList {
                addAll(episodes.map { it.toDomainEpisode() })
                schedule?.lastEpisode?.toDomainEpisode()?.let(::add)
                schedule?.nextEpisode?.toDomainEpisode()?.let(::add)
            }.distinctBy { episode ->
                listOfNotNull(
                    episode.seasonNumber,
                    episode.episodeNumber,
                ).takeIf(List<Int>::isNotEmpty)?.joinToString(":") ?: (episode.id?.toString() ?: episode.airDate?.toString())
            }.sortedWith(
                compareBy<Media.Category.Anime.ScheduleDetails.Episode> {
                    it.seasonNumber ?: Int.MAX_VALUE
                }.thenBy {
                    it.episodeNumber ?: Int.MAX_VALUE
                }.thenBy {
                    it.airDate ?: Long.MAX_VALUE
                },
            )

        private fun MediaEntityView.createMedia(): Media.Core {
            val edge = edge()
            val links = links()
            val ranks = ranks()
            val tags = tags()
            val edgeTrailers = edge?.createTrailers(media.trailer).orEmpty()

            val nextAiringSchedule =
                nextAiring
                    ?.let {
                        AiringSchedule(
                            airingAt = it.airingAt,
                            episode = it.episode,
                            mediaId = media.id,
                            timeUntilAiring = it.timeUntilAiring,
                            id = it.id,
                        )
                    }
                    ?: edge?.media?.schedule?.nextEpisode?.let { next ->
                        val airingAt = next.airDate ?: 0
                        val timeUntilAiring = next.airDate?.let { date -> maxOf(0L, date - (System.currentTimeMillis() / 1000)) } ?: 0
                        AiringSchedule(
                            airingAt = airingAt,
                            episode = next.episodeNumber ?: 0,
                            mediaId = media.id,
                            timeUntilAiring = timeUntilAiring,
                            id = next.id ?: next.tmdbId ?: 0,
                        )
                    }

            return Media.Core(
                countryCode = media.countryOfOrigin,
                description = media.description ?: edge?.media?.description,
                favourites = media.favourites,
                genres =
                    genres.map {
                        Genre.Extended(
                            id = it.genre.id,
                            name = it.genre.genre,
                            emoji = it.genre.emoji,
                            background = media.coverImage.color,
                        )
                    },
                twitterTag = media.hashTag,
                isLicensed = media.isLicensed,
                isLocked = media.isLocked,
                isRecommendationBlocked = media.isRecommendationBlocked,
                isReviewBlocked = media.isReviewBlocked,
                siteUrl = media.createSiteUrl(),
                source = media.source ?: edge?.media?.source.asMediaSource(),
                synonyms =
                    media.synonyms.ifEmpty {
                        edge
                            ?.media
                            ?.title
                            ?.synonyms
                            .orEmpty()
                    },
                trailer = edgeTrailers.firstOrNull(),
                format = media.format,
                season = media.season,
                status = media.status,
                score =
                    MediaScore(
                        mean = media.meanScore ?: 0,
                        average = media.averageScore ?: 0,
                        personal = mediaList?.mediaList?.score,
                        popularity = media.popularity,
                        trending = media.trending,
                    ),
                startDate = media.startDate.asFuzzyDate(),
                endDate = media.endDate.asFuzzyDate(),
                title =
                    MediaTitle(
                        romaji = media.title.romaji ?: edge?.media?.title?.romaji,
                        english = media.title.english ?: edge?.media?.title?.english,
                        native = media.title.original ?: edge?.media?.title?.japanese,
                        userPreferred = media.title.userPreferred ?: edge?.media?.title?.canonical,
                    ),
                image =
                    MediaImage(
                        color = media.coverImage.color ?: edge?.media?.cover?.color,
                        extraLarge = media.coverImage.extraLarge ?: edge?.media?.cover?.extraLarge,
                        large = media.coverImage.large ?: edge?.media?.cover?.large,
                        medium = media.coverImage.medium ?: edge?.media?.cover?.medium,
                        banner = media.coverImage.banner ?: edge?.media?.banner ?: edge?.media?.fanart,
                    ),
                category =
                    when (media.type) {
                        MediaType.ANIME ->
                            Media.Category.Anime(
                                media.episodes ?: edge?.media?.airedEpisodes ?: 0,
                                media.duration ?: edge
                                    ?.media
                                    ?.schedule
                                    ?.nextEpisode
                                    ?.runtime ?: 0,
                                broadcast = edge?.media?.broadcast.normalizedText(),
                                premiered =
                                    edge
                                        ?.media
                                        ?.schedule
                                        ?.firstAirDate
                                        ?.toString(),
                                schedule = nextAiringSchedule,
                                scheduleDetails =
                                    edge?.media?.schedule?.let { schedule ->
                                        Media.Category.Anime.ScheduleDetails(
                                            airedEpisodes = edge.media.airedEpisodes,
                                            firstAirDate = schedule.firstAirDate,
                                            lastAirDate = schedule.lastAirDate,
                                            nextEpisode = schedule.nextEpisode.toDomainEpisode(),
                                            lastEpisode = schedule.lastEpisode.toDomainEpisode(),
                                            episodes = edge.createScheduleEpisodes(schedule),
                                        )
                                    } ?: edge?.let {
                                        Media.Category.Anime.ScheduleDetails(
                                            airedEpisodes = it.media.airedEpisodes,
                                            firstAirDate = null,
                                            lastAirDate = null,
                                            episodes = it.createScheduleEpisodes(schedule = null),
                                        )
                                    },
                            )

                        MediaType.MANGA ->
                            Media.Category.Manga(
                                media.chapters ?: edge?.media?.chapters ?: 0,
                                media.volumes ?: edge?.media?.volumes ?: 0,
                            )
                    },
                isAdult = media.isAdult ?: edge?.media?.isAdult,
                isFavourite = media.isFavourite,
                isFavouriteBlocked = media.isFavouriteBlocked,
                id = media.id,
                mediaList = mediaList?.createMediaList(),
                externalLinks =
                    links.map {
                        MediaExternalLink(
                            color = it.color,
                            icon = it.icon,
                            isDisabled = it.isDisabled,
                            language = it.language,
                            notes = it.notes,
                            siteId = it.siteId,
                            linkType = it.linkType,
                            site = it.site,
                            url = it.url,
                            id = it.id,
                        )
                    },
                rankings =
                    ranks.map {
                        MediaRank(
                            allTime = it.allTime,
                            context = it.context,
                            format = it.format,
                            rank = it.rank,
                            season = it.season,
                            type = it.type,
                            year = it.year,
                            id = it.id,
                        )
                    },
                tags =
                    tags.map {
                        Tag.Extended(
                            name = it.tag.name,
                            description = it.tag.description,
                            category = it.tag.category,
                            rank = it.connection.rank,
                            isGeneralSpoiler = it.tag.isGeneralSpoiler,
                            isMediaSpoiler = it.connection.isMediaSpoiler,
                            isAdult = it.tag.isAdult,
                            id = it.tag.id,
                            background = media.coverImage.color,
                        )
                    },
            )
        }

        override fun transform(source: MediaEntityView) =
            when (source) {
                is MediaEntityView.Core -> source.createMedia()
                is MediaEntityView.Extended ->
                    source.createMedia().let { media ->
                        val edge = source.edge
                        val edgeTrailers = edge?.createTrailers(source.media.trailer).orEmpty()
                        Media.Extended(
                            sourceId =
                                edge
                                    ?.media
                                    ?.externalIds
                                    .toSourceId(media.id),
                            background = edge?.media?.fanart ?: edge?.media?.banner,
                            ageRating = edge?.media?.ageRating,
                            extraInfo = edge?.media?.moreInfo,
                            themes =
                                edge
                                    ?.themes
                                    ?.map {
                                        val firstVideo = it.entries.firstNotNullOfOrNull { entry -> entry.videos.firstOrNull() }
                                        val version =
                                            it.entries
                                                .firstOrNull()
                                                ?.entry
                                                ?.version ?: 0
                                        val variants =
                                            it.entries.map { entryView ->
                                                MediaTheme.Variant(
                                                    version = entryView.entry.version,
                                                    episodes = entryView.entry.episodes,
                                                    previews =
                                                        entryView.videos.map { video ->
                                                            MediaTheme.Preview(
                                                                video = video.link,
                                                                audio = video.audioLink,
                                                                resolution = video.resolution,
                                                                source = video.source,
                                                                tags =
                                                                    buildList {
                                                                        if (video.nc) add("NC")
                                                                        if (video.subbed) add("SUB")
                                                                        if (video.lyrics) add("LYRICS")
                                                                        if (video.uncen) add("UNCEN")
                                                                    },
                                                            )
                                                        },
                                                )
                                            }

                                        MediaTheme(
                                            mediaId = it.theme.mediaId,
                                            themeId = it.theme.themeId,
                                            name = it.theme.songTitle,
                                            audio = firstVideo?.audioLink,
                                            video = firstVideo?.link.orEmpty(),
                                            meta =
                                                MediaTheme.Meta(
                                                    number = it.theme.sequence,
                                                    type = it.theme.type,
                                                    version = version,
                                                ),
                                            variants = variants,
                                        )
                                    }.orEmpty(),
                            trailers = edgeTrailers,
                            gallery = edge?.createGallery().orEmpty(),
                            countryCode = media.countryCode,
                            description = media.description ?: edge?.media?.description,
                            externalLinks = media.externalLinks,
                            favourites = media.favourites,
                            genres = media.genres,
                            twitterTag = media.twitterTag,
                            isLicensed = media.isLicensed,
                            isLocked = media.isLocked,
                            isRecommendationBlocked = media.isRecommendationBlocked,
                            isReviewBlocked = media.isReviewBlocked,
                            rankings = media.rankings,
                            siteUrl = media.siteUrl,
                            source = media.source,
                            synonyms = media.synonyms,
                            tags = media.tags,
                            trailer = media.trailer,
                            format = media.format,
                            season = media.season,
                            status = media.status,
                            score = media.score,
                            startDate = media.startDate,
                            endDate = media.endDate,
                            title = media.title,
                            image = media.image,
                            category = media.category,
                            isAdult = media.isAdult,
                            isFavourite = media.isFavourite,
                            isFavouriteBlocked = media.isFavouriteBlocked,
                            id = media.id,
                            mediaList = media.mediaList,
                        )
                    }
            }
    }
}
