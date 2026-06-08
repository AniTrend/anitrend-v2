/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.auth.mapper

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.view.UserEntityView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticatedUserWriterTest {
    @Test
    fun `given authenticated user when persisting then user and option writers are invoked`() = runBlocking {
        val localSource = FakeUserLocalSource()
        val generalOptionWriter = FakeUserGeneralOptionWriter()
        val mediaOptionWriter = FakeUserMediaOptionWriter()
        val notificationWriter = FakeNotificationWriter()
        val writer =
            AuthenticatedUserWriter(
                generalOptionWriter = generalOptionWriter,
                mediaOptionWriter = mediaOptionWriter,
                notificationMapper = notificationWriter,
                localSource = localSource,
            )

        val user =
            UserEntity(
                about = UserEntity.About(name = "viewer", bio = null, siteUrl = "https://anilist.co/user/viewer", donatorTier = null, donatorBadge = null),
                status = UserEntity.Status(isFollowing = false, isFollower = false, isBlocked = false),
                coverImage = UserEntity.CoverImage(),
                updatedAt = null,
                createdAt = null,
                id = 1L,
            )

        writer.persist(user)

        assertEquals(listOf(1L), localSource.upsertedUserIds)
        assertEquals(1, generalOptionWriter.invocationCount)
        assertEquals(1, mediaOptionWriter.invocationCount)
        assertEquals(1, notificationWriter.invocationCount)
    }

    private class FakeUserGeneralOptionWriter : UserGeneralOptionWriterContract {
        var invocationCount = 0

        override suspend fun persist() {
            invocationCount += 1
        }
    }

    private class FakeUserMediaOptionWriter : UserMediaOptionWriterContract {
        var invocationCount = 0

        override suspend fun persist() {
            invocationCount += 1
        }
    }

    private class FakeNotificationWriter : NotificationPersistence {
        var invocationCount = 0

        override suspend fun persistEmbedded() {
            invocationCount += 1
        }
    }

    private class FakeUserLocalSource : UserLocalSource() {
        val upsertedUserIds = mutableListOf<Long>()

        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun clearById(id: Long) {}
        override suspend fun clearByUserName(userName: String) {}
        override suspend fun clearByMatch(userName: String) {}
        override suspend fun userById(id: Long): UserEntity? = null
        override suspend fun userById(ids: List<Long>): List<UserEntity> = emptyList()
        override fun userByIdFlow(id: Long): Flow<UserEntity?> = flowOf(null)
        override fun userByNameFlow(userName: String): Flow<UserEntity?> = flowOf(null)
        override fun rawPagingSource(query: SupportSQLiteQuery): PagingSource<Int, UserEntity> =
            object : PagingSource<Int, UserEntity>() {
                override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? = null

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> =
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null,
                    )
            }
        override fun userAuthenticated(userId: Long): Flow<UserEntityView.Authenticated?> = flowOf(null)
        override fun userByNameWithOptionsFlow(userName: String): Flow<UserEntityView.WithOptions?> = flowOf(null)
        override fun userByIdWithOptionsFlow(id: Long): Flow<UserEntityView.WithOptions?> = flowOf(null)
        override fun userByIdWithStatisticFlow(id: Long): Flow<UserEntityView.WithStatistic?> = flowOf(null)
        override suspend fun insert(attribute: UserEntity): Long = 0L
        override suspend fun insert(attribute: List<UserEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: UserEntity) {}
        override suspend fun update(attribute: List<UserEntity>) {}
        override suspend fun delete(attribute: UserEntity) {}
        override suspend fun delete(attribute: List<UserEntity>) {}
        override suspend fun upsert(attribute: UserEntity) { upsertedUserIds += attribute.id }
        override suspend fun upsert(attribute: List<UserEntity>) { upsertedUserIds += attribute.map { it.id } }
    }
}
