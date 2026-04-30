/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.user.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.database.common.TransactionRunner
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.review.datasource.local.ReviewLocalSource
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.review.mapper.ReviewMapper
import co.anitrend.data.status.datasource.local.StatusLocalSource
import co.anitrend.data.status.mapper.StatusMapper
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.status.entity.view.ListStatusEntityView
import co.anitrend.data.user.datasource.local.connection.UserProfileReviewLocalSource
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import co.anitrend.data.user.entity.view.UserProfileReviewEntityView
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.status.enums.StatusType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UserProfileFeedMapperTest {
    @Test
    fun `persist uses feed writer inside transaction`() = runBlocking {
        val transactionRunner = FakeTransactionRunner()
        val writer = FakeUserProfileFeedWriter()
        val mapper =
            UserProfileFeedMapper(
                reviewConnectionMapper = UserProfileConnectionMapper.ReviewEmbed(FakeUserProfileReviewLocalSource()),
                reviewPreviewMapper = ReviewMapper.PreviewEmbed(FakeReviewLocalSource()),
                statusEmbedMapper = StatusMapper.Activity.Embed(FakeStatusLocalSource()),
                mediaEmbedMapper = FakeMediaEmbedMapper(),
                writer = writer,
                transactionRunner = transactionRunner,
            )

        mapper.onResponseDatabaseInsert(Unit)

        assertEquals(1, transactionRunner.invocationCount)
        assertEquals(1, writer.invocationCount)
    }

    @Test
    fun `onResponseMapFrom persists review and activity rows`() = runBlocking {
        val reviewConnectionLocalSource = FakeUserProfileReviewLocalSource()
        val reviewLocalSource = FakeReviewLocalSource()
        val statusLocalSource = FakeStatusLocalSource()
        val mediaEmbedMapper = FakeMediaEmbedMapper()
        val reviewConnectionMapper = UserProfileConnectionMapper.ReviewEmbed(reviewConnectionLocalSource)
        val reviewPreviewMapper = ReviewMapper.PreviewEmbed(reviewLocalSource)
        val statusEmbedMapper = StatusMapper.Activity.Embed(statusLocalSource)
        val mapper =
            UserProfileFeedMapper(
                reviewConnectionMapper = reviewConnectionMapper,
                reviewPreviewMapper = reviewPreviewMapper,
                statusEmbedMapper = statusEmbedMapper,
                mediaEmbedMapper = mediaEmbedMapper,
                writer = UserProfileFeedWriter(mediaEmbedMapper, reviewPreviewMapper, reviewConnectionMapper, statusEmbedMapper),
                transactionRunner = FakeTransactionRunner(),
            )

        mapper.onResponseDatabaseInsert(
            mapper.onResponseMapFrom(
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
                                    ),
                                ),
                        ),
                ),
            ),
        )

        assert(!reviewConnectionLocalSource.upserted.isEmpty())
        assert(!reviewLocalSource.upserted.isEmpty())
        assert(!statusLocalSource.upserted.isEmpty())
    }

    private fun mediaPayload(id: Long, title: String) =
        MediaModel.Core(
            id = id,
            title = MediaModel.Title(romaji = title, english = title, userPreferred = title),
            coverImage = MediaModel.CoverImage(large = "https://example.com/$id.jpg", medium = "https://example.com/$id.jpg"),
            type = MediaType.ANIME,
            format = MediaFormat.TV,
            status = MediaStatus.RELEASING,
            favourites = 1,
            siteUrl = "https://anilist.co/media/$id",
            isReviewBlocked = false,
        )

    private class FakeTransactionRunner : TransactionRunner {
        var invocationCount = 0

        override suspend fun run(block: suspend () -> Unit) {
            invocationCount += 1
            block()
        }
    }

    private class FakeUserProfileFeedWriter : UserProfileFeedWriterContract {
        var invocationCount = 0

        override suspend fun persist() {
            invocationCount += 1
        }
    }

    private class FakeUserProfileReviewLocalSource : UserProfileReviewLocalSource() {
        val upserted = mutableListOf<UserProfileReviewEntity>()
        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun insert(attribute: UserProfileReviewEntity): Long = 0L
        override suspend fun insert(attribute: List<UserProfileReviewEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: UserProfileReviewEntity) {}
        override suspend fun update(attribute: List<UserProfileReviewEntity>) {}
        override suspend fun delete(attribute: UserProfileReviewEntity) {}
        override suspend fun delete(attribute: List<UserProfileReviewEntity>) {}
        override suspend fun upsert(attribute: UserProfileReviewEntity) { upserted += attribute }
        override suspend fun upsert(attribute: List<UserProfileReviewEntity>) { upserted += attribute }
        override fun entryByUserIdFlow(userId: Long): Flow<List<UserProfileReviewEntityView>> = flowOf(emptyList())
        override suspend fun clearByUserId(userId: Long) {}
    }

    private class FakeReviewLocalSource : ReviewLocalSource() {
        val upserted = mutableListOf<ReviewEntity>()
        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun clearById(id: Long) {}
        override fun rawFlow(query: androidx.sqlite.db.SupportSQLiteQuery): Flow<co.anitrend.data.review.entity.view.ReviewEntityView.Core?> = flowOf(null)
        override fun rawPagingSource(query: androidx.sqlite.db.SupportSQLiteQuery) = throw NotImplementedError()
        override fun entryByUserIdFlow(userId: Long): Flow<List<ReviewEntity>> = flowOf(emptyList())
        override suspend fun insert(attribute: ReviewEntity): Long = 0L
        override suspend fun insert(attribute: List<ReviewEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: ReviewEntity) {}
        override suspend fun update(attribute: List<ReviewEntity>) {}
        override suspend fun delete(attribute: ReviewEntity) {}
        override suspend fun delete(attribute: List<ReviewEntity>) {}
        override suspend fun upsert(attribute: ReviewEntity) { upserted += attribute }
        override suspend fun upsert(attribute: List<ReviewEntity>) { upserted += attribute }
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
        override fun listStatusByUserIdFlow(userId: Long): Flow<List<ListStatusEntityView>> = flowOf(emptyList())
        override suspend fun clearListStatusByUserId(userId: Long) {}
    }

    private class FakeMediaEmbedMapper : EmbedMapper<MediaModel, MediaEntity>() {
        override val localSource: AbstractLocalSource<MediaEntity> = object : AbstractLocalSource<MediaEntity>() {
            override suspend fun count(): Int = 0
            override suspend fun clear() {}
            override suspend fun insert(attribute: MediaEntity): Long = 0
            override suspend fun insert(attribute: List<MediaEntity>): List<Long> = emptyList()
            override suspend fun update(attribute: MediaEntity) {}
            override suspend fun update(attribute: List<MediaEntity>) {}
            override suspend fun delete(attribute: MediaEntity) {}
            override suspend fun delete(attribute: List<MediaEntity>) {}
            override suspend fun upsert(attribute: MediaEntity) {}
            override suspend fun upsert(attribute: List<MediaEntity>) {}
        }
        override val converter: SupportConverter<MediaModel, MediaEntity>
            get() = throw NotImplementedError()

        override suspend fun onEmbedded(source: List<MediaModel>) {
        }

        override suspend fun persistEmbedded() {
        }
    }
}
