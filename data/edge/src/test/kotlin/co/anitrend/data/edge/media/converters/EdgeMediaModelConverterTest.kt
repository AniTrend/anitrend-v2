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
package co.anitrend.data.edge.media.converters

import co.anitrend.data.edge.graphql.AnimeThemeType
import co.anitrend.data.edge.graphql.GetMediaByIdData
import co.anitrend.data.edge.graphql.SeriesFormat
import co.anitrend.data.edge.graphql.SeriesImageType
import co.anitrend.data.edge.graphql.SeriesKind
import co.anitrend.data.edge.graphql.SeriesNetworkCategory
import co.anitrend.data.edge.graphql.SeriesSource
import co.anitrend.data.edge.graphql.SeriesStatus
import co.anitrend.data.edge.image.converter.EdgeImageConverter
import co.anitrend.data.edge.image.entity.EdgeMediaImageEntity
import co.anitrend.data.edge.network.converter.EdgeNetworkConverter
import co.anitrend.data.edge.theme.model.name
import co.anitrend.data.edge.trailer.converter.EdgeTrailerConverter
import co.anitrend.domain.media.enums.MediaType
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EdgeMediaModelConverterTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

    @Test
    fun `generated series payload decodes and maps into edge media entity`() {
        val result = json.decodeFromString<GetMediaByIdData>(generatedSeriesPayload)

        val series = assertNotNull(result.series)
        assertEquals(1, series.animethemes.orEmpty().size)
        assertEquals("OP 1", series.animethemes.orEmpty().first()?.name)

        val entity = EdgeMediaModelConverter().convertFrom(series)

        assertEquals("notify-id", entity.id)
        assertEquals(MediaType.ANIME, entity.kind)
        assertEquals("English Title", entity.title.english)
        assertEquals("Japanese Title", entity.title.japanese)
        assertEquals(listOf("Synonym One"), entity.title.synonyms)
        assertEquals("TV", entity.format)
        assertEquals("MANGA", entity.source)
        assertEquals("RELEASING", entity.status)
        assertEquals(12, entity.airedEpisodes)
        assertEquals(15125L, entity.externalIds.aniList)
        assertEquals(100L, entity.externalIds.myAnimeList)
        assertEquals(200L, entity.externalIds.tmdb)
        assertEquals(300L, entity.externalIds.tvDb)
        assertEquals(1600000000L, entity.publishedFrom)
        assertEquals(1720000000L, entity.updatedAt)
        assertNotNull(entity.schedule)
        assertEquals(1700000000L, entity.schedule.firstAirDate)
        assertEquals(5002L, entity.schedule.nextEpisodeId)
        assertEquals(5001L, entity.schedule.lastEpisodeId)
        val nextEpisode = assertNotNull(entity.schedule.nextEpisode)
        assertEquals(13, nextEpisode.episodeNumber)
        assertEquals(6002L, nextEpisode.tmdbId)
        val lastEpisode = assertNotNull(entity.schedule.lastEpisode)
        assertEquals("Last Aired", lastEpisode.name)
        assertEquals(1, lastEpisode.seasonNumber)
    }

    @Test
    fun `converter derives entity id from slug when notify is absent`() {
        val series =
            sampleSeries(
                mediaId =
                    sampleMediaId(
                        notify = null,
                        slug = "sample-series",
                    ),
            )

        val entity = EdgeMediaModelConverter().convertFrom(series)

        assertEquals("sample-series", entity.id)
        assertEquals(15125L, entity.externalIds.aniList)
        assertNull(entity.schedule)
    }

    @Test
    fun `converter rejects fractional external ids instead of truncating`() {
        val series =
            sampleSeries(
                mediaId =
                    sampleMediaId(
                        anilist = 15125.5,
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            EdgeMediaModelConverter().convertFrom(series)
        }
    }

    @Test
    fun `converter rejects fractional timestamps instead of truncating`() {
        val series =
            sampleSeries(
                updatedAt = 1720000000.25,
            )

        assertFailsWith<IllegalArgumentException> {
            EdgeMediaModelConverter().convertFrom(series)
        }
    }

    @Test
    fun `image, network and trailer converters adapt generated payloads`() {
        val series =
            sampleSeries(
                mediaId = sampleMediaId(),
                images =
                    listOf(
                        sampleImage(url = "backdrop.jpg", type = SeriesImageType.BACKDROP, height = 1080.0, width = 1920.0),
                        sampleImage(url = "logo.png", type = SeriesImageType.LOGO, height = 200.0, width = 800.0),
                    ),
                networks =
                    listOf(
                        sampleNetwork(),
                    ),
                trailers =
                    listOf(
                        GetMediaByIdData.SeriesTrailers(
                            id = "trailer-key",
                            site = "youtube",
                            thumbnail = null,
                        ),
                    ),
            )

        val images = series.images.orEmpty().mapNotNull { it }
        assertEquals(2, images.size)
        val imageEntity = EdgeImageConverter().convertFrom(MEDIA_ID to images.first())
        assertEquals(1080, imageEntity.height)
        assertEquals(1920, imageEntity.width)
        assertEquals(EdgeMediaImageEntity.ImageType.BACKDROP, imageEntity.type)
        assertEquals("en", imageEntity.locale)

        val networkEntity = EdgeNetworkConverter().convertFrom(MEDIA_ID to series.networks.orEmpty().first()!!)
        assertEquals(77L, networkEntity.networkId)
        assertEquals("PRODUCTION", networkEntity.category)

        val trailerEntity = EdgeTrailerConverter().convertFrom(MEDIA_ID to series.trailers.orEmpty().first()!!)
        assertEquals("trailer-key", trailerEntity.trailerId)
        assertEquals("youtube", trailerEntity.site)
        assertNull(trailerEntity.thumbnail)
    }

    @Test
    fun `image converter rejects fractional dimensions instead of truncating`() {
        val series =
            sampleSeries(
                images =
                    listOf(
                        sampleImage(height = 1080.5),
                    ),
            )

        val image = series.images.orEmpty().first()!!

        assertFailsWith<IllegalArgumentException> {
            EdgeImageConverter().convertFrom(MEDIA_ID to image)
        }
    }

    @Test
    fun `converter maps generated enums to persisted values`() {
        val series =
            sampleSeries().copy(
                format = SeriesFormat.MOVIE,
                source = SeriesSource.ORIGINAL,
                status = SeriesStatus.FINISHED,
                kind = SeriesKind.MANGA,
            )

        val entity = EdgeMediaModelConverter().convertFrom(series)

        assertEquals("MOVIE", entity.format)
        assertEquals("ORIGINAL", entity.source)
        assertEquals("FINISHED", entity.status)
        assertEquals(MediaType.MANGA, entity.kind)
    }
}

