/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.media.mapper

import androidx.paging.PagingSource
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.connection.MediaCharacterConnectionEntity
import co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.common.CountryCode
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class MediaWriterTest {
    @Test
    fun `paged writer flushes tag genre airing and media list after local upsert`() = runBlocking {
        val localSource = FakeMediaLocalSource()
        val writer =
            MediaPagedWriter(
                localSource = localSource,
                tagPersistence = FakePersistEmbedded(),
                genrePersistence = FakePersistEmbedded(),
                airingPersistence = FakePersistEmbedded(),
                mediaListPersistence = FakePersistEmbedded(),
            )

        writer.persist(listOf(mediaEntity(1L), mediaEntity(2L)))

        assertEquals(listOf(1L, 2L), localSource.upsertedListIds)
    }

    @Test
    fun `detail writer flushes embedded writers after local upsert`() = runBlocking {
        val localSource = FakeMediaLocalSource()
        val link = FakePersistEmbedded()
        val rank = FakePersistEmbedded()
        val tag = FakePersistEmbedded()
        val genre = FakePersistEmbedded()
        val airing = FakePersistEmbedded()
        val mediaList = FakePersistEmbedded()
        val writer =
            MediaDetailWriter(
                localSource = localSource,
                linkPersistence = link,
                rankPersistence = rank,
                tagPersistence = tag,
                genrePersistence = genre,
                airingPersistence = airing,
                mediaListPersistence = mediaList,
            )

        writer.persist(mediaEntity(3L))

        assertEquals(listOf(3L), localSource.upsertedSingleIds)
        assertEquals(1, link.invocationCount)
        assertEquals(1, rank.invocationCount)
        assertEquals(1, tag.invocationCount)
        assertEquals(1, genre.invocationCount)
        assertEquals(1, airing.invocationCount)
        assertEquals(1, mediaList.invocationCount)
    }

    @Test
    fun `embed writer flushes tag and genre after local upsert`() = runBlocking {
        val localSource = FakeMediaLocalSource()
        val tag = FakePersistEmbedded()
        val genre = FakePersistEmbedded()
        val writer = MediaEmbedWriter(localSource = localSource, tagPersistence = tag, genrePersistence = genre)

        writer.persist(listOf(mediaEntity(5L)))

        assertEquals(listOf(5L), localSource.upsertedListIds)
        assertEquals(1, tag.invocationCount)
        assertEquals(1, genre.invocationCount)
    }

    @Test
    fun `embed with airing writer flushes tag genre and airing after local upsert`() = runBlocking {
        val localSource = FakeMediaLocalSource()
        val tag = FakePersistEmbedded()
        val genre = FakePersistEmbedded()
        val airing = FakePersistEmbedded()
        val writer =
            MediaEmbedWithAiringWriter(
                localSource = localSource,
                tagPersistence = tag,
                genrePersistence = genre,
                airingPersistence = airing,
            )

        writer.persist(listOf(mediaEntity(7L)))

        assertEquals(listOf(7L), localSource.upsertedListIds)
        assertEquals(1, tag.invocationCount)
        assertEquals(1, genre.invocationCount)
        assertEquals(1, airing.invocationCount)
    }

    @Test
    fun `embed with media list writer flushes tag genre airing and media list after local upsert`() = runBlocking {
        val localSource = FakeMediaLocalSource()
        val tag = FakePersistEmbedded()
        val genre = FakePersistEmbedded()
        val airing = FakePersistEmbedded()
        val mediaList = FakePersistEmbedded()
        val writer =
            MediaEmbedWithMediaListWriter(
                localSource = localSource,
                tagPersistence = tag,
                genrePersistence = genre,
                airingPersistence = airing,
                mediaListPersistence = mediaList,
            )

        writer.persist(listOf(mediaEntity(11L)))

        assertEquals(listOf(11L), localSource.upsertedListIds)
        assertEquals(1, tag.invocationCount)
        assertEquals(1, genre.invocationCount)
        assertEquals(1, airing.invocationCount)
        assertEquals(1, mediaList.invocationCount)
    }

    private fun mediaEntity(id: Long) =
        MediaEntity(
            coverImage = MediaEntity.CoverImage(color = null, extraLarge = null, large = null, medium = null, banner = null),
            title = MediaEntity.Title(romaji = "Title $id", english = null, original = null, userPreferred = "Title $id"),
            trailer = null,
            nextAiringId = null,
            averageScore = null,
            chapters = null,
            countryOfOrigin = "JP",
            description = null,
            duration = null,
            endDate = null,
            episodes = null,
            favourites = 0,
            format = null,
            hashTag = null,
            isAdult = false,
            isFavourite = false,
            isFavouriteBlocked = false,
            isLicensed = null,
            isRecommendationBlocked = false,
            isReviewBlocked = false,
            isLocked = null,
            meanScore = null,
            popularity = null,
            season = null,
            siteUrl = null,
            source = null,
            startDate = null,
            status = null,
            synonyms = emptyList(),
            trending = null,
            type = MediaType.ANIME,
            updatedAt = null,
            volumes = null,
            malId = null,
            id = id,
        )

    private class FakePersistEmbedded : PersistEmbedded {
        var invocationCount = 0

        override suspend fun persistEmbedded() {
            invocationCount += 1
        }
    }

    private class FakeMediaLocalSource : MediaLocalSource() {
        val upsertedSingleIds = mutableListOf<Long>()
        val upsertedListIds = mutableListOf<Long>()

        override suspend fun count(): Int = 0
        override suspend fun clear() {}
        override suspend fun clearById(id: Long) {}
        override fun mediaByIdFlow(id: Long): Flow<MediaEntityView.Extended?> = flowOf(null)
        override fun rawFlow(query: SupportSQLiteQuery): Flow<MediaEntityView.Core?> = flowOf(null)
        override fun rawPagingSource(query: SupportSQLiteQuery): PagingSource<Int, MediaEntityView.Core> = throw NotImplementedError()
        override fun mediaCharactersPagingSource(mediaId: Long): PagingSource<Int, MediaCharacterConnectionEntity> = throw NotImplementedError()
        override fun mediaStaffPagingSource(mediaId: Long): PagingSource<Int, MediaStaffConnectionEntity> = throw NotImplementedError()
        override suspend fun mediaCharactersCount(mediaId: Long): Int = 0
        override suspend fun mediaCharactersMaxSortIndex(mediaId: Long): Int? = null
        override suspend fun mediaStaffCount(mediaId: Long): Int = 0
        override suspend fun mediaStaffMaxSortIndex(mediaId: Long): Int? = null
        override suspend fun clearMediaCharactersByMediaId(mediaId: Long) {}
        override suspend fun clearMediaStaffByMediaId(mediaId: Long) {}
        override suspend fun upsertMediaCharacters(attribute: List<MediaCharacterConnectionEntity>) {}
        override suspend fun upsertMediaStaff(attribute: List<MediaStaffConnectionEntity>) {}
        override suspend fun insert(attribute: MediaEntity): Long = 0L
        override suspend fun insert(attribute: List<MediaEntity>): List<Long> = emptyList()
        override suspend fun update(attribute: MediaEntity) {}
        override suspend fun update(attribute: List<MediaEntity>) {}
        override suspend fun delete(attribute: MediaEntity) {}
        override suspend fun delete(attribute: List<MediaEntity>) {}
        override suspend fun upsert(attribute: MediaEntity) { upsertedSingleIds += attribute.id }
        override suspend fun upsert(attribute: List<MediaEntity>) { upsertedListIds += attribute.map(MediaEntity::id) }
    }
}
