package co.anitrend.medialist.editor.component.compose.util

import co.anitrend.domain.medialist.enums.ScoreFormat
import kotlin.test.Test
import kotlin.test.assertEquals

class ScoreInputFiltersTest {

    @Test
    fun `decimal format keeps one fractional digit and two integer digits`() {
        assertEquals("9.8", filterScoreInput("9.87", ScoreFormat.POINT_10_DECIMAL))
        assertEquals("12.3", filterScoreInput("12.34", ScoreFormat.POINT_10_DECIMAL))
        assertEquals("12.4", filterScoreInput("12.45", ScoreFormat.POINT_10_DECIMAL))
        assertEquals("12", filterScoreInput("12", ScoreFormat.POINT_10_DECIMAL))
    }

    @Test
    fun `decimal format collapses multiple dots and strips non-digits`() {
        assertEquals("1.2", filterScoreInput("..1.2a3", ScoreFormat.POINT_10_DECIMAL))
        assertEquals("", filterScoreInput("abc", ScoreFormat.POINT_10_DECIMAL))
    }

    @Test
    fun `non-decimal formats keep digits only`() {
        assertEquals("123", filterScoreInput("1a2b3", ScoreFormat.POINT_5))
        assertEquals("10", filterScoreInput("10.5", ScoreFormat.POINT_100))
    }
}
