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

import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.entity.attribute.option.UserNotificationOption

object AniListSettingsMapper {
    fun from(source: User.Authenticated): AniListSettingsUiState =
        AniListSettingsUiState(
            groups =
                listOf(
                    AniListSettingsUiState.Group(
                        group = AniListSettingsGroup.Profile,
                        entries =
                            listOf(
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.ProfileAbout,
                                    value =
                                        source.status.about
                                            ?.toString()
                                            .orNone(),
                                ),
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.ProfileDonatorBadge,
                                    value =
                                        source.status.donationBadge
                                            ?.toString()
                                            .orNone(),
                                ),
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.ProfileColor,
                                    value = source.profileOption.profileColor.orNone(),
                                ),
                            ),
                    ),
                    AniListSettingsUiState.Group(
                        group = AniListSettingsGroup.General,
                        entries =
                            listOf(
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.TitleLanguage,
                                    value =
                                        source.profileOption.titleLanguage.alias
                                            .toString(),
                                ),
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.StaffNameLanguage,
                                    value =
                                        source.profileOption.staffNameLanguage
                                            ?.alias
                                            ?.toString()
                                            .orNone(),
                                ),
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.DisplayAdultContent,
                                    value = source.profileOption.displayAdultContent.asEnabledLabel(),
                                ),
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.TimeZone,
                                    value = source.profileOption.timeZone.orNone(),
                                ),
                            ),
                    ),
                    AniListSettingsUiState.Group(
                        group = AniListSettingsGroup.Notifications,
                        entries =
                            listOf(
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.AiringNotifications,
                                    value = source.profileOption.airingNotifications.asEnabledLabel(),
                                ),
                                AniListSettingsUiState.Entry(
                                    entry = AniListSettingsEntry.NotificationOptions,
                                    value = source.profileOption.notificationOptions.notificationSummary(),
                                ),
                            ),
                    ),
                    AniListSettingsUiState.Group(
                        group = AniListSettingsGroup.AnimeList,
                        entries =
                            source.listOption.animeList.entries(
                                scoreFormat =
                                    source.listOption.scoreFormat.alias
                                        .toString(),
                                rowOrder =
                                    source.listOption.rowOrder
                                        ?.toString()
                                        .orNone(),
                                scoreEntry = AniListSettingsEntry.AnimeScoreFormat,
                                rowOrderEntry = AniListSettingsEntry.AnimeRowOrder,
                                sectionOrderEntry = AniListSettingsEntry.AnimeSectionOrder,
                                splitCompletedEntry = AniListSettingsEntry.AnimeSplitCompletedSection,
                                customListsEntry = AniListSettingsEntry.AnimeCustomLists,
                                advancedScoringEntry = AniListSettingsEntry.AnimeAdvancedScoring,
                                advancedScoringEnabledEntry = AniListSettingsEntry.AnimeAdvancedScoringEnabled,
                            ),
                    ),
                    AniListSettingsUiState.Group(
                        group = AniListSettingsGroup.MangaList,
                        entries =
                            source.listOption.mangaList.entries(
                                scoreFormat =
                                    source.listOption.scoreFormat.alias
                                        .toString(),
                                rowOrder =
                                    source.listOption.rowOrder
                                        ?.toString()
                                        .orNone(),
                                scoreEntry = AniListSettingsEntry.MangaScoreFormat,
                                rowOrderEntry = AniListSettingsEntry.MangaRowOrder,
                                sectionOrderEntry = AniListSettingsEntry.MangaSectionOrder,
                                splitCompletedEntry = AniListSettingsEntry.MangaSplitCompletedSection,
                                customListsEntry = AniListSettingsEntry.MangaCustomLists,
                                advancedScoringEntry = AniListSettingsEntry.MangaAdvancedScoring,
                                advancedScoringEnabledEntry = AniListSettingsEntry.MangaAdvancedScoringEnabled,
                            ),
                    ),
                ),
        )

    private fun UserMediaListTypeOptions.entries(
        scoreFormat: String,
        rowOrder: String,
        scoreEntry: AniListSettingsEntry,
        rowOrderEntry: AniListSettingsEntry,
        sectionOrderEntry: AniListSettingsEntry,
        splitCompletedEntry: AniListSettingsEntry,
        customListsEntry: AniListSettingsEntry,
        advancedScoringEntry: AniListSettingsEntry,
        advancedScoringEnabledEntry: AniListSettingsEntry,
    ) = listOf(
        AniListSettingsUiState.Entry(
            entry = scoreEntry,
            value = scoreFormat,
        ),
        AniListSettingsUiState.Entry(
            entry = rowOrderEntry,
            value = rowOrder,
        ),
        AniListSettingsUiState.Entry(
            entry = sectionOrderEntry,
            value = sectionOrder.valuesSummary(),
        ),
        AniListSettingsUiState.Entry(
            entry = splitCompletedEntry,
            value = splitCompletedSectionByFormat.asEnabledLabel(),
        ),
        AniListSettingsUiState.Entry(
            entry = customListsEntry,
            value = customLists.valuesSummary(),
        ),
        AniListSettingsUiState.Entry(
            entry = advancedScoringEntry,
            value = advancedScoring.valuesSummary(),
        ),
        AniListSettingsUiState.Entry(
            entry = advancedScoringEnabledEntry,
            value = advancedScoringEnabled.asEnabledLabel(),
        ),
    )

    private fun Collection<UserNotificationOption>.notificationSummary(): String =
        mapNotNull { option ->
            option.type
                .takeIf { option.isEnabled }
                ?.alias
                ?.toString()
        }.valuesSummary()

    private fun Collection<CharSequence>.valuesSummary(): String =
        map(CharSequence::toString)
            .filter(String::isNotBlank)
            .takeIf(Collection<String>::isNotEmpty)
            ?.joinToString()
            ?: "None"

    private fun String?.orNone(): String =
        if (isNullOrBlank()) {
            "None"
        } else {
            this
        }

    private fun Boolean.asEnabledLabel(): String =
        if (this) {
            "Enabled"
        } else {
            "Disabled"
        }
}
