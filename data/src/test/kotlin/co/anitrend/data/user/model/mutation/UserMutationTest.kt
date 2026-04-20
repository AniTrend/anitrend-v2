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
package co.anitrend.data.user.model.mutation

import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.notification.enums.NotificationType
import co.anitrend.domain.user.enums.UserStaffNameLanguage
import co.anitrend.domain.user.enums.UserTitleLanguage
import co.anitrend.domain.user.model.UserParam
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserMutationTest {
    @Test
    fun `given update mutation when serialized then timezone variable key is used`() {
        val payload = UserMutation.Update(param = fixtureParam()).toMap()

        assertTrue(payload.containsKey("timezone"))
        assertFalse(payload.containsKey("timeZone"))
        assertEquals("+02:00", payload["timezone"])
        assertEquals(60, payload["activityMergeTime"])
        assertEquals(true, payload["restrictMessagesToFollowing"])

        val disabledListActivity = payload["disabledListActivity"] as? List<*>
        val first = disabledListActivity?.firstOrNull() as? Map<*, *>
        assertEquals(false, first?.get("disabled"))
        assertEquals(MediaListStatus.CURRENT, first?.get("type"))
    }

    private fun fixtureParam() =
        UserParam.Update(
            about = "Bio",
            titleLanguage = UserTitleLanguage.ROMAJI,
            activityMergeTime = 60,
            displayAdultContent = false,
            airingNotifications = true,
            scoreFormat = ScoreFormat.POINT_10,
            timeZone = "+02:00",
            rowOrder = "SCORE",
            profileColor = "blue",
            disabledListActivity =
                listOf(
                    UserParam.Update.ListActivityOption(
                        disabled = false,
                        type = MediaListStatus.CURRENT,
                    ),
                ),
            restrictMessagesToFollowing = true,
            notificationOptions =
                listOf(
                    UserParam.Update.NotificationOption(
                        enabled = true,
                        type = NotificationType.AIRING,
                    ),
                ),
            animeListOptions =
                UserParam.Update.MediaListOptions(
                    sectionOrder = listOf("Watching"),
                    splitCompletedSectionByFormat = false,
                    customLists = listOf("Seasonal"),
                    advancedScoring = listOf("Story"),
                    advancedScoringEnabled = true,
                ),
            mangaListOptions =
                UserParam.Update.MediaListOptions(
                    sectionOrder = listOf("Reading"),
                    splitCompletedSectionByFormat = false,
                    customLists = listOf("Backlog"),
                    advancedScoring = listOf("Art"),
                    advancedScoringEnabled = false,
                ),
            staffNameLanguage = UserStaffNameLanguage.ROMAJI,
        )
}
