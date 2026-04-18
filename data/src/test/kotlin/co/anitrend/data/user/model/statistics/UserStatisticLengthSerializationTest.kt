/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.user.model.statistics

import co.anitrend.data.user.model.statistics.media.UserStatisticAnimeModel
import co.anitrend.data.user.model.statistics.media.UserStatisticMangaModel
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserStatisticLengthSerializationTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `anime length bucket accepts null length values`() {
        val payload =
            """
            {
              "length": null,
              "count": 5,
              "meanScore": 81.5,
              "mediaIds": [1, 2, 3],
              "minutesWatched": 1440
            }
            """.trimIndent()

        val result = json.decodeFromString(UserStatisticAnimeModel.Length.serializer(), payload)

        assertNull(result.length)
        assertEquals(5, result.count)
        assertEquals(1440, result.minutesWatched)
    }

    @Test
    fun `manga length bucket accepts null length values`() {
        val payload =
            """
            {
              "length": null,
              "count": 2,
              "meanScore": 76.0,
              "mediaIds": [8, 13],
              "chaptersRead": 220
            }
            """.trimIndent()

        val result = json.decodeFromString(UserStatisticMangaModel.Length.serializer(), payload)

        assertNull(result.length)
        assertEquals(2, result.count)
        assertEquals(220, result.chaptersRead)
    }
}