private const val MEDIA_ID = "media-id"

private val generatedSeriesPayload =
    """
    {
      "series": {
        "title": {
          "canonical": "Canonical Title",
          "english": "English Title",
          "harigana": null,
          "japanese": "Japanese Title",
          "romaji": "Romaji Title",
          "synonyms": ["Synonym One", null]
        },
        "cover": {
          "color": "#FFFFFF",
          "extraLarge": "xl.jpg",
          "large": "large.jpg",
          "medium": "medium.jpg"
        },
        "banner": "banner.jpg",
        "description": "Description",
        "fanart": "fanart.jpg",
        "format": "TV",
        "homepage": "https://example.com",
        "airedEpisodes": 12,
        "broadcast": "Saturdays",
        "source": "MANGA",
        "status": "RELEASING",
        "ageRating": "PG",
        "isAdult": false,
        "kind": "ANIME",
        "chapters": null,
        "volumes": null,
        "moreInfo": "more",
        "publishedFrom": 1600000000,
        "publishedTo": null,
        "mediaId": {
          "anidb": null,
          "anilist": 15125,
          "animePlanet": null,
          "anisearch": null,
          "imdb": null,
          "kitsu": null,
          "livechart": null,
          "myanimelist": 100,
          "notify": "notify-id",
          "shoboi": null,
          "slug": null,
          "themoviedb": 200,
          "trakt": null,
          "tvMazeId": null,
          "tvdb": 300,
          "tvrage": null
        },
        "images": [
          {
            "height": 1080,
            "locale": "en",
            "type": "BACKDROP",
            "url": "backdrop.jpg",
            "width": 1920
          }
        ],
        "schedule": {
          "firstAirDate": 1700000000,
          "lastAirDate": 1710000000,
          "lastAiredEpisode": {
            "airDate": 1710000000,
            "episodeNumber": 12,
            "id": 5001,
            "image": "still.jpg",
            "name": "Last Aired",
            "overview": "Overview",
            "productionCode": "PC1",
            "runtime": 24,
            "seasonNumber": 1,
            "tmdbId": 6001
          },
          "nextEpisodeToAir": {
            "airDate": 1710003600,
            "episodeNumber": 13,
            "id": 5002,
            "image": "still2.jpg",
            "name": "Next Episode",
            "overview": "Overview 2",
            "productionCode": "PC2",
            "runtime": 24,
            "seasonNumber": 1,
            "tmdbId": 6002
          }
        },
        "networks": [
          {
            "category": "DISTRIBUTION",
            "id": 77,
            "isPrimary": true,
            "logoPath": "logo/77.png",
            "name": "Network One",
            "originCountry": "JP"
          }
        ],
        "animethemes": [
          {
            "animethemeentries": [
              {
                "episodes": "1-12",
                "id": 100,
                "notes": "TV",
                "nsfw": false,
                "spoiler": false,
                "version": 1,
                "videos": [
                  {
                    "audio": {
                      "id": 9000,
                      "link": "audio.mp3"
                    },
                    "id": 1000,
                    "link": "video.mp4",
                    "lyrics": false,
                    "nc": false,
                    "overlap": null,
                    "resolution": 1080,
                    "source": "WEB",
                    "subbed": false,
                    "tags": null,
                    "uncen": false
                  }
                ]
              }
            ],
            "id": 1,
            "sequence": 1,
            "slug": "opening-1",
            "song": {
              "id": 50,
              "title": "OP 1"
            },
            "type": "OP"
          }
        ],
        "trailers": [
          {
            "id": "trailer-key",
            "site": "youtube",
            "thumbnail": "thumb.jpg"
          }
        ],
        "updatedAt": 1720000000,
        "volumes": null
      }
    }
    """.trimIndent()

