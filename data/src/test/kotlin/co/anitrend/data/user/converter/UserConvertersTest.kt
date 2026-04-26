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
package co.anitrend.data.user.converter

import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.entity.view.UserEntityView
import co.anitrend.data.user.entity.view.UserStatisticEntityView
import co.anitrend.data.user.model.UserModel
import co.anitrend.data.user.model.option.UserOptionsModel
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.notification.enums.NotificationType
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.enums.UserStaffNameLanguage
import co.anitrend.domain.user.enums.UserTitleLanguage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserConvertersTest {
    @Test
    fun `given null notification types when mapping options then null types are skipped`() {
        val source =
            UserModel.Viewer(
                name = "viewer",
                siteUrl = "https://anilist.co/user/viewer",
                id = 1L,
                options =
                    UserOptionsModel.Viewer(
                        notificationOptions =
                            listOf(
                                UserOptionsModel.NotificationOptions(
                                    enabled = true,
                                    notificationType = null,
                                ),
                                UserOptionsModel.NotificationOptions(
                                    enabled = false,
                                    notificationType = NotificationType.AIRING,
                                ),
                            ),
                    ),
            )

        val result = UserGeneralOptionModelConverter().convertFrom(source)

        assertEquals(1, result.notificationOption.size)
        assertEquals(NotificationType.AIRING, result.notificationOption.first().notificationType)
        assertEquals(false, result.notificationOption.first().enabled)
    }

    @Test
    fun `given only null notification types when mapping options then result is empty`() {
        val source =
            UserModel.Viewer(
                name = "viewer",
                siteUrl = "https://anilist.co/user/viewer",
                id = 1L,
                options =
                    UserOptionsModel.Viewer(
                        notificationOptions =
                            listOf(
                                UserOptionsModel.NotificationOptions(
                                    enabled = true,
                                    notificationType = null,
                                ),
                            ),
                    ),
            )

        val result = UserGeneralOptionModelConverter().convertFrom(source)

        assertTrue(result.notificationOption.isEmpty())
    }

    @Test
    fun `given submission update notification type when mapping options then type is preserved`() {
        val source =
            UserModel.Viewer(
                name = "viewer",
                siteUrl = "https://anilist.co/user/viewer",
                id = 1L,
                options =
                    UserOptionsModel.Viewer(
                        notificationOptions =
                            listOf(
                                UserOptionsModel.NotificationOptions(
                                    enabled = true,
                                    notificationType = NotificationType.CHARACTER_SUBMISSION_UPDATE,
                                ),
                            ),
                    ),
            )

        val result = UserGeneralOptionModelConverter().convertFrom(source)

        assertEquals(1, result.notificationOption.size)
        assertEquals(NotificationType.CHARACTER_SUBMISSION_UPDATE, result.notificationOption.first().notificationType)
        assertTrue(result.notificationOption.first().enabled)
    }

    @Test
    fun `given missing statistic relation when mapping profile stats view then empty stats are used`() {
        val source = withStatisticView(statistic = null)

        val result = UserViewEntityConverter().convertFrom(source) as User.WithStats

        assertNull(result.statistics.anime)
        assertNull(result.statistics.manga)
        assertEquals("viewer", result.name)
    }

    @Test
    fun `given authenticated user view when mapping then AniList settings are preserved`() {
        val source =
            UserEntityView.Authenticated(
                user =
                    UserEntity(
                        about =
                            UserEntity.About(
                                name = "viewer",
                                bio = "About me",
                                siteUrl = "https://anilist.co/user/viewer",
                                donatorTier = null,
                                donatorBadge = "Supporter",
                            ),
                        status =
                            UserEntity.Status(
                                isFollowing = false,
                                isFollower = false,
                                isBlocked = false,
                            ),
                        coverImage = UserEntity.CoverImage(),
                        updatedAt = null,
                        createdAt = null,
                        id = 1L,
                    ),
                notification = UserGeneralOptionEntity.NotificationOption(enabled = true, notificationType = NotificationType.AIRING)
                    .let { _ ->
                        co.anitrend.data.user.entity.notification.UserNotificationEntity(
                            userId = 1L,
                            unreadNotifications = 7,
                        )
                    },
                generalOption =
                    UserGeneralOptionEntity(
                        userId = 1L,
                        airingNotifications = true,
                        displayAdultContent = true,
                        notificationOption =
                            listOf(
                                UserGeneralOptionEntity.NotificationOption(
                                    enabled = true,
                                    notificationType = NotificationType.AIRING,
                                ),
                            ),
                        titleLanguage = UserTitleLanguage.ENGLISH,
                        profileColor = "blue",
                        timeZone = "+02:00",
                        staffNameLanguage = UserStaffNameLanguage.NATIVE,
                    ),
                mediaListOption =
                    UserMediaOptionEntity(
                        userId = 1L,
                        scoreFormat = ScoreFormat.POINT_100,
                        rowOrder = "SCORE",
                        anime =
                            UserMediaOptionEntity.MediaOption(
                                customLists = listOf("Seasonal"),
                                sectionOrder = listOf("Watching", "Completed"),
                                advancedScoring = listOf("Story", "Characters"),
                                advancedScoringEnabled = true,
                                splitCompletedSectionByFormat = true,
                            ),
                        manga =
                            UserMediaOptionEntity.MediaOption(
                                customLists = listOf("Backlog"),
                                sectionOrder = listOf("Reading", "Completed"),
                                advancedScoring = listOf("Art"),
                                advancedScoringEnabled = false,
                                splitCompletedSectionByFormat = false,
                            ),
                    ),
            )

        val result = UserViewEntityConverter().convertFrom(source) as User.Authenticated

        assertEquals(7, result.unreadNotifications)
        assertEquals("About me", result.status.about)
        assertEquals("Supporter", result.status.donationBadge)
        assertEquals("+02:00", result.profileOption.timeZone)
        assertEquals(UserStaffNameLanguage.NATIVE, result.profileOption.staffNameLanguage)
        assertEquals(UserTitleLanguage.ENGLISH, result.profileOption.titleLanguage)
        assertEquals("SCORE", result.listOption.rowOrder)
        assertEquals(listOf("Watching", "Completed"), result.listOption.animeList.sectionOrder)
        assertTrue(result.listOption.animeList.advancedScoringEnabled)
    }

    private fun withStatisticView(statistic: UserWithStatisticEntity?): UserEntityView.WithStatistic {
        val constructor =
            UserEntityView.WithStatistic::class.java.getDeclaredConstructor(
                UserEntity::class.java,
                UserStatisticEntityView::class.java,
                UserGeneralOptionEntity::class.java,
                UserMediaOptionEntity::class.java,
                List::class.java,
                List::class.java,
                List::class.java,
            )

        return constructor.newInstance(
            UserEntity(
                about =
                    UserEntity.About(
                        name = "viewer",
                        bio = null,
                        siteUrl = "https://anilist.co/user/viewer",
                        donatorTier = null,
                        donatorBadge = null,
                    ),
                status =
                    UserEntity.Status(
                        isFollowing = false,
                        isFollower = false,
                        isBlocked = false,
                    ),
                coverImage = UserEntity.CoverImage(),
                updatedAt = null,
                createdAt = null,
                id = 1L,
            ),
            statistic?.let { UserStatisticEntityView(statistic = it) },
            UserGeneralOptionEntity(
                userId = 1L,
                airingNotifications = false,
                displayAdultContent = false,
                notificationOption = emptyList(),
                titleLanguage = UserTitleLanguage.ROMAJI,
                profileColor = null,
            ),
            UserMediaOptionEntity(
                userId = 1L,
                scoreFormat = ScoreFormat.POINT_100,
                rowOrder = null,
                anime =
                    UserMediaOptionEntity.MediaOption(
                        customLists = emptyList(),
                        sectionOrder = emptyList(),
                        advancedScoring = emptyList(),
                        advancedScoringEnabled = false,
                        splitCompletedSectionByFormat = false,
                    ),
                manga =
                    UserMediaOptionEntity.MediaOption(
                        customLists = emptyList(),
                        sectionOrder = emptyList(),
                        advancedScoring = emptyList(),
                        advancedScoringEnabled = false,
                        splitCompletedSectionByFormat = false,
                    ),
            ),
            emptyList<Any>(),
            emptyList<Any>(),
            emptyList<Any>(),
        )
    }
}
