/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.medialist.mapper

import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class MediaListEmbedWriterTest {
    @Test
    fun `given embedded media list entities when persisting then user custom list and custom score are flushed`() =
        runBlocking {
            val userPersistence = FakePersistEmbedded()
            val customListPersistence = FakePersistEmbedded()
            val customScorePersistence = FakePersistEmbedded()
            val localSource = FakeMediaListLocalSource()
            val writer =
                MediaListEmbedWriter(
                    userPersistence = userPersistence,
                    localSource = localSource,
                    customListPersistence = customListPersistence,
                    customScorePersistence = customScorePersistence,
                )

            writer.persist(listOf(mediaListEntity(id = 3L), mediaListEntity(id = 5L)))

            assertEquals(listOf(3L, 5L), localSource.upsertedListIds)
            assertEquals(1, userPersistence.invocationCount)
            assertEquals(1, customListPersistence.invocationCount)
            assertEquals(1, customScorePersistence.invocationCount)
        }

    @Test
    fun `given embedded media list entities with media when persisting then user and media are flushed`() =
        runBlocking {
            val userPersistence = FakePersistEmbedded()
            val mediaPersistence = FakePersistEmbedded()
            val localSource = FakeMediaListLocalSource()
            val writer =
                MediaListEmbedWithMediaWriter(
                    userPersistence = userPersistence,
                    mediaPersistence = mediaPersistence,
                    localSource = localSource,
                )

            writer.persist(listOf(mediaListEntity(id = 7L), mediaListEntity(id = 11L)))

            assertEquals(listOf(7L, 11L), localSource.upsertedListIds)
            assertEquals(1, userPersistence.invocationCount)
            assertEquals(1, mediaPersistence.invocationCount)
        }

    private fun mediaListEntity(id: Long) =
        MediaListEntity(
            mediaType = MediaType.ANIME,
            completedAt = null,
            createdAt = null,
            hiddenFromStatus = false,
            mediaId = id * 10,
            notes = null,
            priority = null,
            hidden = false,
            progress = 0,
            progressVolumes = 0,
            repeatCount = 0,
            score = 0f,
            startedAt = null,
            status = MediaListStatus.CURRENT,
            updatedAt = null,
            userId = 1L,
            userName = "viewer",
            id = id,
        )

    private class FakePersistEmbedded : PersistEmbedded {
        var invocationCount = 0

        override suspend fun persistEmbedded() {
            invocationCount += 1
        }
    }

    private class FakeMediaListLocalSource : MediaListLocalSource() {
        val upsertedListIds = mutableListOf<Long>()

        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun clearById(id: Long, userId: Long) {
        }

        override suspend fun clearByUserId(userId: Long) {
        }

        override suspend fun clearByUserName(userName: String) {
        }

        override suspend fun clearByMediaId(mediaId: Long, userId: Long) {
        }

        override suspend fun insert(attribute: MediaListEntity): Long = 0L

        override suspend fun insert(attribute: List<MediaListEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: MediaListEntity) {
        }

        override suspend fun update(attribute: List<MediaListEntity>) {
        }

        override suspend fun delete(attribute: MediaListEntity) {
        }

        override suspend fun delete(attribute: List<MediaListEntity>) {
        }

        override suspend fun upsert(attribute: MediaListEntity) {
        }

        override suspend fun upsert(attribute: List<MediaListEntity>) {
            upsertedListIds += attribute.map(MediaListEntity::id)
        }
    }
}
