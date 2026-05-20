package co.anitrend.data.media.converter

import co.anitrend.data.edge.episode.entity.EdgeEpisodeEntity
import co.anitrend.data.edge.image.entity.EdgeMediaImageEntity
import co.anitrend.data.edge.media.entity.EdgeMediaCoverEntity
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.entity.EdgeMediaExternalIdsEntity
import co.anitrend.data.edge.media.entity.EdgeMediaScheduleEntity
import co.anitrend.data.edge.media.entity.EdgeMediaScheduleEpisodeEntity
import co.anitrend.data.edge.media.entity.EdgeMediaTitleEntity
import co.anitrend.data.edge.media.entity.view.EdgeMediaEntityView
import co.anitrend.data.edge.trailer.entity.EdgeTrailerEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaGalleryImage
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MediaEntityViewConverterTest {
    private val converter = MediaEntityViewConverter()

    @Test
    fun `given edge broadcast and assets when converting extended media then broadcast and gallery stay enriched`() {
        val source =
            MediaEntityView.Extended(
                media =
                    MediaEntity(
                        coverImage =
                            MediaEntity.CoverImage(
                                color = "#101820",
                                extraLarge = "https://cdn.example.com/poster-xl.jpg",
                                large = "https://cdn.example.com/poster-l.jpg",
                                medium = "https://cdn.example.com/poster-m.jpg",
                                banner = "https://cdn.example.com/banner.jpg",
                            ),
                        title =
                            MediaEntity.Title(
                                romaji = "Kikan no Test",
                                english = "Seasonal Test",
                                original = "季刊のテスト",
                                userPreferred = "Seasonal Test",
                            ),
                        trailer = null,
                        nextAiringId = null,
                        averageScore = 84,
                        chapters = null,
                        countryOfOrigin = null,
                        description = "Testing media mapping",
                        duration = 24,
                        endDate = null,
                        episodes = 12,
                        favourites = 4_200,
                        format = MediaFormat.TV,
                        hashTag = null,
                        isAdult = false,
                        isFavourite = false,
                        isFavouriteBlocked = false,
                        isLicensed = false,
                        isRecommendationBlocked = false,
                        isReviewBlocked = false,
                        isLocked = false,
                        meanScore = 82,
                        popularity = 88_000,
                        season = MediaSeason.WINTER,
                        siteUrl = "https://anilist.co/anime/1001",
                        source = MediaSource.ORIGINAL,
                        startDate = "20250103",
                        status = MediaStatus.RELEASING,
                        synonyms = emptyList(),
                        trending = 120,
                        type = MediaType.ANIME,
                        updatedAt = 1_736_000_000L,
                        volumes = null,
                        malId = 5001L,
                        id = 1001L,
                    ),
                mediaList = null,
                nextAiring = null,
                genres = emptyList(),
                links = emptyList(),
                ranks = emptyList(),
                tags = emptyList(),
                edge =
                    EdgeMediaEntityView(
                        media =
                            EdgeMediaEntity(
                                title =
                                    EdgeMediaTitleEntity(
                                        canonical = "Seasonal Test",
                                        english = "Seasonal Test",
                                        harigana = null,
                                        japanese = "季刊のテスト",
                                        romaji = "Kikan no Test",
                                        synonyms = listOf("Season Test"),
                                    ),
                                cover =
                                    EdgeMediaCoverEntity(
                                        medium = "https://cdn.example.com/edge-medium.jpg",
                                        large = "https://cdn.example.com/edge-large.jpg",
                                        extraLarge = "https://cdn.example.com/edge-xl.jpg",
                                        color = "#101820",
                                    ),
                                banner = "https://cdn.example.com/edge-banner.jpg",
                                description = "Edge description",
                                fanart = "https://cdn.example.com/fanart.jpg",
                                format = "TV",
                                homepage = "https://seasonal.example.com",
                                airedEpisodes = 5,
                                broadcast = "  Fridays 24:30 JST  ",
                                source = "ORIGINAL",
                                status = "CURRENT",
                                ageRating = "PG-13",
                                isAdult = false,
                                kind = MediaType.ANIME,
                                chapters = null,
                                volumes = null,
                                moreInfo = "Extra edge info",
                                publishedFrom = null,
                                publishedTo = null,
                                externalIds =
                                    EdgeMediaExternalIdsEntity(
                                        aniDb = null,
                                        aniList = 1001L,
                                        animePlanet = null,
                                        aniSearch = null,
                                        imdb = null,
                                        kitsu = null,
                                        liveChart = null,
                                        myAnimeList = 5001L,
                                        notify = null,
                                        shoboi = null,
                                        slug = null,
                                        tmdb = null,
                                        trakt = null,
                                        tvDb = null,
                                        tvMaze = null,
                                        tvRage = null,
                                    ),
                                schedule =
                                    EdgeMediaScheduleEntity(
                                        firstAirDate = 1_735_948_800L,
                                        lastAirDate = 1_738_454_400L,
                                        nextEpisodeId = 6L,
                                        lastEpisodeId = 5L,
                                        nextEpisode = null,
                                        lastEpisode =
                                            EdgeMediaScheduleEpisodeEntity(
                                                id = 5L,
                                                airDate = 1_737_849_600L,
                                                episodeNumber = 5,
                                                image = null,
                                                name = "Signal Lost",
                                                overview = null,
                                                productionCode = null,
                                                runtime = 24,
                                                seasonNumber = 1,
                                                tmdbId = null,
                                            ),
                                    ),
                                updatedAt = 1_738_500_000L,
                                id = "edge-1001",
                            ),
                        networks = emptyList(),
                        trailers =
                            listOf(
                                EdgeTrailerEntity(
                                    mediaId = "edge-1001",
                                    trailerId = "abc123",
                                    site = "youtube",
                                    thumbnail = "https://img.youtube.com/vi/abc123/maxresdefault.jpg",
                                ),
                            ),
                        images =
                            listOf(
                                EdgeMediaImageEntity(
                                    mediaId = "edge-1001",
                                    type = EdgeMediaImageEntity.ImageType.POSTER,
                                    url = "https://cdn.example.com/poster-1.jpg",
                                    height = 900,
                                    width = 600,
                                    locale = "en-US",
                                ),
                            ),
                        episodes =
                            listOf(
                                EdgeEpisodeEntity(
                                    mediaId = "edge-1001",
                                    seasonNumber = 1,
                                    episodeNumber = 4,
                                    name = "Static Lines",
                                    overview = "A quiet setup episode before the signal collapses.",
                                    image = "https://cdn.example.com/episode-4.jpg",
                                    poster = null,
                                    runtime = 24,
                                    absoluteEpisodeNumber = 4,
                                    airDate = 1_737_244_800L,
                                    id = 4L,
                                ),
                                EdgeEpisodeEntity(
                                    mediaId = "edge-1001",
                                    seasonNumber = 1,
                                    episodeNumber = 5,
                                    name = "Signal Lost",
                                    overview = "The transmission cuts out right after the city blackout begins.",
                                    image = "https://cdn.example.com/episode-5.jpg",
                                    poster = null,
                                    runtime = 24,
                                    absoluteEpisodeNumber = 5,
                                    airDate = 1_737_849_600L,
                                    id = 5L,
                                ),
                            ),
                        seasons = emptyList(),
                        themes = emptyList(),
                    ),
            )

        val result = converter.convertFrom(source) as Media.Extended
        val category = result.category as Media.Category.Anime

        assertEquals("Fridays 24:30 JST", category.broadcast)
        assertEquals(5, category.scheduleDetails?.airedEpisodes)
        assertEquals(listOf("https://cdn.example.com/fanart.jpg", "https://cdn.example.com/poster-1.jpg"), result.gallery.map { it.url })
        assertEquals(listOf(MediaGalleryImage.Type.BACKDROP, MediaGalleryImage.Type.POSTER), result.gallery.map { it.type })
        assertEquals(1, result.trailers.size)
        assertEquals("abc123", result.trailer?.id)
        assertNotNull(category.scheduleDetails?.lastEpisode)
        assertEquals(5, category.scheduleDetails?.lastEpisode?.episodeNumber)
        assertEquals(listOf(4, 5), category.scheduleDetails?.episodes?.map { it.episodeNumber })
        assertEquals("https://cdn.example.com/episode-4.jpg", category.scheduleDetails?.episodes?.firstOrNull()?.image)
    }
}
