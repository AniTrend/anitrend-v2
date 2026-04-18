package co.anitrend.profile.component.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileSectionStateTest {

    @Test
    fun `dataOrNull is empty for loading empty and error states`() {
        assertNull(ProfileSectionState.Loading.state)
        assertNull(ProfileSectionState.Empty.state)
        assertNull(ProfileSectionState.Error(IllegalStateException("boom")).state)
    }

    @Test
    fun `dataOrNull exposes the stored value for partial and content states`() {
        assertEquals("AniTrend", ProfileSectionState.Partial("AniTrend").state)
        assertEquals("AniTrend", ProfileSectionState.Content("AniTrend").state)
    }
}
