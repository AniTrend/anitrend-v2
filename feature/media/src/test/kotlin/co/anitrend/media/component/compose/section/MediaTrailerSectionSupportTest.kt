package co.anitrend.media.component.compose.section

import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MediaTrailerSectionSupportTest {
    @Test
    fun `resolveTrailerUrl maps supported trailer providers to playable urls`() {
        assertEquals(
            "https://youtube.com/watch?v=abc123",
            resolveTrailerUrl(
                MediaTrailer(
                    id = "abc123",
                    site = "youtube",
                    thumbnail = null,
                ),
            ),
        )
        assertEquals(
            "https://www.dailymotion.com/video/x9demo",
            resolveTrailerUrl(
                MediaTrailer(
                    id = "x9demo",
                    site = "dailymotion",
                    thumbnail = null,
                ),
            ),
        )
        assertEquals(
            "https://vimeo.com/91827364",
            resolveTrailerUrl(
                MediaTrailer(
                    id = "91827364",
                    site = "vimeo",
                    thumbnail = null,
                ),
            ),
        )
    }

    @Test
    fun `resolveTrailerUrl only falls back to raw urls when trailer id is already a url`() {
        assertEquals(
            "https://cdn.example.com/trailer.mp4",
            resolveTrailerUrl(
                MediaTrailer(
                    id = "https://cdn.example.com/trailer.mp4",
                    site = "unknown",
                    thumbnail = null,
                ),
            ),
        )
        assertNull(
            resolveTrailerUrl(
                MediaTrailer(
                    id = "opaque-id",
                    site = "unknown",
                    thumbnail = null,
                ),
            ),
        )
    }
}
