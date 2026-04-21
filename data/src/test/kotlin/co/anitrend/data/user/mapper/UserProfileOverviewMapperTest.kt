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

import co.anitrend.data.status.datasource.local.StatusLocalSource
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.datasource.local.connection.UserProfileFavouriteMediaLocalSource
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
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

class UserProfileOverviewMapperTest {
    @Test
    fun `onResponseMapFrom and persist store favourites and activities for the user`() =
        runBlocking {
            val source =
                UserSidecarModelContainer.Overview(
                    user =
                        UserSidecarModelContainer.Overview.User(
                            id = 5L,
                            favourites =
                                UserSidecarModelContainer.Overview.User.Favourites(
                                    anime =
                                        UserSidecarModelContainer.MediaConnection(
                                            edges =
                                                listOf(
                                                    UserSidecarModelContainer.MediaConnection.MediaEdge(
                                                        favouriteOrder = 1,
                                                        node = mediaPayload(10L, "Cowboy Bebop", MediaType.ANIME),
                                                    ),
                                                ),
                                        ),
                                    manga =
                                        UserSidecarModelContainer.MediaConnection(
                                            edges =
                                                listOf(
                                                    UserSidecarModelContainer.MediaConnection.MediaEdge(
                                                        favouriteOrder = 1,
                                                        node = mediaPayload(11L, "Vagabond", MediaType.MANGA),
                                                    ),
                                                ),
                                        ),
                                ),
                        ),
                    page =
                        UserSidecarModelContainer.Overview.OverviewPage(
                            activities =
                                listOf(
                                    UserSidecarModelContainer.ListActivityPayload(
                                        id = 101L,
                                        createdAt = 1_700_000_000L,
                                        status = "watched episode",
                                        progress = "12 of 26",
                                        siteUrl = "https://anilist.co/activity/101",
                                        type = StatusType.ANIME_LIST,
                                        media = mediaPayload(10L, "Cowboy Bebop", MediaType.ANIME),
                                        mediaListStatus = MediaListStatus.CURRENT,
                                        mediaListProgress = 12,
                                        mediaListVolumeProgress = null,
                                    ),
                                ),
                        ),
                )

            val fakeFavouriteSource = FakeUserProfileFavouriteMediaLocalSource()
            val fakeStatusSource = FakeStatusLocalSource()
            val mapper = UserProfileOverviewMapper(
                favouriteMediaLocalSource = fakeFavouriteSource,
                statusLocalSource = fakeStatusSource,
            )
            val mapped = mapper.onResponseMapFrom(source)
            mapper.onResponseDatabaseInsert(mapped)

            // Favourites: 1 anime + 1 manga = 2 total
            assertEquals(2, fakeFavouriteSource.upserted.size)
            val anime = fakeFavouriteSource.upserted.first { it.category == "ANIME" }
            assertEquals(10L, anime.mediaId)
            assertEquals("Cowboy Bebop", anime.titleUserPreferred)
            assertEquals(5L, anime.userId)
            val manga = fakeFavouriteSource.upserted.first { it.category == "MANGA" }
            assertEquals(11L, manga.mediaId)

            // Activities
            assertEquals(1, fakeStatusSource.upserted.size)
            assertEquals(101L, fakeStatusSource.upserted.first().id)
            assertEquals(12, fakeStatusSource.upserted.first().mediaListProgress)
        }

    @Test
    fun `onResponseMapFrom handles empty favourites and activity page`() =
        runBlocking {
            val source =
                UserSidecarModelContainer.Overview(
                    user =
                        UserSidecarModelContainer.Overview.User(
                            id = 99L,
                            favourites =
                                UserSidecarModelContainer.Overview.User.Favourites(
                                    anime = UserSidecarModelContainer.MediaConnection(edges = emptyList()),
                                    manga = UserSidecarModelContainer.MediaConnection(edges = emptyList()),
                                ),
                        ),
                    page = UserSidecarModelContainer.Overview.OverviewPage(activities = emptyList()),
                )

            val fakeFavouriteSource = FakeUserProfileFavouriteMediaLocalSource()
            val fakeStatusSource = FakeStatusLocalSource()
            val mapper = UserProfileOverviewMapper(
                favouriteMediaLocalSource = fakeFavouriteSource,
                statusLocalSource = fakeStatusSource,
            )
            val mapped = mapper.onResponseMapFrom(source)
            mapper.onResponseDatabaseInsert(mapped)

            assertEquals(0, fakeFavouriteSource.upserted.size)
            assertEquals(0, fakeStatusSource.upserted.size)
        }

    private fun mediaPayload(
        id: Long,
        title: String,
        type: MediaType,
    ) = UserSidecarModelContainer.MediaPreviewPayload(
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
        type = type,
        format = MediaFormat.TV,
        status = MediaStatus.FINISHED,
        episodes = 26,
        chapters = null,
        volumes = null,
        isFavourite = true,
        meanScore = 88,
        averageScore = 86,
        siteUrl = "https://anilist.co/media/$id",
        mediaList = null,
    )

    private class FakeUserProfileFavouriteMediaLocalSource : UserProfileFavouriteMediaLocalSource() {
        val upserted = mutableListOf<UserProfileFavouriteMediaEntity>()

        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun insert(attribute: UserProfileFavouriteMediaEntity): Long = 0L
        override suspend fun insert(attribute: List<UserProfileFavouriteMediaEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: UserProfileFavouriteMediaEntity) {}
        override suspend fun update(attribute: List<UserProfileFavouriteMediaEntity>) {}
        override suspend fun delete(attribute: UserProfileFavouriteMediaEntity) {}
        override suspend fun delete(attribute: List<UserProfileFavouriteMediaEntity>) {}
        override suspend fun upsert(attribute: UserProfileFavouriteMediaEntity) { upserted += attribute }
        override suspend fun upsert(attribute: List<UserProfileFavouriteMediaEntity>) { upserted += attribute }
        override fun entryByUserIdFlow(userId: Long): Flow<List<UserProfileFavouriteMediaEntity>> = flowOf(emptyList())
        override suspend fun clearByUserId(userId: Long) {}
    }

    private class FakeStatusLocalSource : StatusLocalSource() {
        val upserted = mutableListOf<StatusEntity.ListStatus>()

        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun insert(attribute: StatusEntity.ListStatus): Long = 0L
        override suspend fun insert(attribute: List<StatusEntity.ListStatus>): List<Long> = emptyList()
        override suspend fun update(attribute: StatusEntity.ListStatus) {}
        override suspend fun update(attribute: List<StatusEntity.ListStatus>) {}
        override suspend fun delete(attribute: StatusEntity.ListStatus) {}
        override suspend fun delete(attribute: List<StatusEntity.ListStatus>) {}
        override suspend fun upsert(attribute: StatusEntity.ListStatus) { upserted += attribute }
        override suspend fun upsert(attribute: List<StatusEntity.ListStatus>) { upserted += attribute }
        override fun listStatusByUserIdFlow(userId: Long): Flow<List<StatusEntity.ListStatus>> = flowOf(emptyList())
        override suspend fun clearListStatusByUserId(userId: Long) {}
    }
}
