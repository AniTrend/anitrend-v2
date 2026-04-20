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

class AniListSettingsEditorMapperTest {
    @Test
    fun `given authenticated user when mapping to editor then values are hydrated for editing`() {
        val source = fixtureUser()

        val editorState = AniListSettingsEditorMapper.from(source)

        assertEquals("About editor", editorState.about)
        assertEquals("blue", editorState.profileColor)
        assertEquals(UserTitleLanguage.ENGLISH_STYLISED, editorState.titleLanguage)
        assertEquals(UserStaffNameLanguage.NATIVE, editorState.staffNameLanguage)
        assertEquals("Planning, Watching", editorState.animeListOptions.sectionOrder)
        assertEquals("Art, Story", editorState.animeListOptions.advancedScoring)
    }

    @Test
    fun `given blank editor fields when mapping to param then optional values become null`() {
        val editorState =
            AniListSettingsEditorMapper
                .from(fixtureUser())
                .copy(
                    about = "",
                    profileColor = "",
                    timeZone = "",
                    rowOrder = "",
                )

        val param = AniListSettingsEditorMapper.toParam(editorState)

        assertEquals(null, param.about)
        assertEquals(null, param.profileColor)
        assertEquals(null, param.timeZone)
        assertEquals(null, param.rowOrder)
    }

    private fun fixtureUser() =
        User.Authenticated(
            unreadNotifications = 0,
            listOption =
                UserMediaListOption(
                    scoreFormat = ScoreFormat.POINT_10,
                    rowOrder = "SCORE",
                    animeList =
                        UserMediaListTypeOptions(
                            sectionOrder = listOf("Planning", "Watching"),
                            splitCompletedSectionByFormat = true,
                            customLists = listOf("Seasonal"),
                            advancedScoring = listOf("Art", "Story"),
                            advancedScoringEnabled = true,
                        ),
                    mangaList =
                        UserMediaListTypeOptions(
                            sectionOrder = listOf("Reading"),
                            splitCompletedSectionByFormat = false,
                            customLists = listOf("Backlog"),
                            advancedScoring = listOf("Art"),
                            advancedScoringEnabled = false,
                        ),
                ),
            profileOption =
                UserProfileOption(
                    titleLanguage = UserTitleLanguage.ENGLISH_STYLISED,
                    displayAdultContent = false,
                    airingNotifications = true,
                    notificationOptions =
                        listOf(
                            UserNotificationOption(
                                isEnabled = true,
                                type = NotificationType.AIRING,
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
                    about = "About editor",
                    donationBadge = null,
                    donationTier = null,
                    isFollowing = null,
                    isFollower = null,
                    isBlocked = null,
                    pageUrl = null,
                    createdAt = null,
                    updatedAt = null,
                ),
            id = 7L,
        )
}
