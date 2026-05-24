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

import co.anitrend.data.edge.theme.model.EdgeThemeModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EdgeThemeConverterTest {
    @Test
    fun `converter maps nested animetheme payload into theme entry and video entities`() {
        val model =
            EdgeThemeModel(
                id = 100L,
                sequence = 1,
                type = "OP",
                song = EdgeThemeModel.SongModel(id = 10L, title = "Opening One"),
                entries =
                    listOf(
                        EdgeThemeModel.EntryModel(
                            id = 200L,
                            episodes = "1-12",
                            notes = "TV",
                            version = 2,
                            videos =
                                listOf(
                                    EdgeThemeModel.VideoModel(
                                        id = 300L,
                                        link = "video.mp4",
                                        resolution = 1080,
                                        source = "WEB",
                                        audio = EdgeThemeModel.AudioModel(id = 400L, link = "audio.mp3"),
                                    ),
                                ),
                        ),
                    ),
            )

        val result = EdgeThemeConverter().convertFromOrNull("media-id" to model)

        assertNotNull(result)
        assertEquals("100", result.theme.themeId)
        assertEquals("Opening One", result.theme.songTitle)
        assertEquals(1, result.entries.size)
        assertEquals("200", result.entries.first().entryId)
        assertEquals(1, result.videos.size)
        assertEquals("300", result.videos.first().videoId)
        assertEquals("audio.mp3", result.videos.first().audioLink)
    }

    @Test
    fun `converter creates deterministic fallback ids when upstream ids are missing`() {
        val model =
            EdgeThemeModel(
                sequence = 1,
                type = "ED",
                song = EdgeThemeModel.SongModel(title = "Ending One"),
                entries =
                    listOf(
                        EdgeThemeModel.EntryModel(
                            version = 1,
                            videos =
                                listOf(
                                    EdgeThemeModel.VideoModel(
                                        link = "ending-video.mp4",
                                        audio = EdgeThemeModel.AudioModel(link = "ending-audio.mp3"),
                                    ),
                                ),
                        ),
                    ),
            )

        val result = EdgeThemeConverter().convertFromOrNull("media-id" to model)

        assertNotNull(result)
        assertEquals("endingone:ed:1:1", result.theme.themeId)
        assertEquals("endingone:ed:1:1:entry:0", result.entries.first().entryId)
        assertEquals("endingone:ed:1:1:entry:0:video:0", result.videos.first().videoId)
    }
}
