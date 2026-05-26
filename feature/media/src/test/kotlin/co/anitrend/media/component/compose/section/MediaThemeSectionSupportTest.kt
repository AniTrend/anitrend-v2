package co.anitrend.media.component.compose.section

import co.anitrend.domain.media.entity.attribute.theme.MediaTheme
import co.anitrend.media.R
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertContentEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MediaThemeSectionSupportTest {

    @Test
    fun `availabilitySummaryResId maps asset combinations to expected copy`() {
        assertEquals(
            R.string.label_media_theme_section_audio_video_available,
            theme(audio = "https://cdn.example/audio.mp3", video = "https://cdn.example/video.webm").availabilitySummaryResId(),
        )
        assertEquals(
            R.string.label_media_theme_section_audio_available,
            theme(audio = "https://cdn.example/audio.mp3", video = "").availabilitySummaryResId(),
        )
        assertEquals(
            R.string.label_media_theme_section_video_available,
            theme(audio = null, video = "https://cdn.example/video.webm").availabilitySummaryResId(),
        )
        assertEquals(
            R.string.label_media_theme_section_details_only,
            theme(audio = null, video = "").availabilitySummaryResId(),
        )
    }

    @Test
    fun `metaBadgeLabel builds compact uppercase label from metadata`() {
        assertEquals(
            "OP 3 v2",
            theme(
                meta =
                    MediaTheme.Meta(
                        number = 3,
                        type = "op",
                        version = 2,
                    ),
            ).metaBadgeLabel(),
        )
    }

    @Test
    fun `metaBadgeLabel omits empty metadata values`() {
        assertEquals(
            "ED",
            theme(
                meta =
                    MediaTheme.Meta(
                        number = 0,
                        type = "ed",
                        version = 1,
                    ),
            ).metaBadgeLabel(),
        )
        assertNull(
            theme(
                meta =
                    MediaTheme.Meta(
                        number = 0,
                        type = "",
                        version = 1,
                    ),
            ).metaBadgeLabel(),
        )
    }

    @Test
    fun `meta helpers expose uppercase type and optional version label`() {
        val primary =
            theme(
                meta =
                    MediaTheme.Meta(
                        number = 1,
                        type = "op",
                        version = 3,
                    ),
            )

        val baseline =
            theme(
                meta =
                    MediaTheme.Meta(
                        number = 1,
                        type = "",
                        version = 1,
                    ),
            )

        assertEquals("OP", primary.metaTypeLabel())
        assertEquals("v3", primary.metaVersionLabel())
        assertNull(baseline.metaTypeLabel())
        assertNull(baseline.metaVersionLabel())
    }

    @Test
    fun `sortedForDisplay prioritises op then ed then unknown by sequence and version`() {
        val sorted =
            listOf(
                theme(name = "Ending 2", meta = MediaTheme.Meta(number = 2, type = "ed", version = 1)),
                theme(name = "No Meta", meta = null),
                theme(name = "Opening 1 v2", meta = MediaTheme.Meta(number = 1, type = "op", version = 2)),
                theme(name = "Opening 1 v1", meta = MediaTheme.Meta(number = 1, type = "op", version = 1)),
            ).sortedForDisplay()

        assertEquals(
            listOf("Opening 1 v1", "Opening 1 v2", "Ending 2", "No Meta"),
            sorted.map(MediaTheme::name),
        )
    }

    @Test
    fun `asset helpers detect availability`() {
        assertTrue(theme(audio = "https://cdn.example/audio.mp3", video = "").hasAudioAsset())
        assertFalse(theme(audio = "  ", video = "").hasAudioAsset())
        assertTrue(theme(audio = null, video = "https://cdn.example/video.webm").hasVideoAsset())
        assertFalse(theme(audio = null, video = "").hasVideoAsset())
    }

    @Test
    fun `variant helpers format labels and preview summary`() {
        val variant =
            MediaTheme.Variant(
                version = 2,
                episodes = "1-12",
                previews =
                    listOf(
                        MediaTheme.Preview(
                            video = "https://cdn.example/video.webm",
                            audio = "https://cdn.example/audio.mp3",
                            resolution = 1080,
                            source = "web",
                            tags = listOf("NC"),
                        ),
                    ),
            )

        assertEquals("v2", variant.variantLabel())
        assertEquals("1080P WEB NC", variant.previewSummaryText())
    }

    @Test
    fun `preview mediaTagTokens omits blank fields`() {
        val preview =
            MediaTheme.Preview(
                video = "https://cdn.example/video.webm",
                audio = null,
                resolution = null,
                source = "bd",
                tags = emptyList(),
            )

        assertContentEquals(listOf("BD"), preview.mediaTagTokens())
    }

    @Test
    fun `preferredPreview selects first playable variant preview and builds stable key`() {
        val theme =
            theme(
                themeId = "theme-1",
                variants =
                    listOf(
                        MediaTheme.Variant(
                            version = 1,
                            episodes = "1-15",
                            previews =
                                listOf(
                                    MediaTheme.Preview(
                                        video = "",
                                        audio = "https://cdn.example/audio.mp3",
                                        resolution = 1080,
                                        source = "web",
                                    ),
                                ),
                        ),
                    ),
            )

        val selection = theme.preferredPreviewSelection()

        assertTrue(selection != null)
        assertTrue(selection.previewKey.startsWith("theme-1:v1:"))
        assertTrue(selection.previewKey.endsWith("https://cdn.example/audio.mp3"))
        assertEquals("1-15", selection.variant.episodes)
    }

    private fun theme(
        themeId: String = "theme-1",
        name: String = "Sample Theme",
        audio: String? = null,
        video: String = "",
        meta: MediaTheme.Meta? = null,
        variants: List<MediaTheme.Variant> = emptyList(),
    ) =
        MediaTheme(
            mediaId = "media-1",
            themeId = themeId,
            name = name,
            audio = audio,
            video = video,
            meta = meta,
            variants = variants,
        )
}
