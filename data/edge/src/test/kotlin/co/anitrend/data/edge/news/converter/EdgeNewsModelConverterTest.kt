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

import co.anitrend.data.edge.news.model.remote.EdgeNewsConnectionModel
import kotlin.test.Test
import kotlin.test.assertEquals

class EdgeNewsModelConverterTest {
    @Test
    fun `converter derives source and published timestamp from schema fields`() {
        val news =
            EdgeNewsConnectionModel.News(
                area = "Anime",
                category = "Editorial",
                genre = "Feature",
                id = "news-1",
                language = "en",
                title = "Title",
                link = "https://example.com",
                image = "https://example.com/image.jpg",
                publishedOn = 1710000123.0,
                description = "",
                content = "Fallback content",
            )

        val result = EdgeNewsModelConverter().convertFrom(listOf(news)).first()

        assertEquals("Editorial", result.source)
        assertEquals(1710000123L, result.publishedAt)
        assertEquals("Fallback content", result.description)
    }
}
