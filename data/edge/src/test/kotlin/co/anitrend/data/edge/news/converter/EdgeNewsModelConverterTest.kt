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
package co.anitrend.data.edge.news.converter

import co.anitrend.data.edge.graphql.NewsConnectionData
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EdgeNewsModelConverterTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

    @Test
    fun `converter derives source and published timestamp from schema fields`() {
        val payload =
            """
            {
              "news": {
                "count": 2,
                "data": [
                  {
                    "area": "Anime",
                    "category": "Editorial",
                    "genre": "Feature",
                    "id": "news-1",
                    "lang": "en",
                    "title": "Title",
                    "link": "https://example.com",
                    "image": "https://example.com/image.jpg",
                    "publishedOn": 1710000123,
                    "description": "",
                    "content": "Fallback content"
                  },
                  {
                    "area": "Manga",
                    "category": null,
                    "genre": null,
                    "id": "news-2",
                    "lang": "ja",
                    "title": "Second",
                    "link": "https://example.com/2",
                    "image": null,
                    "publishedOn": 1710000456,
                    "description": "Real description",
                    "content": ""
                  }
                ],
                "first": "cursor-1",
                "last": "cursor-2"
              }
            }
            """.trimIndent()

        val result = json.decodeFromString<NewsConnectionData>(payload)
        val news = result.news?.data.orEmpty().filterNotNull()

        val first = EdgeNewsModelConverter().convertFrom(news).first()

        assertEquals("Editorial", first.source)
        assertEquals(1710000123L, first.publishedAt)
        assertEquals("Fallback content", first.description)

        val second = EdgeNewsModelConverter().convertFrom(news)[1]
        assertEquals("Manga", second.source)
        assertEquals(1710000456L, second.publishedAt)
        assertEquals("Real description", second.description)
        assertEquals("Real description", second.content)
    }

    @Test
    fun `converter rejects fractional published timestamps instead of truncating`() {
        val payload =
            """
            {
              "news": {
                "count": 1,
                "data": [
                  {
                    "area": null,
                    "category": null,
                    "genre": null,
                    "id": "news-1",
                    "lang": null,
                    "title": "Title",
                    "link": "https://example.com",
                    "image": null,
                    "publishedOn": 1710000123.5,
                    "description": "Description",
                    "content": "Content"
                  }
                ],
                "first": null,
                "last": null
              }
            }
            """.trimIndent()

        val result = json.decodeFromString<NewsConnectionData>(payload)
        val news = result.news?.data.orEmpty().filterNotNull()

        assertFailsWith<IllegalArgumentException> {
            EdgeNewsModelConverter().convertFrom(news)
        }
    }

    @Test
    fun `converter handles absent paging root as empty page`() {
        val result = json.decodeFromString<NewsConnectionData>("""{"news": null}""")

        val news = result.news?.data.orEmpty().filterNotNull()
        assertEquals(0, news.size)
        assertEquals(emptyList(), EdgeNewsModelConverter().convertFrom(news))
    }
}
