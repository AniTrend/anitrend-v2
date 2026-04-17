package co.anitrend.android.navigation.drawer.model.internal

import co.anitrend.domain.config.entity.Config
import timber.log.Timber

internal class DrawerConfigMapper(
    private val titleResByKey: Map<String, Int> = DrawerResourceRegistry.titleResByKey,
    private val iconResByKey: Map<String, Int> = DrawerResourceRegistry.iconResByKey,
    private val groupIdByKey: Map<String, Int> = DrawerResourceRegistry.groupIdByKey,
) {
    fun map(
        navigation: List<Config.Navigation>,
        authenticated: Boolean,
        selectedDestination: DrawerDestination,
    ): List<DrawerEntry> {
        val filteredNavigation =
            navigation.filter { entry ->
                authenticated || !entry.group.authenticated
            }

        val entries = mutableListOf<DrawerEntry>()
        var currentGroupId: Int? = null

        filteredNavigation.forEach { entry ->
            val groupTitle = titleResByKey[entry.group.i18n]
            val groupId = groupIdByKey[entry.group.i18n]
            val titleRes = titleResByKey[entry.i18n]
            val iconRes = iconResByKey[entry.icon]
            val destination = entry.destination.toDrawerDestination()

            if (groupTitle == null || groupId == null || titleRes == null || iconRes == null || destination == null) {
                Timber.w("Ignoring invalid drawer config entry: %s", entry)
                return@forEach
            }

            if (currentGroupId != groupId) {
                currentGroupId = groupId
                entries +=
                    DrawerEntry.Header(
                        groupId = groupId,
                        titleRes = groupTitle,
                    )
            }

            val isCheckable = destination !is DrawerDestination.ExternalUrl
            entries +=
                DrawerEntry.Item(
                    destination = destination,
                    iconRes = iconRes,
                    titleRes = titleRes,
                    isCheckable = isCheckable,
                    isChecked = isCheckable && destination == selectedDestination,
                )
        }

        return entries
    }

    private fun String.toDrawerDestination(): DrawerDestination? =
        when (this) {
            "/home" -> DrawerDestination.Home
            "/discover" -> DrawerDestination.Discover
            "/social" -> DrawerDestination.Social
            "/reviews" -> DrawerDestination.Reviews
            "/suggestions" -> DrawerDestination.Suggestions
            "/animelist" -> DrawerDestination.AnimeList
            "/mangalist" -> DrawerDestination.MangaList
            "/news" -> DrawerDestination.News
            "/forum/recent" -> DrawerDestination.Forums
            "/episodes" -> DrawerDestination.Episodes
            else ->
                takeIf {
                    startsWith("https://") || startsWith("http://")
                }?.let(DrawerDestination::ExternalUrl)
        }
}
