/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.feed.episode.converter

import co.anitrend.data.feed.episode.converter.EpisodeModelConverter.Companion.durationFormatted
import co.anitrend.data.feed.episode.converter.EpisodeModelConverter.Companion.toCoverImage
import co.anitrend.data.feed.episode.entity.EpisodeEntity.CoverImage
import co.anitrend.data.feed.episode.model.EpisodeModelItem
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EpisodeModelConverterTest {

    private val item = mockk<EpisodeModelItem>()

    @Before
    fun setup() {
        every { item.duration } returns 1405
        every { item.thumbnails } returns listOf(
            EpisodeModelItem.ThumbnailModel(
                url = "https://img.thumb.1080",
                width = 1080
            ),
            EpisodeModelItem.ThumbnailModel(
                url = "https://img.thumb.720",
                width = 720
            ),
            EpisodeModelItem.ThumbnailModel(
                url = "https://img.thumb.480",
                width = 480
            )
        )
        every { item.description } returns """<img src="https://img1.ak.crunchyroll.com/i/spire1-tmb/745d6f6f240ffc99cc08f4f4d52f6e0b1374356790_thumb.jpg" /><br />Koji shows an interest in Boruto’s transformation and withdraws, and Boruto and his friends decide to head back to the village. On their way back, they discover an unconscious boy who has the same strange mark on his palm as Boruto."""
        every { item.subtitleLanguages } returns "en - us,es - la,es - es,fr - fr,pt - br,ar - me,it - it"
        every { item.keywords } returns "shounen, magic, fantasy, adventure, action, shonen, comedy, 2020"
        every { item.seriesTitle } returns "BORUTO: NARUTO NEXT GENERATIONS - Episode 188 - Awakening"
    }

    @Test
    fun `to cover image converter`() {
        val expected = CoverImage(
            large = "https://img.thumb.1080",
            medium = "https://img.thumb.720"
        )

        val actual = item.thumbnails.toCoverImage()

        assertEquals(expected, actual)
    }

    @Test
    fun `formatted episode duration converter`() {
        val expected = "23:25"
        val actual = item.durationFormatted()

        assertEquals(expected, actual)
    }

    @Test
    fun `extract key words from comma separated string`() {
        val expected = listOf("shounen", "magic", "fantasy", "adventure", "action", "shonen", "comedy")
        val actual = EpisodeModelConverter.extractKeyWords(item.keywords)

        assertEquals(expected, actual)
    }

    @Test
    fun `extract subtitles from comma separated string`() {
        val expected = listOf("enUS","esLA","esES","frFR","ptBR","arME","itIT")
        val actual = EpisodeModelConverter.extractSubTitles(item.subtitleLanguages)

        assertEquals(expected, actual)
    }

    @Test
    fun `extract description excluding embedded images`() {
        val expected = "Koji shows an interest in Boruto’s transformation and withdraws, and Boruto and his friends decide to head back to the village. On their way back, they discover an unconscious boy who has the same strange mark on his palm as Boruto."
        val actual = EpisodeModelConverter.stripOutImages(item.description)

        assertEquals(expected, actual)
    }

    @Test
    fun `extract series name without episode information`() {
        val expected = "BORUTO: NARUTO NEXT GENERATIONS"
        val actual = EpisodeModelConverter.extractSeriesTitle(item.seriesTitle)

        assertEquals(expected, actual)
    }
}