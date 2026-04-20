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

import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.notification.enums.NotificationType
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.enums.UserStaffNameLanguage
import co.anitrend.domain.user.enums.UserTitleLanguage
import co.anitrend.domain.user.model.UserParam

data class AniListSettingsEditorState(
    val about: String,
    val profileColor: String,
    val titleLanguage: UserTitleLanguage,
    val staffNameLanguage: UserStaffNameLanguage,
    val displayAdultContent: Boolean,
    val timeZone: String,
    val airingNotifications: Boolean,
    val notificationOptions: List<UserParam.Update.NotificationOption>,
    val scoreFormat: ScoreFormat,
    val rowOrder: String,
    val animeListOptions: MediaListOptions,
    val mangaListOptions: MediaListOptions,
) {
    data class MediaListOptions(
        val sectionOrder: String,
        val splitCompletedSectionByFormat: Boolean,
        val customLists: String,
        val advancedScoring: String,
        val advancedScoringEnabled: Boolean,
    )
}

object AniListSettingsEditorMapper {
    fun from(source: User.Authenticated): AniListSettingsEditorState =
        AniListSettingsEditorState(
            about = source.status.about?.toString().orEmpty(),
            profileColor = source.profileOption.profileColor.orEmpty(),
            titleLanguage = source.profileOption.titleLanguage,
            staffNameLanguage = source.profileOption.staffNameLanguage ?: UserStaffNameLanguage.ROMAJI,
            displayAdultContent = source.profileOption.displayAdultContent,
            timeZone = source.profileOption.timeZone.orEmpty(),
            airingNotifications = source.profileOption.airingNotifications,
            notificationOptions =
                source.profileOption.notificationOptions.map { option ->
                    UserParam.Update.NotificationOption(
                        enabled = option.isEnabled,
                        type = option.type,
                    )
                },
            scoreFormat = source.listOption.scoreFormat,
            rowOrder = source.listOption.rowOrder?.toString().orEmpty(),
            animeListOptions = source.listOption.animeList.toEditorState(),
            mangaListOptions = source.listOption.mangaList.toEditorState(),
        )

    fun toParam(source: AniListSettingsEditorState): UserParam.Update =
        UserParam.Update(
            about = source.about.ifBlank { null },
            titleLanguage = source.titleLanguage,
            displayAdultContent = source.displayAdultContent,
            airingNotifications = source.airingNotifications,
            scoreFormat = source.scoreFormat,
            timeZone = source.timeZone.ifBlank { null },
            rowOrder = source.rowOrder.ifBlank { null },
            profileColor = source.profileColor.ifBlank { null },
            notificationOptions = source.notificationOptions,
            animeListOptions = source.animeListOptions.toParam(),
            mangaListOptions = source.mangaListOptions.toParam(),
            staffNameLanguage = source.staffNameLanguage,
        )

    private fun UserMediaListTypeOptions.toEditorState() =
        AniListSettingsEditorState.MediaListOptions(
            sectionOrder = sectionOrder.toJoinedSummary(),
            splitCompletedSectionByFormat = splitCompletedSectionByFormat,
            customLists = customLists.toJoinedSummary(),
            advancedScoring = advancedScoring.toJoinedSummary(),
            advancedScoringEnabled = advancedScoringEnabled,
        )

    private fun AniListSettingsEditorState.MediaListOptions.toParam() =
        UserParam.Update.MediaListOptions(
            sectionOrder = sectionOrder.toListOrNull(),
            splitCompletedSectionByFormat = splitCompletedSectionByFormat,
            customLists = customLists.toListOrNull(),
            advancedScoring = advancedScoring.toListOrNull(),
            advancedScoringEnabled = advancedScoringEnabled,
        )

    private fun Collection<CharSequence>.toJoinedSummary(): String =
        map(CharSequence::toString)
            .filter(String::isNotBlank)
            .joinToString()

    private fun String.toListOrNull(): List<String>? =
        split(',')
            .map(String::trim)
            .filter(String::isNotBlank)
            .takeIf(List<String>::isNotEmpty)

    fun NotificationType.label(): String = alias.toString()
}
