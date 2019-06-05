package co.anitrend.data.model.extension

import co.anitrend.data.model.contract.FuzzyDateInt
import co.anitrend.data.model.response.meta.FuzzyDate
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FuzzyDateConversionTest {

    @Test
    fun `should produce unknown fuzzy date fields`() {
        /** should produce fuzzy date of [FuzzyDate.UNKNOWN] */
        val fuzzyDateInt : FuzzyDateInt = "0"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.day)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.month)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with an unknown day field`() {
        val fuzzyDateInt : FuzzyDateInt = "19760500"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.day)
        Assert.assertEquals(5, fuzzyDate.month)
        Assert.assertEquals(1976, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with an unknown month field`() {
        val fuzzyDateInt : FuzzyDateInt = "18430010"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(10, fuzzyDate.day)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.month)
        Assert.assertEquals(1843, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with an unknown year field`() {
        val fuzzyDateInt : FuzzyDateInt = "00000810"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(10, fuzzyDate.day)
        Assert.assertEquals(8, fuzzyDate.month)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with no unknown fields`() {
        val fuzzyDateInt : FuzzyDateInt = "20111117"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(17, fuzzyDate.day)
        Assert.assertEquals(11, fuzzyDate.month)
        Assert.assertEquals(2011, fuzzyDate.year)
    }
}