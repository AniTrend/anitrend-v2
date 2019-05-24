package co.anitrend.data.model.response.meta

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FuzzyDateTest {

    @Test
    fun toFuzzyDateIntUnknownIsCorrect() {
        /** new instance of the fuzzy date object should return all fields defaulted to [FuzzyDate.UNKNOWN] */
        val fuzzyDate = FuzzyDate()

        assertEquals("0", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun toFuzzyDateIntMissingDayIsCorrect() {
        val fuzzyDate = FuzzyDate(
            year = 1976,
            month = 5
        )

        assertEquals("19760500", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun toFuzzyDateIntMissingMonthIsCorrect() {
        val fuzzyDate = FuzzyDate(
            year = 1843,
            day = 10
        )

        assertEquals("18430010", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun toFuzzyDateIntMissingYearIsCorrect() {
        val fuzzyDate = FuzzyDate(
            month = 8,
            day = 10
        )

        assertEquals("00000810", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun toFuzzyDateIntAllPresentIsCorrect() {
        val fuzzyDate = FuzzyDate(2011,11,17)

        assertEquals("20111117", fuzzyDate.toFuzzyDateInt())
    }
}