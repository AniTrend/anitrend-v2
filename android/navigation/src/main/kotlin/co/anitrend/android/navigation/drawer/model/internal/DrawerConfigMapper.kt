/*
 * Copyright (C) 2026 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