private fun sampleMediaId(
    anilist: Double? = 15125.0,
    notify: String? = "notify-id",
    slug: String? = null,
) =
    GetMediaByIdData.SeriesMediaId(
        anidb = null,
        anilist = anilist,
        animePlanet = null,
        anisearch = null,
        imdb = null,
        kitsu = null,
        livechart = null,
        myanimelist = 100.0,
        notify = notify,
        shoboi = null,
        slug = slug,
        themoviedb = 200.0,
        trakt = null,
        tvMazeId = null,
        tvdb = 300.0,
        tvrage = null,
    )

private fun sampleImage(
    url: String = "backdrop.jpg",
    height: Double = 1080.0,
    width: Double = 1920.0,
    type: SeriesImageType = SeriesImageType.BACKDROP,
) =
    GetMediaByIdData.SeriesImages(
        height = height,
        locale = "en",
        type = type,
        url = url,
        width = width,
    )

private fun sampleNetwork() =
    GetMediaByIdData.SeriesNetworks(
        category = SeriesNetworkCategory.PRODUCTION,
        id = 77.0,
        isPrimary = true,
        logoPath = "logo/77.png",
        name = "Network One",
        originCountry = "JP",
    )

private fun sampleSeries(
    mediaId: GetMediaByIdData.SeriesMediaId = sampleMediaId(),
    images: List<GetMediaByIdData.SeriesImages> = emptyList(),
    networks: List<GetMediaByIdData.SeriesNetworks> = emptyList(),
    trailers: List<GetMediaByIdData.SeriesTrailers> = emptyList(),
    updatedAt: Double = 1720000000.0,
) =
    GetMediaByIdData.Series(
        ageRating = null,
        airedEpisodes = 12.0,
        animethemes = emptyList(),
        banner = null,
        broadcast = null,
        chapters = null,
        cover =
            GetMediaByIdData.SeriesCover(
                color = null,
                extraLarge = null,
                large = null,
                medium = "medium.jpg",
            ),
        description = null,
        fanart = null,
        format = null,
        homepage = null,
        images = images,
        isAdult = null,
        kind = SeriesKind.ANIME,
        mediaId = mediaId,
        moreInfo = null,
        networks = networks,
        publishedFrom = null,
        publishedTo = null,
        schedule = null,
        source = null,
        status = null,
        title =
            GetMediaByIdData.SeriesTitle(
                canonical = "Canonical Title",
                english = "English Title",
                harigana = null,
                japanese = null,
                romaji = "Romaji Title",
                synonyms = null,
            ),
        trailers = trailers,
        updatedAt = updatedAt,
        volumes = null,
    )
