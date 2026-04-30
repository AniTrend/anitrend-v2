/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.airing.mapper

import co.anitrend.data.airing.datasource.local.AiringLocalSource
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.android.mapper.PersistEmbedded
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class AiringWriterTest {
    private fun airingEntity(id: Long) =
        AiringScheduleEntity(
            airingAt = 100L,
            episode = 1,
            mediaId = id * 10,
            timeUntilAiring = 50L,
            id = id,
        )

    @Test
    fun `given airing schedules when persisting then media is flushed before local upsert`() =
        runBlocking {
            val mediaPersistence = FakePersistEmbedded()
            val localSource = FakeAiringLocalSource()
            val writer = AiringWriter(mediaPersistence = mediaPersistence, localSource = localSource)

            writer.persist(
                listOf(
                    airingEntity(id = 5L),
                    airingEntity(id = 7L),
                ),
            )

            assertEquals(listOf(5L, 7L), localSource.upsertedIds)
            assertEquals(1, mediaPersistence.invocationCount)
        }

    private class FakePersistEmbedded : PersistEmbedded {
        var invocationCount = 0

        override suspend fun persistEmbedded() {
            invocationCount += 1
        }
    }

    private class FakeAiringLocalSource : AiringLocalSource() {
        val upsertedIds = mutableListOf<Long>()

        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun airingByMediaId(mediaId: Long): AiringScheduleEntity? = null

        override fun airingByMediaIdFlow(mediaId: Long): Flow<AiringScheduleEntity> =
            flowOf(
                AiringScheduleEntity(
                    airingAt = 0L,
                    episode = 0,
                    mediaId = mediaId,
                    timeUntilAiring = 0L,
                    id = 0L,
                ),
            )

        override suspend fun insert(attribute: AiringScheduleEntity): Long = 0L

        override suspend fun insert(attribute: List<AiringScheduleEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: AiringScheduleEntity) {
        }

        override suspend fun update(attribute: List<AiringScheduleEntity>) {
        }

        override suspend fun delete(attribute: AiringScheduleEntity) {
        }

        override suspend fun delete(attribute: List<AiringScheduleEntity>) {
        }

        override suspend fun upsert(attribute: AiringScheduleEntity) {
        }

        override suspend fun upsert(attribute: List<AiringScheduleEntity>) {
            upsertedIds += attribute.map(AiringScheduleEntity::id)
        }
    }
}
