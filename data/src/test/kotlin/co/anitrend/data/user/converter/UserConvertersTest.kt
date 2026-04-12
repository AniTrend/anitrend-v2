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

import co.anitrend.data.user.model.UserModel
import co.anitrend.data.user.model.option.UserOptionsModel
import co.anitrend.domain.notification.enums.NotificationType
import kotlin.test.Test
import kotlin.test.assertEquals
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
}
