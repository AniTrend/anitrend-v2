package co.anitrend.medialist.editor.component.compose.state

import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MediaListEditorStateTest {

    private val dateHelper = mockk<AniTrendDateHelper>()

    /**
     * Base media used across tests. Individual tests override only the fields they care about
     * with media.copy(...), keeping setup compact and consistent.
     */
    private val baseMedia: Media.Extended =
        Media.Extended.empty().copy(
            id = 100L,
            title = MediaTitle("T", null, null, "T"),
            category = Media.Category.Anime(episodes = 12, duration = 0, broadcast = null, premiered = null, schedule = null),
            status = MediaStatus.RELEASING,
            mediaList =
                MediaList.Core(
                    id = 10L,
                    mediaId = 100L,
                    userId = 1L,
                    status = MediaListStatus.CURRENT,
                    score = 0f,
                    progress = MediaListProgress.Anime(3, 0),
                    startedOn = FuzzyDate.empty(),
                    finishedOn = FuzzyDate.empty(),
                    privacy = MediaListPrivacy(isPrivate = false, notes = null, isHidden = false),
                    customLists = emptyList(),
                    advancedScores = emptyList(),
                    priority = 0,
                    createdOn = 0L,
                ),
        )

    @Test
    fun `COMPLETED sets progress to total and end date`() {
        every { dateHelper.convertToFuzzyDate(unixTimeStamp = any()) } returns FuzzyDate.empty()
        val media = baseMedia.copy(
            category = (baseMedia.category as Media.Category.Anime).copy(episodes = 24),
            mediaList = (baseMedia.mediaList as MediaList.Core).copy(
                progress = MediaListProgress.Anime(1, 0),
            ),
        )
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)

        state.onStatusSelected(MediaListStatus.COMPLETED)

        assertEquals("24", state.progressText)
        assertNotNull(state.selectedEndDate)
    }

    @Test
    fun `CURRENT sets start date`() {
        every { dateHelper.convertToFuzzyDate(unixTimeStamp = any()) } returns FuzzyDate.empty()
        val media = baseMedia
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)

        state.onStatusSelected(MediaListStatus.CURRENT)

        assertNotNull(state.selectedStartDate)
    }

    @Test
    fun `REPEATING resets progress to 0`() {
        val media = baseMedia.copy(
            mediaList = (baseMedia.mediaList as MediaList.Core).copy(
                progress = MediaListProgress.Anime(7, 0),
            ),
        )
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)

        state.onStatusSelected(MediaListStatus.REPEATING)

        assertEquals("0", state.progressText)
    }

    @Test
    fun `save params use edited progress and preserve hidden flag (anime)`() {
        val media = baseMedia.copy(
            mediaList = (baseMedia.mediaList as MediaList.Core).copy(
                id = 42L,
                progress = MediaListProgress.Anime(5, 0),
                privacy = (baseMedia.mediaList as MediaList.Core).privacy.copy(isHidden = true),
            ),
        )
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)
        state.progressText = "12"
        state.isPrivate = true

        val params = state.createSaveEntryParams()
        assertEquals(42L, params.id)
        assertEquals(12, params.progress)
        assertEquals(true, state.isPrivate)
        assertEquals(true, params.hiddenFromStatusLists)
    }

    @Test
    fun `save params map manga volumes and repeating`() {
        val media = baseMedia.copy(
            category = Media.Category.Manga(chapters = 100, volumes = 0),
            mediaList = (baseMedia.mediaList as MediaList.Core).copy(
                progress = MediaListProgress.Manga(10, 5, 0),
            ),
        )
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)

        // default mapping keeps initial volume progress
        var params = state.createSaveEntryParams()
        assertEquals(5, params.progressVolumes)

        // when repeating, volumes should reset to 0
        state.onStatusSelected(MediaListStatus.REPEATING)
        params = state.createSaveEntryParams()
        assertEquals(0, params.progressVolumes)
    }

    @Test
    fun `scoreRaw normalizes to 100 for POINT_10`() {
        val media = baseMedia
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)
        state.scoreText = "7"
        val params = state.createSaveEntryParams()
        assertEquals(70, params.scoreRaw)
        assertEquals(7f, params.score)
    }

    @Test
    fun `scoreRaw normalizes to 100 for POINT_10_DECIMAL`() {
        val media = baseMedia
        val state = MediaListEditorState(media, ScoreFormat.POINT_10_DECIMAL, dateHelper)
        state.scoreText = "7.4"
        val params = state.createSaveEntryParams()
        assertEquals(74, params.scoreRaw)
        assertEquals(7.4f, params.score)
    }

    @Test
    fun `scoreRaw normalizes to 100 for POINT_100`() {
        val media = baseMedia
        val state = MediaListEditorState(media, ScoreFormat.POINT_100, dateHelper)
        state.scoreText = "83"
        val params = state.createSaveEntryParams()
        assertEquals(83, params.scoreRaw)
        assertEquals(83f, params.score)
    }

    @Test
    fun `scoreRaw normalizes to 100 for POINT_5`() {
        val media = baseMedia
        val state = MediaListEditorState(media, ScoreFormat.POINT_5, dateHelper)
        state.scoreText = "4"
        val params = state.createSaveEntryParams()
        assertEquals(80, params.scoreRaw)
        assertEquals(4f, params.score)
    }

    @Test
    fun `scoreRaw normalizes to 100 for POINT_3`() {
        val media = baseMedia
        val state = MediaListEditorState(media, ScoreFormat.POINT_3, dateHelper)
        state.scoreText = "2"
        val params = state.createSaveEntryParams()
        assertEquals(67, params.scoreRaw)
        assertEquals(2f, params.score)
    }

    @Test
    fun `maps edited repeat and volumes`() {
        val media = baseMedia.copy(
            category = Media.Category.Manga(chapters = 100, volumes = 0),
            mediaList = (baseMedia.mediaList as MediaList.Core).copy(
                progress = MediaListProgress.Manga(10, 5, 0),
            ),
        )
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)
        state.volumeProgressText = "7"
        state.repeatText = "3"
        val params = state.createSaveEntryParams()
        assertEquals(7, params.progressVolumes)
        assertEquals(3, params.repeat)
    }

    @Test
    fun `advanced scores map in stable order`() {
        val media = baseMedia.copy(
            mediaList = (baseMedia.mediaList as MediaList.Core).copy(
                // Provide initial names to define stable order
                advancedScores = listOf(
                    MediaList.AdvancedScore(name = "Story", score = 0f),
                    MediaList.AdvancedScore(name = "Art", score = 0f),
                ),
            ),
        )
        val state = MediaListEditorState(media, ScoreFormat.POINT_10, dateHelper)
        state.setAdvancedScore("Story" , "7.0")
        state.setAdvancedScore( "Art", "8.0")
        val params = state.createSaveEntryParams()
        assertEquals(listOf(7.0f, 8.0f), params.advancedScores)
    }
}
