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
package co.anitrend.data.user.mapper

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.core.CoreTestSuite
import co.anitrend.data.user.model.UserModel
import co.anitrend.data.user.model.container.UserModelContainer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class UserSidecarPersistenceTest : CoreTestSuite() {
    @Test
    fun authenticatedViewerMappingPersistsRelatedRowsAcrossRefreshes() =
        runTest {
            val userId = 910_001L
            val mapper = get<AuthMapper>()
            val user =
                UserModel.Viewer(
                    name = "authenticated-viewer",
                    siteUrl = "https://anilist.co/user/authenticated-viewer",
                    id = userId,
                )

            val mappedUser = mapper.onResponseMapFrom(UserModelContainer.Viewer(user))
            mapper.onResponseDatabaseInsert(mappedUser)
            val refreshedUser = mapper.onResponseMapFrom(UserModelContainer.Viewer(user))
            mapper.onResponseDatabaseInsert(refreshedUser)

            val authenticated =
                assertNotNull(
                    store.userDao().userAuthenticated(userId).first(),
                )

            assertEquals(userId, authenticated.notification.userId)
            assertEquals(userId, authenticated.generalOption.userId)
            assertEquals(userId, authenticated.mediaListOption.userId)
        }

    @Test
    fun profileMappingPersistsRelatedOptionsAcrossRefreshes() =
        runTest {
            val userId = 910_002L
            val mapper = get<UserMapper.Profile>()
            val user =
                UserModel.Extended(
                    name = "profile-user",
                    siteUrl = "https://anilist.co/user/profile-user",
                    id = userId,
                    previousNames =
                        listOf(
                            UserModel.UserPreviousName(
                                createdAt = 1L,
                                name = "old-profile-name",
                                updatedAt = 2L,
                            ),
                        ),
                )

            val mappedUser = mapper.onResponseMapFrom(UserModelContainer.Profile(user))
            mapper.onResponseDatabaseInsert(mappedUser)
            val refreshedUser = mapper.onResponseMapFrom(UserModelContainer.Profile(user))
            mapper.onResponseDatabaseInsert(refreshedUser)

            val profile =
                assertNotNull(
                    store.userDao().userByIdWithOptionsFlow(userId).first(),
                )

            assertEquals(userId, profile.generalOption.userId)
            assertEquals(userId, profile.mediaListOption.userId)
            assertEquals(listOf("old-profile-name"), profile.previousNames.map { it.name })
        }
}
