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
package co.anitrend.settings.component.content.anilist.model

data class AniListSettingsUiState(
    val groups: List<Group>,
) {
    data class Group(
        val group: AniListSettingsGroup,
        val entries: List<Entry>,
    )

    data class Entry(
        val entry: AniListSettingsEntry,
        val value: String,
    )
}

enum class AniListSettingsGroup {
    Profile,
    General,
    Notifications,
    AnimeList,
    MangaList,
}

enum class AniListSettingsEntry {
    ProfileAbout,
    ProfileDonatorBadge,
    ProfileColor,
    TitleLanguage,
    StaffNameLanguage,
    DisplayAdultContent,
    TimeZone,
    AiringNotifications,
    NotificationOptions,
    AnimeScoreFormat,
    AnimeRowOrder,
    AnimeSectionOrder,
    AnimeSplitCompletedSection,
    AnimeCustomLists,
    AnimeAdvancedScoring,
    AnimeAdvancedScoringEnabled,
    MangaScoreFormat,
    MangaRowOrder,
    MangaSectionOrder,
    MangaSplitCompletedSection,
    MangaCustomLists,
    MangaAdvancedScoring,
    MangaAdvancedScoringEnabled,
}
