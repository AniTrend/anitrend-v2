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
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.medialist.converter.MediaListEntityViewConverter
import co.anitrend.data.medialist.entity.view.MediaListEntityView
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
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
        private fun MediaListEntityView.createMediaList(): MediaList = koinOf<MediaListEntityViewConverter>().convertFrom(this)

        private fun MediaEntity.createSiteUrl(): Media.SiteUrl =
            Media.SiteUrl(
                aniList = siteUrl,
                myAnimeList =
                    malId?.let {
                        "https://myanimelist.net/${type.name.lowercase()}/$it"
                    },
            )

        private fun MediaEntityView.createMedia(): Media =
            Media.Core(
                sourceId =
                    MediaSourceId(
                        aniDb = edge?.media?.externalIds?.aniDb,
                        aniList = edge?.media?.externalIds?.aniList,
                        animePlanet = edge?.media?.externalIds?.animePlanet,
                        aniSearch = edge?.media?.externalIds?.aniSearch,
                        imdb = edge?.media?.externalIds?.imdb,
                        kitsu = edge?.media?.externalIds?.kitsu,
                        liveChart = edge?.media?.externalIds?.liveChart,
                        myAnimeList = edge?.media?.externalIds?.myAnimeList,
                        notify = edge?.media?.externalIds?.notify,
                        shoboi = edge?.media?.externalIds?.shoboi,
                        slug = edge?.media?.externalIds?.slug,
                        tmdb = edge?.media?.externalIds?.tmdb,
                        trakt = edge?.media?.externalIds?.trakt,
                        tvDb = edge?.media?.externalIds?.tvDb,
                        tvMaze = edge?.media?.externalIds?.tvMaze,
                        tvRage = edge?.media?.externalIds?.tvRage,
                    ),
                countryCode = media.countryOfOrigin,
                description = media.description ?: edge?.media?.description,
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
                siteUrl = media.createSiteUrl(),
                source =
                    media.source ?: edge?.media?.source?.let {
                        runCatching {
                            MediaSource.valueOf(it.uppercase())
                        }.getOrNull()
                    },
                synonyms =
                    media.synonyms.let {
                        it.ifEmpty {
                            edge
                                ?.media
                                ?.title
                                ?.synonyms
                                .orEmpty()
                        }
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
                trailer =
                    media.trailer?.let {
                        MediaTrailer(
                            id = it.id,
                            site = it.site,
                            thumbnail = it.thumbnail,
                        )
                    },
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
                        romaji = media.title.romaji ?: edge?.media?.title?.japanese,
                        english = media.title.english ?: edge?.media?.title?.english,
                        native = media.title.original,
                        userPreferred = media.title.userPreferred ?: edge?.media?.title?.canonical,
                    ),
                image =
                    MediaImage(
                        color = media.coverImage.color ?: edge?.media?.cover?.color,
                        extraLarge = media.coverImage.extraLarge ?: edge?.media?.cover?.extraLarge,
                        large = media.coverImage.large ?: edge?.media?.cover?.large,
                        medium = media.coverImage.medium ?: edge?.media?.cover?.medium,
                        banner = media.coverImage.banner ?: edge?.media?.banner,
                    ),
                category =
                    when (media.type) {
                        MediaType.ANIME ->
                            Media.Category.Anime(
                                media.episodes ?: edge?.media?.airedEpisodes ?: 0,
                                media.duration ?: 0,
                                broadcast = null, // edge?.networks?.firstOrNull { it.name },
                                premiered = null, // edge?.media?.schedule.firstAirDate,
                                nextAiring?.let {
                                    AiringSchedule(
                                        airingAt = it.airingAt,
                                        episode = it.episode,
                                        mediaId = media.id,
                                        timeUntilAiring = it.timeUntilAiring,
                                        id = it.id,
                                    )
                                },
                            )

                        MediaType.MANGA ->
                            Media.Category.Manga(
                                media.chapters ?: 0, // ?: jikan?.chapters,
                                media.volumes ?: 0, // ?: jikan?.volumes,
                            )
                    },
                isAdult = media.isAdult,
                isFavourite = media.isFavourite,
                isFavouriteBlocked = media.isFavouriteBlocked,
                id = media.id,
                mediaList = mediaList?.createMediaList(),
            )

        override fun transform(source: MediaEntityView) =
            when (source) {
                is MediaEntityView.Core -> source.createMedia()
                is MediaEntityView.Extended ->
                    source.createMedia().let { media ->
                        Media.Extended(
                            sourceId = media.sourceId,
                            background = null, // source.edge?.media,
                            ageRating = source.edge?.media?.ageRating,
                            extraInfo = null, // source.jikan?.info,
                            themes =
                                source.edge
                                    ?.themes
                                    ?.map {
                                        MediaTheme(
                                            mediaId = it.mediaId,
                                            themeId = it.themeId,
                                            name = it.name,
                                            audio = it.audio,
                                            video = it.video,
                                            meta =
                                                MediaTheme.Meta(
                                                    number = it.meta.number,
                                                    type = it.meta.type,
                                                    version = it.meta.version,
                                                ),
                                        )
                                    }.orEmpty(),
                            countryCode = media.countryCode,
                            description = media.description,
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
