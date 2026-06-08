package co.anitrend.data.media.entity.filter

import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.model.MediaParam
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertTrue

class MediaQueryFilterTest {

    private val authenticationSettings = mockk<IAuthenticationSettings>(relaxed = true)

    @Test
    fun `search query uses LIKE predicates for media title fields`() {
        val filter = MediaQueryFilter.Paged(authentication = authenticationSettings)
        val param = MediaParam.Find(search = "ani")

        val sql = filter.build(param).sql.lowercase()

        assertTrue(sql.contains(" like "), "Expected LIKE predicate in SQL, got: $sql")
        assertTrue(!sql.contains(" match "), "MATCH should not be used for media search SQL, got: $sql")
    }

    @Test
    fun `media type filter targets media_type column`() {
        val filter = MediaQueryFilter.Paged(authentication = authenticationSettings)
        val param = MediaParam.Find(search = "ani", type = MediaType.ANIME)

        val sql = filter.build(param).sql.lowercase()

        assertTrue(sql.contains("media_type"), "Expected media_type filter in SQL, got: $sql")
        assertTrue(!sql.contains(" where type ="), "SQL should not use non-existent type column, got: $sql")
        assertTrue(sql.contains(" and "), "Expected AND predicate for type+search, got: $sql")
    }
}
