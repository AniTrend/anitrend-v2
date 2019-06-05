package co.anitrend.data.model.query

import co.anitrend.data.model.extension.toFuzzyDateInt
import co.anitrend.data.model.query.media.AiringScheduleQuery
import co.anitrend.data.model.query.media.MediaQuery
import co.anitrend.data.model.response.meta.FuzzyDate
import co.anitrend.data.repository.media.attributes.MediaFormatContract
import co.anitrend.data.repository.media.attributes.MediaSeasonContract
import co.anitrend.data.repository.media.attributes.MediaStatusContract
import co.anitrend.data.repository.media.attributes.MediaTypeContract
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class QueryMappingTest {

    @Test
    fun `mapping of page query has correct keys and values`() {
        val pageQuery = PageQuery(
            page = 1,
            perPage = 15
        )

        val mappedQuery = pageQuery.toMap()

        assertNotNull("Conversion is not null", mappedQuery)

        assertTrue("Map contains key `page`", mappedQuery.containsKey("page"))
        assertTrue("Map contains key `perPage`", mappedQuery.containsKey("perPage"))

        assertEquals(1, mappedQuery["page"])
        assertEquals(15, mappedQuery["perPage"])
    }

    @Test
    fun `mapping of media query has correct keys and values`() {
        val mediaQuery = MediaQuery(
            type = MediaTypeContract.ANIME,
            endDate = FuzzyDate(
                year = 2018,
                month = 5
            ).toFuzzyDateInt(),
            season = MediaSeasonContract.FALL,
            format_in = MediaFormatContract.ALL,
            onList = true,
            status_in = listOf(
                MediaStatusContract.FINISHED,
                MediaStatusContract.NOT_YET_RELEASED
            )
        )

        val mappedQuery = mediaQuery.toMap()

        assertNotNull("Conversion is not null", mappedQuery)

        assertTrue("Map contains key `type`", mappedQuery.containsKey("type"))
        assertTrue("Map contains key `endDate`", mappedQuery.containsKey("endDate"))
        assertTrue("Map contains key `season`", mappedQuery.containsKey("season"))
        assertTrue("Map contains key `format_in`", mappedQuery.containsKey("format_in"))
        assertTrue("Map contains key `onList`", mappedQuery.containsKey("onList"))
        assertTrue("Map contains key `status_in`", mappedQuery.containsKey("status_in"))

        assertEquals("ANIME", mappedQuery["type"])
        assertEquals("20180500", mappedQuery["endDate"])
        assertEquals("FALL", mappedQuery["season"])
        assertEquals(listOf(
            "TV", "TV_SHORT", "MOVIE", "SPECIAL", "OVA", "ONA",
            "MUSIC", "MANGA", "NOVEL", "ONE_SHOT"
        ), mappedQuery["format_in"])
        assertEquals(true, mappedQuery["onList"])
        assertEquals(listOf("FINISHED", "NOT_YET_RELEASED"), mappedQuery["status_in"])
    }
}