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
import co.anitrend.data.status.datasource.local.StatusLocalSource
import co.anitrend.data.status.mapper.StatusMapper
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.status.entity.view.ListStatusEntityView
import co.anitrend.data.user.datasource.local.connection.UserProfileFavouriteMediaLocalSource
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.data.user.entity.view.UserProfileFavouriteMediaEntityView
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

class UserProfileOverviewMapperTest {
    @Test
    fun `persist uses overview writer inside transaction`() = runBlocking {
        val transactionRunner = FakeTransactionRunner()
        val writer = FakeUserProfileOverviewWriter()
        val mapper =
            UserProfileOverviewMapper(
                favouriteEmbedMapper = UserProfileConnectionMapper.FavouriteEmbed(FakeUserProfileFavouriteMediaLocalSource()),
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
    fun `onResponseMapFrom and persist store favourites activities and embedded media`() = runBlocking {
        val favouriteLocalSource = FakeUserProfileFavouriteMediaLocalSource()
        val statusLocalSource = FakeStatusLocalSource()
        val mediaEmbedMapper = FakeMediaEmbedMapper()
        val favouriteEmbedMapper = UserProfileConnectionMapper.FavouriteEmbed(favouriteLocalSource)
        val statusEmbedMapper = StatusMapper.Activity.Embed(statusLocalSource)
        val mapper =
            UserProfileOverviewMapper(
                favouriteEmbedMapper = favouriteEmbedMapper,
                statusEmbedMapper = statusEmbedMapper,
                mediaEmbedMapper = mediaEmbedMapper,
                writer = UserProfileOverviewWriter(mediaEmbedMapper, favouriteEmbedMapper, statusEmbedMapper),
                transactionRunner = FakeTransactionRunner(),
            )

        mapper.onResponseDatabaseInsert(
            mapper.onResponseMapFrom(
                UserSidecarModelContainer.Overview(
                    user =
                        UserSidecarModelContainer.Overview.User(
                            id = 5L,
                            favourites =
                                UserSidecarModelContainer.Overview.User.Favourites(
                                    anime = UserSidecarModelContainer.MediaConnection(edges = listOf(mediaEdge(10L, "Cowboy Bebop", MediaType.ANIME))),
                                    manga = UserSidecarModelContainer.MediaConnection(edges = listOf(mediaEdge(11L, "Vagabond", MediaType.MANGA))),
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
                                    ),
                                ),
                        ),
                ),
            ),
        )

        assertEquals(2, favouriteLocalSource.upserted.size)
        assertEquals(1, statusLocalSource.upserted.size)
    }

    private fun mediaEdge(id: Long, title: String, type: MediaType) =
        UserSidecarModelContainer.MediaConnection.MediaEdge(
            favouriteOrder = 1,
            node = mediaPayload(id, title, type),
        )

    private fun mediaPayload(id: Long, title: String, type: MediaType) =
        MediaModel.Core(
            id = id,
            title = MediaModel.Title(romaji = title, english = title, userPreferred = title),
            coverImage = MediaModel.CoverImage(large = "https://example.com/$id.jpg", medium = "https://example.com/$id.jpg"),
            type = type,
            format = MediaFormat.TV,
            status = MediaStatus.FINISHED,
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

    private class FakeUserProfileOverviewWriter : UserProfileOverviewWriterContract {
        var invocationCount = 0

        override suspend fun persist() {
            invocationCount += 1
        }
    }

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
        override fun entryByUserIdFlow(userId: Long): Flow<List<UserProfileFavouriteMediaEntityView>> = flowOf(emptyList())
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
        override fun listStatusByUserIdFlow(userId: Long): Flow<List<ListStatusEntityView>> = flowOf(emptyList())
        override suspend fun clearListStatusByUserId(userId: Long) {}
    }

    private class FakeMediaEmbedMapper : EmbedMapper<MediaModel, MediaEntity>() {
        var embeddedCount = 0
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
            embeddedCount += source.size
        }

        override suspend fun persistEmbedded() {
        }
    }
}
