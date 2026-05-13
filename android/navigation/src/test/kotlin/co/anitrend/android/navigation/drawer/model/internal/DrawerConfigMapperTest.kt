package co.anitrend.android.navigation.drawer.model.internal

import co.anitrend.android.navigation.drawer.R
import co.anitrend.domain.config.entity.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DrawerConfigMapperTest {
    private val mapper = DrawerConfigMapper()

    @Test
    fun `mapper preserves backend order and inserts headers`() {
        val entries =
            mapper.map(
                navigation = defaultNavigation(),
                authenticated = true,
                selectedDestination = DrawerDestination.Home,
            )

        assertEquals(5, entries.size)
        val generalHeader = assertIs<DrawerEntry.Header>(entries[0])
        val homeItem = assertIs<DrawerEntry.Item>(entries[1])
        val discoverItem = assertIs<DrawerEntry.Item>(entries[2])
        val manageHeader = assertIs<DrawerEntry.Header>(entries[3])
        val animeListItem = assertIs<DrawerEntry.Item>(entries[4])

        assertEquals(R.string.navigation_header_general, generalHeader.titleRes)
        assertEquals(DrawerDestination.Home, homeItem.destination)
        assertEquals(DrawerDestination.Discover, discoverItem.destination)
        assertEquals(R.string.navigation_header_manage, manageHeader.titleRes)
        assertEquals(DrawerDestination.AnimeList, animeListItem.destination)
    }

    @Test
    fun `mapper filters authenticated entries for anonymous users`() {
        val entries =
            mapper.map(
                navigation = defaultNavigation(),
                authenticated = false,
                selectedDestination = DrawerDestination.Home,
            )

        assertEquals(3, entries.size)
        assertTrue(entries.none { entry -> entry is DrawerEntry.Item && entry.destination == DrawerDestination.AnimeList })
    }

    @Test
    fun `mapper marks external destinations as non checkable`() {
        val entries =
            mapper.map(
                navigation =
                    listOf(
                        configNavigation(
                            destination = "https://www.patreon.com/wax911",
                            groupKey = "navigation_header_support",
                            iconKey = "ic_patreon_24dp",
                            titleKey = "navigation_support",
                        ),
                    ),
                authenticated = false,
                selectedDestination = DrawerDestination.Home,
            )

        val item = entries.filterIsInstance<DrawerEntry.Item>().single()

        assertIs<DrawerDestination.ExternalUrl>(item.destination)
        assertFalse(item.isCheckable)
    }

    private fun defaultNavigation() =
        listOf(
            configNavigation(
                destination = "/home",
                groupKey = "navigation_header_general",
                iconKey = "ic_deck_24dp",
                titleKey = "navigation_home",
            ),
            configNavigation(
                destination = "/discover",
                groupKey = "navigation_header_general",
                iconKey = "ic_discover_24dp",
                titleKey = "navigation_discover",
            ),
            configNavigation(
                destination = "/animelist",
                groupKey = "navigation_header_manage",
                iconKey = "ic_anime_24",
                titleKey = "navigation_anime_list",
                authenticated = true,
            ),
        )

    private fun configNavigation(
        destination: String,
        groupKey: String,
        iconKey: String,
        titleKey: String,
        authenticated: Boolean = false,
        id: Long = destination.hashCode().toLong(),
    ) = Config.Navigation(
        criteria = ">=2.0.0",
        destination = destination,
        group =
            Config.Navigation.Group(
                authenticated = authenticated,
                i18n = groupKey,
            ),
        i18n = titleKey,
        icon = iconKey,
        id = id,
    )
}
