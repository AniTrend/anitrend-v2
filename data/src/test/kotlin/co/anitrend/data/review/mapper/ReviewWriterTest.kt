/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.review.mapper

import androidx.paging.PagingSource
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.review.datasource.local.ReviewLocalSource
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.review.entity.view.ReviewEntityView
import co.anitrend.domain.review.enums.ReviewRating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewWriterTest {
    @Test
    fun `given review entity when persisting then media is flushed before local upsert`() =
        runBlocking {
            val mediaPersistence = FakePersistEmbedded()
            val userPersistence = FakePersistEmbedded()
            val localSource = FakeReviewLocalSource()
            val writer = ReviewWriter(mediaPersistence = mediaPersistence, userPersistence = userPersistence, localSource = localSource)

            writer.persist(reviewEntity(id = 5L))

            assertEquals(listOf(5L), localSource.upsertedSingleIds)
            assertEquals(1, mediaPersistence.invocationCount)
            assertEquals(0, userPersistence.invocationCount)
        }

    @Test
    fun `given review entities when persisting then media and user are flushed before local upsert`() =
        runBlocking {
            val mediaPersistence = FakePersistEmbedded()
            val userPersistence = FakePersistEmbedded()
            val localSource = FakeReviewLocalSource()
            val writer = ReviewWriter(mediaPersistence = mediaPersistence, userPersistence = userPersistence, localSource = localSource)

            writer.persist(listOf(reviewEntity(id = 7L), reviewEntity(id = 11L)))

            assertEquals(listOf(7L, 11L), localSource.upsertedListIds)
            assertEquals(1, mediaPersistence.invocationCount)
            assertEquals(1, userPersistence.invocationCount)
        }

    private fun reviewEntity(id: Long) =
        ReviewEntity(
            body = "body",
            createdAt = 1L,
            mediaId = 2L,
            isPrivate = false,
            rating = 0,
            ratingAmount = 0,
            score = 0,
            siteUrl = "https://anilist.co/review/$id",
            summary = "summary",
            updatedAt = 1L,
            userId = 3L,
            userRating = ReviewRating.NO_VOTE,
            id = id,
        )

    private class FakePersistEmbedded : PersistEmbedded {
        var invocationCount = 0

        override suspend fun persistEmbedded() {
            invocationCount += 1
        }
    }

    private class FakeReviewLocalSource : ReviewLocalSource() {
        val upsertedSingleIds = mutableListOf<Long>()
        val upsertedListIds = mutableListOf<Long>()

        override suspend fun count(): Int = 0

        override suspend fun clear() {
        }

        override suspend fun clearById(id: Long) {
        }

        override fun rawFlow(query: SupportSQLiteQuery): Flow<ReviewEntityView.Core?> = flowOf(null)

        override fun rawPagingSource(query: SupportSQLiteQuery): PagingSource<Int, ReviewEntityView.Core> =
            throw NotImplementedError()

        override fun entryByUserIdFlow(userId: Long): Flow<List<ReviewEntity>> = flowOf(emptyList())

        override suspend fun insert(attribute: ReviewEntity): Long = 0L

        override suspend fun insert(attribute: List<ReviewEntity>): List<Long> = emptyList()

        override suspend fun update(attribute: ReviewEntity) {
        }

        override suspend fun update(attribute: List<ReviewEntity>) {
        }

        override suspend fun delete(attribute: ReviewEntity) {
        }

        override suspend fun delete(attribute: List<ReviewEntity>) {
        }

        override suspend fun upsert(attribute: ReviewEntity) {
            upsertedSingleIds += attribute.id
        }

        override suspend fun upsert(attribute: List<ReviewEntity>) {
            upsertedListIds += attribute.map(ReviewEntity::id)
        }
    }
}
