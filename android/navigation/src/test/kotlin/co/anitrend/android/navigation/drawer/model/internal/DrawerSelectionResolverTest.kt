package co.anitrend.android.navigation.drawer.model.internal

import co.anitrend.android.navigation.drawer.R
import kotlin.test.Test
import kotlin.test.assertEquals

class DrawerSelectionResolverTest {
    @Test
    fun `resolver falls back to home when selected destination is unavailable`() {
        val resolved =
            DrawerSelectionResolver.resolve(
                currentSelection = DrawerDestination.AnimeList,
                entries =
                    listOf(
                        DrawerEntry.Header(
                            groupId = R.id.navigation_group_general,
                            titleRes = R.string.navigation_header_general,
                        ),
                        DrawerEntry.Item(
                            destination = DrawerDestination.Home,
                            iconRes = R.drawable.ic_deck_24dp,
                            titleRes = R.string.navigation_home,
                            isCheckable = true,
                            isChecked = false,
                        ),
                    ),
            )

        assertEquals(DrawerDestination.Home, resolved)
    }
}
