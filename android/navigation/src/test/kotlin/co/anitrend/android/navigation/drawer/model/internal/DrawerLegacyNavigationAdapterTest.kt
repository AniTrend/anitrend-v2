package co.anitrend.android.navigation.drawer.model.internal

import co.anitrend.android.navigation.drawer.R
import co.anitrend.android.navigation.drawer.model.navigation.Navigation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class DrawerLegacyNavigationAdapterTest {
    private val adapter = DrawerLegacyNavigationAdapter

    @Test
    fun `adapter maps typed destinations to legacy menu ids`() {
        val item =
            DrawerEntry.Item(
                destination = DrawerDestination.News,
                iconRes = R.drawable.ic_news_24,
                titleRes = R.string.navigation_news,
                isCheckable = true,
                isChecked = true,
            )

        val legacy = adapter.toLegacy(item)

        assertNotNull(legacy)
        assertEquals(R.id.navigation_news, legacy.id)
        assertEquals(R.string.navigation_news, legacy.titleRes)
        assertEquals(true, legacy.isChecked)
    }

    @Test
    fun `adapter maps external url destinations to legacy support ids`() {
        val item =
            DrawerEntry.Item(
                destination = DrawerDestination.ExternalUrl("https://discord.gg/2wzTqnF"),
                iconRes = R.drawable.ic_discord_24dp,
                titleRes = R.string.navigation_discord,
                isCheckable = false,
            )

        val legacy = adapter.toLegacy(item)

        assertNotNull(legacy)
        assertEquals(R.id.navigation_discord, legacy.id)
        assertFalse(legacy.isCheckable)
    }

    @Test
    fun `adapter maps drawer entries into legacy navigation list`() {
        val navigation =
            adapter.map(
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
                            isChecked = true,
                        ),
                    ),
            )

        assertEquals(2, navigation.size)
        assertIs<Navigation.Group>(navigation[0])
        assertIs<Navigation.Menu>(navigation[1])
        assertEquals(R.id.navigation_home, (navigation[1] as Navigation.Menu).id)
    }
}
