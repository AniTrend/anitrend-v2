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

import co.anitrend.data.edge.episode.model.EdgeEpisodeModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EdgeEpisodeConverterTest {
    @Test
    fun `converter maps schema shaped episode into local entity`() {
        val model =
            EdgeEpisodeModel(
                absoluteEpisodeNumber = 13,
                aired = 1710000999L,
                duration = 24,
                episodeNumber = 1,
                id = 1001L,
                image = "image.jpg",
                synopsis = "Episode synopsis",
                title =
                    EdgeEpisodeModel.EdgeEpisodeTitleModel(
                        english = "Episode One",
                        native = "Episode Native",
                        romanji = "Episode Romaji",
                    ),
                poster = "poster.jpg",
                seasonNumber = 2,
                tvdbId = 2002L,
                tvdbShowId = 3003L,
            )

        val result = EdgeEpisodeConverter().convertFromOrNull("media-id" to model)

        assertNotNull(result)
        assertEquals("media-id", result.mediaId)
        assertEquals(2, result.seasonNumber)
        assertEquals(1, result.episodeNumber)
        assertEquals("Episode One", result.name)
        assertEquals("Episode synopsis", result.overview)
        assertEquals(24, result.runtime)
        assertEquals(1710000999L, result.airDate)
    }

    @Test
    fun `converter drops incomplete episode rows`() {
        val model =
            EdgeEpisodeModel(
                aired = null,
                episodeNumber = 1,
                seasonNumber = 1,
            )

        val result = EdgeEpisodeConverter().convertFromOrNull("media-id" to model)

        assertNull(result)
    }
}
