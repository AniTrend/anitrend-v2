package co.anitrend.data.model.extension

import co.anitrend.data.model.response.meta.FuzzyDate
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FuzzyDateIntConversionTest {

    @Test
    fun `should produce fuzzy date int unknown`() {
        /** new instance of the fuzzy date object should return all fields defaulted to [FuzzyDate.UNKNOWN] */
        val fuzzyDate = FuzzyDate()

        assertEquals("0", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce fuzzy date int with no day postfix`() {
        val fuzzyDate = FuzzyDate(
            year = 1976,
            month = 5
        )

        assertEquals("19760500", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce fuzzy date int with no month`() {
        val fuzzyDate = FuzzyDate(
            year = 1843,
            day = 10
        )

        assertEquals("18430010", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce fuzzy date int with no year`() {
        val fuzzyDate = FuzzyDate(
            month = 8,
            day = 10
        )

        assertEquals("00000810", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce full fuzzy date int`() {
        val fuzzyDate = FuzzyDate(2011,11,17)

        assertEquals("20111117", fuzzyDate.toFuzzyDateInt())
    }
}