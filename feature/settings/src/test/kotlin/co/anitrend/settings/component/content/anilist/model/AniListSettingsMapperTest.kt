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
import co.anitrend.domain.user.entity.attribute.option.UserMediaListOption
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.entity.attribute.option.UserNotificationOption
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus
import co.anitrend.domain.user.enums.UserStaffNameLanguage
import co.anitrend.domain.user.enums.UserTitleLanguage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AniListSettingsMapperTest {
    @Test
    fun `given authenticated AniList user when mapping then groups and compact values are created`() {
        val source =
            User.Authenticated(
                unreadNotifications = 3,
                listOption =
                    UserMediaListOption(
                        scoreFormat = ScoreFormat.POINT_100,
                        rowOrder = "SCORE",
                        animeList =
                            UserMediaListTypeOptions(
                                sectionOrder = listOf("Watching", "Completed"),
                                splitCompletedSectionByFormat = true,
                                customLists = listOf("Seasonal"),
                                advancedScoring = listOf("Story", "Characters"),
                                advancedScoringEnabled = true,
                            ),
                        mangaList =
                            UserMediaListTypeOptions(
                                sectionOrder = listOf("Reading", "Completed"),
                                splitCompletedSectionByFormat = false,
                                customLists = listOf("Backlog"),
                                advancedScoring = listOf("Art"),
                                advancedScoringEnabled = false,
                            ),
                    ),
                profileOption =
                    UserProfileOption(
                        titleLanguage = UserTitleLanguage.ENGLISH,
                        displayAdultContent = true,
                        airingNotifications = true,
                        notificationOptions =
                            listOf(
                                UserNotificationOption(
                                    isEnabled = true,
                                    type = NotificationType.AIRING,
                                ),
                                UserNotificationOption(
                                    isEnabled = false,
                                    type = NotificationType.FOLLOWING,
                                ),
                            ),
                        profileColor = "blue",
                        timeZone = "+02:00",
                        staffNameLanguage = UserStaffNameLanguage.NATIVE,
                    ),
                name = "viewer",
                avatar = UserImage.empty(),
                status =
                    UserStatus(
                        about = "About me",
                        donationBadge = "Supporter",
                        donationTier = null,
                        isFollowing = false,
                        isFollower = false,
                        isBlocked = false,
                        pageUrl = "https://anilist.co/user/viewer",
                        createdAt = null,
                        updatedAt = null,
                    ),
                id = 1L,
            )

        val state = AniListSettingsMapper.from(source)

        assertEquals(
            listOf(
                AniListSettingsGroup.Profile,
                AniListSettingsGroup.General,
                AniListSettingsGroup.Notifications,
                AniListSettingsGroup.AnimeList,
                AniListSettingsGroup.MangaList,
            ),
            state.groups.map(AniListSettingsUiState.Group::group),
        )
        assertEquals("Airing", state.entryValue(AniListSettingsEntry.NotificationOptions))
        assertEquals("Watching, Completed", state.entryValue(AniListSettingsEntry.AnimeSectionOrder))
        assertEquals("Story, Characters", state.entryValue(AniListSettingsEntry.AnimeAdvancedScoring))
        assertEquals("Enabled", state.entryValue(AniListSettingsEntry.AnimeAdvancedScoringEnabled))
        assertTrue(state.entryValue(AniListSettingsEntry.ProfileAbout).contains("About me"))
    }

    private fun AniListSettingsUiState.entryValue(entry: AniListSettingsEntry): String =
        groups
            .flatMap { it.entries }
            .first { it.entry == entry }
            .value
}
