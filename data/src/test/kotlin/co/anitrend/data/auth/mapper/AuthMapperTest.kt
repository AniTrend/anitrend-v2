/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.auth.mapper

import co.anitrend.data.android.database.common.TransactionRunner
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.datasource.local.notification.UserNotificationLocalSource
import co.anitrend.data.user.datasource.local.option.UserGeneralOptionLocalSource
import co.anitrend.data.user.datasource.local.option.UserMediaOptionLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.notification.UserNotificationEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.mapper.UserMapper
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthMapperTest {
    @Test
    fun `given auth mapper when persisting user then writer is invoked inside transaction`() =
        runBlocking {
            val transactionRunner = FakeTransactionRunner()
            val writer = FakeAuthenticatedUserWriter()
            val mapper =
                AuthMapper(
                    generalOptionMapper = UserMapper.GeneralOptionEmbed(localSource = FakeUserGeneralOptionLocalSource(), converter = UserGeneralOptionModelConverter()),
                    mediaOptionMapper = UserMapper.MediaOptionEmbed(localSource = FakeUserMediaOptionLocalSource(), converter = UserMediaOptionModelConverter()),
                    notificationMapper = UserMapper.NotificationEmbed(localSource = FakeUserNotificationLocalSource()),
                    writer = writer,
                    transactionRunner = transactionRunner,
                    converter = UserModelConverter(),
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

            mapper.onResponseDatabaseInsert(user)

            assertEquals(1, transactionRunner.invocationCount)
            assertEquals(listOf(1L), writer.persistedUserIds)
        }

    private class FakeTransactionRunner : TransactionRunner {
        var invocationCount = 0

        override suspend fun run(block: suspend () -> Unit) {
            invocationCount += 1
            block()
        }
    }

    private class FakeAuthenticatedUserWriter : AuthenticatedUserWriterContract {
        val persistedUserIds = mutableListOf<Long>()

        override suspend fun persist(user: UserEntity) {
            persistedUserIds += user.id
        }
    }

    private class FakeUserGeneralOptionLocalSource : UserGeneralOptionLocalSource() {
        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun insert(attribute: UserGeneralOptionEntity): Long = 0L
        override suspend fun insert(attribute: List<UserGeneralOptionEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: UserGeneralOptionEntity) {}
        override suspend fun update(attribute: List<UserGeneralOptionEntity>) {}
        override suspend fun delete(attribute: UserGeneralOptionEntity) {}
        override suspend fun delete(attribute: List<UserGeneralOptionEntity>) {}
        override suspend fun upsert(attribute: UserGeneralOptionEntity) {}
        override suspend fun upsert(attribute: List<UserGeneralOptionEntity>) {}
    }

    private class FakeUserMediaOptionLocalSource : UserMediaOptionLocalSource() {
        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun insert(attribute: UserMediaOptionEntity): Long = 0L
        override suspend fun insert(attribute: List<UserMediaOptionEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: UserMediaOptionEntity) {}
        override suspend fun update(attribute: List<UserMediaOptionEntity>) {}
        override suspend fun delete(attribute: UserMediaOptionEntity) {}
        override suspend fun delete(attribute: List<UserMediaOptionEntity>) {}
        override suspend fun upsert(attribute: UserMediaOptionEntity) {}
        override suspend fun upsert(attribute: List<UserMediaOptionEntity>) {}
    }

    private class FakeUserNotificationLocalSource : UserNotificationLocalSource() {
        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun insert(attribute: UserNotificationEntity): Long = 0L
        override suspend fun insert(attribute: List<UserNotificationEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: UserNotificationEntity) {}
        override suspend fun update(attribute: List<UserNotificationEntity>) {}
        override suspend fun delete(attribute: UserNotificationEntity) {}
        override suspend fun delete(attribute: List<UserNotificationEntity>) {}
        override suspend fun upsert(attribute: UserNotificationEntity) {}
        override suspend fun upsert(attribute: List<UserNotificationEntity>) {}
    }
}
