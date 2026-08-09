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
package co.anitrend.data.edge.theme.converter

import co.anitrend.data.edge.graphql.AnimeThemeType
import co.anitrend.data.edge.graphql.GetMediaByIdData
import co.anitrend.data.edge.theme.model.isPersistable
import co.anitrend.data.edge.theme.model.name
import co.anitrend.data.edge.theme.model.video
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EdgeThemeConverterTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

    @Test
    fun `generated animetheme payload decodes and maps into theme entry and video entities`() {
        val model = json.decodeFromString<GetMediaByIdData.SeriesAnimethemes>(generatedThemePayload)

        assertTrue(model.isPersistable)
        assertEquals(SONG_TITLE, model.name)
        assertEquals(VIDEO_LINK, model.video)

        val result = EdgeThemeConverter().convertFromOrNull("media-id" to model)

        assertNotNull(result)
        assertEquals("100", result.theme.themeId)
        assertEquals(SONG_TITLE, result.theme.songTitle)
        assertEquals(THEME_SLUG, result.theme.slug)
        assertEquals("OP", result.theme.type)
        assertEquals(1, result.theme.sequence)
        assertEquals(10L, result.theme.songId)
        assertEquals(1, result.entries.size)
        assertEquals("200", result.entries.first().entryId)
        assertEquals("1-12", result.entries.first().episodes)
        assertEquals("TV", result.entries.first().notes)
        assertEquals(2, result.entries.first().version)
        assertEquals(1, result.videos.size)
        assertEquals("300", result.videos.first().videoId)
        assertEquals(VIDEO_LINK, result.videos.first().link)
        assertEquals(1080, result.videos.first().resolution)
        assertEquals("WEB", result.videos.first().source)
        assertEquals(400L, result.videos.first().audioId)
        assertEquals("audio.mp3", result.videos.first().audioLink)
    }

    @Test
    fun `converter derives stable ids from upstream ids`() {
        val model =
            sampleTheme(
                id = 100.0,
                slug = "ending-1",
                type = AnimeThemeType.ED,
                song = GetMediaByIdData.SeriesAnimethemesSong(id = 10.0, title = "Ending One"),
                entries =
                    listOf(
                        sampleEntry(
                            id = 200.0,
                            version = 1.0,
                            videos =
                                listOf(
                                    sampleVideo(
                                        id = 300.0,
                                        link = "ending-video.mp4",
                                        audio =
                                            GetMediaByIdData.SeriesAnimethemesAnimethemeentriesVideosAudio(
                                                id = 400.0,
                                                link = "ending-audio.mp3",
                                            ),
                                    ),
                                ),
                        ),
                    ),
            )

        val result = EdgeThemeConverter().convertFromOrNull("media-id" to model)

        assertNotNull(result)
        assertEquals("100", result.theme.themeId)
        assertEquals("200", result.entries.first().entryId)
        assertEquals("300", result.videos.first().videoId)
        assertEquals("ending-audio.mp3", result.videos.first().audioLink)
    }

    @Test
    fun `converter drops themes without persistable content`() {
        val model =
            sampleTheme(
                id = 101.0,
                song = GetMediaByIdData.SeriesAnimethemesSong(id = 11.0, title = null),
                entries = emptyList(),
            )

        assertFalse(model.isPersistable)
        assertNull(model.name)
        assertNull(model.video)
        assertNull(EdgeThemeConverter().convertFromOrNull("media-id" to model))
    }

    @Test
    fun `converter rejects fractional theme ids instead of truncating`() {
        val model =
            sampleTheme(
                id = 100.5,
            )

        assertFailsWith<IllegalArgumentException> {
            EdgeThemeConverter().convertFromOrNull("media-id" to model)
        }
    }

    @Test
    fun `converter keeps multiple themes`() {
        val themes =
            listOf(
                sampleTheme(id = 1.0, slug = THEME_SLUG, song = GetMediaByIdData.SeriesAnimethemesSong(50.0, "OP 1")),
                sampleTheme(id = 2.0, slug = "ending-1", type = AnimeThemeType.ED, song = GetMediaByIdData.SeriesAnimethemesSong(51.0, "ED 1")),
            )

        assertEquals(listOf("OP 1", "ED 1"), themes.map { it.name })
        val converted =
            themes.mapNotNull { theme ->
                EdgeThemeConverter().convertFromOrNull("media-id" to theme)
            }
        assertEquals(listOf("1", "2"), converted.map { it.theme.themeId })
    }

    private fun sampleTheme(
        id: Double = 100.0,
        slug: String = THEME_SLUG,
        type: AnimeThemeType = AnimeThemeType.OP,
        song: GetMediaByIdData.SeriesAnimethemesSong? =
            GetMediaByIdData.SeriesAnimethemesSong(
                id = 10.0,
                title = SONG_TITLE,
            ),
        entries: List<GetMediaByIdData.SeriesAnimethemesAnimethemeentries> = listOf(sampleEntry()),
    ) =
        GetMediaByIdData.SeriesAnimethemes(
            animethemeentries = entries,
            id = id,
            sequence = 1.0,
            slug = slug,
            song = song,
            type = type,
        )

    private fun sampleEntry(
        id: Double = 200.0,
        episodes: String? = "1-12",
        notes: String? = "TV",
        version: Double? = 2.0,
        videos: List<GetMediaByIdData.SeriesAnimethemesAnimethemeentriesVideos> = listOf(sampleVideo()),
    ) =
        GetMediaByIdData.SeriesAnimethemesAnimethemeentries(
            episodes = episodes,
            id = id,
            notes = notes,
            nsfw = false,
            spoiler = false,
            version = version,
            videos = videos,
        )

    private fun sampleVideo(
        id: Double = 300.0,
        link: String = VIDEO_LINK,
        resolution: Double? = 1080.0,
        source: String? = "WEB",
        audio: GetMediaByIdData.SeriesAnimethemesAnimethemeentriesVideosAudio? =
            GetMediaByIdData.SeriesAnimethemesAnimethemeentriesVideosAudio(
                id = 400.0,
                link = "audio.mp3",
            ),
    ) =
        GetMediaByIdData.SeriesAnimethemesAnimethemeentriesVideos(
            audio = audio,
            id = id,
            link = link,
            lyrics = false,
            nc = false,
            overlap = null,
            resolution = resolution,
            source = source,
            subbed = false,
            tags = null,
            uncen = false,
        )
}

private const val SONG_TITLE = "Opening One"
private const val VIDEO_LINK = "video.mp4"
private const val THEME_SLUG = "opening-1"

private val generatedThemePayload =
    """
    {
      "animethemeentries": [
        {
          "episodes": "1-12",
          "id": 200,
          "notes": "TV",
          "nsfw": false,
          "spoiler": false,
          "version": 2,
          "videos": [
            {
              "audio": {
                "id": 400,
                "link": "audio.mp3"
              },
              "id": 300,
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
      "id": 100,
      "sequence": 1,
      "slug": "opening-1",
      "song": {
        "id": 10,
        "title": "Opening One"
      },
      "type": "OP"
    }
    """.trimIndent()
