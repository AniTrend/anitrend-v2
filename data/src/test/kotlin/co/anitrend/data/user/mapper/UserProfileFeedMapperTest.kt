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

import co.anitrend.data.user.datasource.local.sidecar.UserProfileFeedLocalSource
import co.anitrend.data.user.entity.sidecar.UserProfileFeedEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.status.enums.StatusType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UserProfileFeedMapperTest {
    @Test
    fun `onResponseMapFrom merges aliased review and activity pages`() =
        runBlocking {
            val source =
                UserSidecarModelContainer.Feed(
                    user = UserSidecarModelContainer.Feed.User(id = 7L),
                    reviewPage =
                        UserSidecarModelContainer.Feed.ReviewPage(
                            reviews =
                                listOf(
                                    UserSidecarModelContainer.ReviewPreviewPayload(
                                        id = 1L,
                                        summary = "Sharp review",
                                        score = 89,
                                        rating = 12,
                                        ratingAmount = 4,
                                        siteUrl = "https://anilist.co/review/1",
                                        createdAt = 100L,
                                        updatedAt = 120L,
                                        mediaId = 55L,
                                        mediaType = MediaType.ANIME,
                                        media = mediaPayload(55L, "Frieren"),
                                    ),
                                ),
                        ),
                    activityPage =
                        UserSidecarModelContainer.Feed.ActivityPage(
                            listActivity =
                                listOf(
                                    UserSidecarModelContainer.ListActivityPayload(
                                        id = 8L,
                                        createdAt = 130L,
                                        status = "watched episode",
                                        progress = "5 of 12",
                                        siteUrl = "https://anilist.co/activity/8",
                                        type = StatusType.ANIME_LIST,
                                        media = mediaPayload(77L, "Delicious in Dungeon"),
                                        mediaListStatus = MediaListStatus.CURRENT,
                                        mediaListProgress = 5,
                                        mediaListVolumeProgress = null,
                                    ),
                                ),
                        ),
                )

            val result = UserProfileFeedMapper(FakeUserProfileFeedLocalSource()).onResponseMapFrom(source)

            assertEquals(7L, result.id)
            assertEquals(1, result.reviews.size)
            assertEquals("Frieren", result.reviews.first().media?.title?.userPreferred)
            assertEquals(1, result.listActivity.size)
            assertEquals(5, result.listActivity.first().mediaListProgress)
            assertEquals("Delicious in Dungeon", result.listActivity.first().media?.title?.userPreferred)
        }

    private fun mediaPayload(id: Long, title: String) =
        UserSidecarModelContainer.MediaPreviewPayload(
            id = id,
            title =
                UserSidecarModelContainer.MediaPreviewPayload.Title(
                    english = title,
                    nativeTitle = null,
                    romaji = title,
                    userPreferred = title,
                ),
            image =
                UserSidecarModelContainer.MediaPreviewPayload.Image(
                    color = null,
                    large = "https://example.com/$id.jpg",
                    medium = "https://example.com/$id.jpg",
                ),
            type = MediaType.ANIME,
            format = MediaFormat.TV,
            status = MediaStatus.RELEASING,
            episodes = 12,
            chapters = null,
            volumes = null,
            isFavourite = false,
            meanScore = 84,
            averageScore = 83,
            siteUrl = "https://anilist.co/media/$id",
            mediaList = null,
        )

    private class FakeUserProfileFeedLocalSource : UserProfileFeedLocalSource() {
        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun insert(attribute: UserProfileFeedEntity): Long = 0L

        override suspend fun insert(attribute: List<UserProfileFeedEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: UserProfileFeedEntity) {
        }

        override suspend fun update(attribute: List<UserProfileFeedEntity>) {
        }

        override suspend fun delete(attribute: UserProfileFeedEntity) {
        }

        override suspend fun delete(attribute: List<UserProfileFeedEntity>) {
        }

        override suspend fun upsert(attribute: UserProfileFeedEntity) {
        }

        override suspend fun upsert(attribute: List<UserProfileFeedEntity>) {
        }

        override fun entryByUserIdFlow(userId: Long): Flow<UserProfileFeedEntity?> = flowOf(null)

        override suspend fun clearByUserId(userId: Long) {
        }
    }
}
