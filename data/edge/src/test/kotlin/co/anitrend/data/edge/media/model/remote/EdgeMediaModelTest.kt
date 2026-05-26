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
package co.anitrend.data.edge.media.model.remote

import co.anitrend.data.edge.media.converters.EdgeMediaModelConverter
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EdgeMediaModelTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

    @Test
    fun `series payload decodes typed animethemes from json array`() {
        val payload =
            """
            {
              "series": {
                "title": {
                  "canonical": "Canonical Title",
                  "english": "English Title",
                  "romaji": "Romaji Title"
                },
                "cover": {
                  "medium": "medium.jpg"
                },
                "kind": "ANIME",
                "mediaId": {
                  "anilist": 15125,
                  "notify": "notify-id"
                },
                "animethemes": [
                  {
                    "id": 1,
                    "sequence": 1,
                    "slug": "opening-1",
                    "type": "OP",
                    "song": {
                      "id": 50,
                      "title": "OP 1"
                    },
                    "animethemeentries": [
                      {
                        "id": 100,
                        "version": 1,
                        "videos": [
                          {
                            "id": 1000,
                            "link": "video.mp4",
                            "audio": {
                              "id": 9000,
                              "link": "audio.mp3"
                            }
                          }
                        ]
                      }
                    ]
                  }
                ],
                "trailers": [],
                "updatedAt": 1710000000
              }
            }
            """.trimIndent()

        val result = json.decodeFromString<EdgeMediaModel>(payload)

        val series = assertNotNull(result.series)
        assertEquals(1, series.animethemes.size)
        assertEquals(1L, series.animethemes.first().id)
        assertEquals("OP 1", series.animethemes.first().name)
    }

    @Test
    fun `converter derives edge entity id from media source ids`() {
        val payload =
            """
            {
              "series": {
                "title": {
                  "canonical": "Canonical Title"
                },
                "cover": {
                  "medium": "medium.jpg"
                },
                "kind": "ANIME",
                "mediaId": {
                  "anilist": 15125,
                  "slug": "sample-series"
                },
                "animethemes": [],
                "trailers": [],
                "updatedAt": 1710000000
              }
            }
            """.trimIndent()

        val result = json.decodeFromString<EdgeMediaModel>(payload)
        val series = assertNotNull(result.series)

        val entity = EdgeMediaModelConverter().convertFrom(series)

        assertEquals("sample-series", entity.id)
        assertEquals(15125L, entity.externalIds.aniList)
    }

    @Test
    fun `series payload keeps multiple themes even when ids are missing`() {
        val payload =
            """
            {
              "series": {
                "title": {
                  "canonical": "Canonical Title"
                },
                "cover": {
                  "medium": "medium.jpg"
                },
                "kind": "ANIME",
                "mediaId": {
                  "anilist": 15125,
                  "notify": "notify-id"
                },
                "animethemes": [
                  {
                    "sequence": 1,
                    "type": "OP",
                    "song": {
                      "title": "OP 1"
                    }
                  },
                  {
                    "sequence": 1,
                    "type": "ED",
                    "song": {
                      "title": "ED 1"
                    }
                  }
                ],
                "trailers": [],
                "updatedAt": 1710000000
              }
            }
            """.trimIndent()

        val result = json.decodeFromString<EdgeMediaModel>(payload)
        val series = assertNotNull(result.series)

        assertEquals(2, series.animethemes.size)
        assertEquals(listOf("OP 1", "ED 1"), series.animethemes.map { it.name })
    }
}
