package co.anitrend.data.model.extension

import co.anitrend.data.model.response.meta.FuzzyDate
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FuzzyDateLikeConversionTest {

    @Test
    fun `should produce unknown fuzzy date like pattern`() {
        /** new instance of the fuzzy date object should return all fields defaulted to [FuzzyDate.UNKNOWN] */
        val fuzzyDate = FuzzyDate()

        Assert.assertEquals("0", fuzzyDate.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with missing day`() {
        val fuzzyDate = FuzzyDate(
            year = 1976,
            month = 5
        )

        Assert.assertEquals("197605%", fuzzyDate.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with missing month`() {
        val fuzzyDate = FuzzyDate(
            year = 1843,
            day = 10
        )

        Assert.assertEquals("1843%10", fuzzyDate.toFuzzyDateLike())

        val fuzzyDateYear = FuzzyDate(
            year = 1843
        )

        Assert.assertEquals("1843%", fuzzyDateYear.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with missing year`() {
        val fuzzyDate = FuzzyDate(
            month = 8,
            day = 10
        )

        Assert.assertEquals("%0810", fuzzyDate.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with no missing parts`() {
        val fuzzyDate = FuzzyDate(2011,11,17)

        Assert.assertEquals("20111117", fuzzyDate.toFuzzyDateLike())
    }
}