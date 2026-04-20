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

import co.anitrend.data.android.database.common.TransactionRunner
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.datasource.local.option.UserGeneralOptionLocalSource
import co.anitrend.data.user.datasource.local.option.UserMediaOptionLocalSource
import co.anitrend.data.user.datasource.local.statistic.UserStatisticLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.name.UserPreviousNameEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.entity.view.UserEntityView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UserMapperTest {
    @Test
    fun `given user mapper when persisting user then statistic placeholder is ensured`() =
        runBlocking {
            val statisticLocalSource = FakeUserStatisticLocalSource()
            val transactionRunner = FakeTransactionRunner()
            val mapper =
                UserMapper.User(
                    localSource = FakeUserLocalSource(),
                    statisticMapper =
                        UserMapper.StatisticEmbed(
                            localSource = statisticLocalSource,
                        ),
                    transactionRunner = transactionRunner,
                    converter = UserModelConverter(),
                )

            mapper.onResponseDatabaseInsert(userEntity())

            assertEquals(listOf(1L), statisticLocalSource.ensuredUserIds)
            assertEquals(1, transactionRunner.invocationCount)
        }

    @Test
    fun `given profile mapper when persisting user then statistic placeholder is ensured`() =
        runBlocking {
            val statisticLocalSource = FakeUserStatisticLocalSource()
            val transactionRunner = FakeTransactionRunner()
            val mapper =
                UserMapper.Profile(
                    generalOptionMapper =
                        UserMapper.GeneralOptionEmbed(
                            localSource = FakeUserGeneralOptionLocalSource(),
                            converter = UserGeneralOptionModelConverter(),
                        ),
                    mediaOptionMapper =
                        UserMapper.MediaOptionEmbed(
                            localSource = FakeUserMediaOptionLocalSource(),
                            converter = UserMediaOptionModelConverter(),
                        ),
                    previousNameMapper =
                        UserMapper.PreviousNameEmbed(
                            localSource = FakeUserPreviousNameLocalSource(),
                        ),
                    statisticMapper =
                        UserMapper.StatisticEmbed(
                            localSource = statisticLocalSource,
                        ),
                    localSource = FakeUserLocalSource(),
                    transactionRunner = transactionRunner,
                    converter = UserModelConverter(),
                )

            mapper.onResponseDatabaseInsert(userEntity())

            assertEquals(listOf(1L), statisticLocalSource.ensuredUserIds)
            assertEquals(1, transactionRunner.invocationCount)
        }

    private fun userEntity() =
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
        )

    private class FakeUserStatisticLocalSource : UserStatisticLocalSource() {
        val ensuredUserIds = mutableListOf<Long>()

        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun ensurePlaceholder(userId: Long) {
            ensuredUserIds += userId
        }

        override suspend fun insert(attribute: UserWithStatisticEntity): Long = 0L

        override suspend fun insert(attribute: List<UserWithStatisticEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: UserWithStatisticEntity) {
        }

        override suspend fun update(attribute: List<UserWithStatisticEntity>) {
        }

        override suspend fun delete(attribute: UserWithStatisticEntity) {
        }

        override suspend fun delete(attribute: List<UserWithStatisticEntity>) {
        }

        override suspend fun upsert(attribute: UserWithStatisticEntity) {
        }

        override suspend fun upsert(attribute: List<UserWithStatisticEntity>) {
        }
    }

    private class FakeTransactionRunner : TransactionRunner {
        var invocationCount = 0

        override suspend fun run(block: suspend () -> Unit) {
            invocationCount += 1
            block()
        }
    }

    private class FakeUserLocalSource : UserLocalSource() {
        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun clearById(id: Long) {
        }

        override suspend fun clearByUserName(userName: String) {
        }

        override suspend fun clearByMatch(userName: String) {
        }

        override suspend fun userById(id: Long): UserEntity? = null

        override suspend fun userById(ids: List<Long>): List<UserEntity> = emptyList()

        override fun userByIdFlow(id: Long): Flow<UserEntity?> = flowOf(null)

        override fun userByNameFlow(userName: String): Flow<UserEntity?> = flowOf(null)

        override fun userAuthenticated(userId: Long): Flow<UserEntityView.Authenticated?> = flowOf(null)

        override fun userByNameWithOptionsFlow(userName: String): Flow<UserEntityView.WithOptions?> = flowOf(null)

        override fun userByIdWithOptionsFlow(id: Long): Flow<UserEntityView.WithOptions?> = flowOf(null)

        override fun userByIdWithStatisticFlow(id: Long): Flow<UserEntityView.WithStatistic?> = flowOf(null)

        override suspend fun insert(attribute: UserEntity): Long = 0L

        override suspend fun insert(attribute: List<UserEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: UserEntity) {
        }

        override suspend fun update(attribute: List<UserEntity>) {
        }

        override suspend fun delete(attribute: UserEntity) {
        }

        override suspend fun delete(attribute: List<UserEntity>) {
        }

        override suspend fun upsert(attribute: UserEntity) {
        }

        override suspend fun upsert(attribute: List<UserEntity>) {
        }
    }

    private class FakeUserGeneralOptionLocalSource : UserGeneralOptionLocalSource() {
        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun insert(attribute: UserGeneralOptionEntity): Long = 0L

        override suspend fun insert(attribute: List<UserGeneralOptionEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: UserGeneralOptionEntity) {
        }

        override suspend fun update(attribute: List<UserGeneralOptionEntity>) {
        }

        override suspend fun delete(attribute: UserGeneralOptionEntity) {
        }

        override suspend fun delete(attribute: List<UserGeneralOptionEntity>) {
        }

        override suspend fun upsert(attribute: UserGeneralOptionEntity) {
        }

        override suspend fun upsert(attribute: List<UserGeneralOptionEntity>) {
        }
    }

    private class FakeUserMediaOptionLocalSource : UserMediaOptionLocalSource() {
        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun insert(attribute: UserMediaOptionEntity): Long = 0L

        override suspend fun insert(attribute: List<UserMediaOptionEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: UserMediaOptionEntity) {
        }

        override suspend fun update(attribute: List<UserMediaOptionEntity>) {
        }

        override suspend fun delete(attribute: UserMediaOptionEntity) {
        }

        override suspend fun delete(attribute: List<UserMediaOptionEntity>) {
        }

        override suspend fun upsert(attribute: UserMediaOptionEntity) {
        }

        override suspend fun upsert(attribute: List<UserMediaOptionEntity>) {
        }
    }

    private class FakeUserPreviousNameLocalSource : AbstractLocalSource<UserPreviousNameEntity>() {
        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun insert(attribute: UserPreviousNameEntity): Long = 0L

        override suspend fun insert(attribute: List<UserPreviousNameEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: UserPreviousNameEntity) {
        }

        override suspend fun update(attribute: List<UserPreviousNameEntity>) {
        }

        override suspend fun delete(attribute: UserPreviousNameEntity) {
        }

        override suspend fun delete(attribute: List<UserPreviousNameEntity>) {
        }

        override suspend fun upsert(attribute: UserPreviousNameEntity) {
        }

        override suspend fun upsert(attribute: List<UserPreviousNameEntity>) {
        }
    }
}
