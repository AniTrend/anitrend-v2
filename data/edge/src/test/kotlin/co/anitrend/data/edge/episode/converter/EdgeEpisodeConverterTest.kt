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
package co.anitrend.data.edge.episode.converter

import co.anitrend.data.edge.graphql.EpisodesData
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EdgeEpisodeConverterTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

    @Test
    fun `generated episodes payload decodes and maps into local entity`() {
        val payload =
            """
            {
              "episodes": {
                "data": [
                  {
                    "absoluteEpisodeNumber": 13,
                    "aired": 1710000999,
                    "airedAfterEpisodeNumber": null,
                    "airedAfterSeasonNumber": null,
                    "airedBeforeEpisodeNumber": null,
                    "airedBeforeSeasonNumber": null,
                    "duration": 24,
                    "episodeNumber": 1,
                    "id": 1001,
                    "image": "image.jpg",
                    "kind": "MAIN",
                    "poster": "poster.jpg",
                    "seasonNumber": 2,
                    "synopsis": "Episode synopsis",
                    "title": {
                      "english": "Episode One",
                      "native": "Episode Native",
                      "romanji": "Episode Romaji"
                    },
                    "tmdbId": 7001,
                    "tvdbId": 2002,
                    "tvdbShowId": 3003,
                    "url": "https://example.com/episode"
                  }
                ]
              }
            }
            """.trimIndent()

        val result = json.decodeFromString<EpisodesData>(payload)
        val model = assertNotNull(result.episodes?.data.orEmpty().first())

        val mapped = EdgeEpisodeConverter().convertFromOrNull("media-id" to model)

        assertNotNull(mapped)
        assertEquals("media-id", mapped.mediaId)
        assertEquals(2, mapped.seasonNumber)
        assertEquals(1, mapped.episodeNumber)
        assertEquals("Episode One", mapped.name)
        assertEquals("Episode synopsis", mapped.overview)
        assertEquals("image.jpg", mapped.image)
        assertEquals("poster.jpg", mapped.poster)
        assertEquals(24, mapped.runtime)
        assertEquals(13, mapped.absoluteEpisodeNumber)
        assertEquals(1710000999L, mapped.airDate)
    }

    @Test
    fun `converter drops incomplete episode rows`() {
        val model =
            sampleEpisode(
                aired = null,
            )

        val mapped = EdgeEpisodeConverter().convertFromOrNull("media-id" to model)

        assertNull(mapped)
    }

    @Test
    fun `converter rejects fractional season numbers instead of truncating`() {
        val model =
            sampleEpisode(
                seasonNumber = 1.5,
            )

        assertFailsWith<IllegalArgumentException> {
            EdgeEpisodeConverter().convertFromOrNull("media-id" to model)
        }
    }

    @Test
    fun `converter rejects fractional episode numbers instead of truncating`() {
        val model =
            sampleEpisode(
                episodeNumber = 1.5,
            )

        assertFailsWith<IllegalArgumentException> {
            EdgeEpisodeConverter().convertFromOrNull("media-id" to model)
        }
    }

    private fun sampleEpisode(
        aired: Double? = 1710000999.0,
        episodeNumber: Double = 1.0,
        seasonNumber: Double = 2.0,
    ) =
        EpisodesData.EpisodesData(
            absoluteEpisodeNumber = 13.0,
            aired = aired,
            airedAfterEpisodeNumber = null,
            airedAfterSeasonNumber = null,
            airedBeforeEpisodeNumber = null,
            airedBeforeSeasonNumber = null,
            duration = 24.0,
            episodeNumber = episodeNumber,
            id = 1001.0,
            image = "image.jpg",
            kind = null,
            poster = "poster.jpg",
            seasonNumber = seasonNumber,
            synopsis = "Episode synopsis",
            title =
                EpisodesData.EpisodesDataTitle(
                    english = "Episode One",
                    native = "Episode Native",
                    romanji = "Episode Romaji",
                ),
            tmdbId = 7001.0,
            tvdbId = 2002.0,
            tvdbShowId = 3003.0,
            url = "https://example.com/episode",
        )
}
