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
package co.anitrend.data.medialist.mapper

import androidx.paging.PagingSource
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.core.IsolatedSidecarTestSuite
import co.anitrend.data.customlist.entity.CustomListEntity
import co.anitrend.data.customscore.entity.CustomScoreEntity
import co.anitrend.data.core.extensions.toHashId
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.medialist.entity.filter.MediaListQueryFilter
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.data.medialist.model.container.MediaListContainerModel
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.model.remote.TagModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.medialist.model.MediaListParam
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class MediaListSidecarPersistenceTest : IsolatedSidecarTestSuite() {
    @Test
    fun pagedConsumerLoadReturnsAllSeventeenJoinedEntriesWithSidecars() =
        runTest {
            seedCatalog()
            seedOrdinaryMedia(id = 21L, title = "ONE PIECE")
            val mapper = get<MediaListMapper.Paged>()
            val entries =
                (0 until 17).map { index ->
                    val mediaId = if (index == 0) 21L else 930_000L + (index - 1)
                    mediaListExtended(listId = 700_000L + index, mediaId = mediaId)
                }
            val container =
                MediaListContainerModel.Paged(
                    page = MediaListContainerModel.Paged.Page(entries = entries),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            val param =
                MediaListParam.Paged(
                    scoreFormat = ScoreFormat.POINT_100,
                    type = MediaType.ANIME,
                    userId = USER_ID,
                    userName = null,
                    status = MediaListStatus.CURRENT,
                    sort = null,
                )
            val query =
                MediaListQueryFilter
                    .Paged(authentication = mockk(relaxed = true))
                    .build(param)
            val result =
                database.mediaDao().rawPagingSource(query).load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 50,
                        placeholdersEnabled = false,
                    ),
                )

            val data =
                when (result) {
                    is PagingSource.LoadResult.Page<*, *> -> {
                        @Suppress("UNCHECKED_CAST")
                        (result.data as List<MediaEntityView.Core>)
                    }

                    else -> {
                        fail("expected LoadResult.Page but was $result")
                    }
                }
            val expectedIds = (listOf(21L) + (930_000L..930_015L)).sorted()
            assertEquals(expectedIds, data.map { it.media.id }.sorted())
            data.forEach { row ->
                val list = assertNotNull(row.mediaList)
                assertEquals(MediaListStatus.CURRENT, list.mediaList.status)
                assertEquals(USER_ID, list.mediaList.userId)
            }

            assertEquals(17, database.mediaListDao().count())
            assertEquals(17, database.mediaDao().count())
            assertEquals(1, database.userDao().count())
            assertEquals(51, database.tagConnectionDao().count())
            assertEquals(51, database.genreConnectionDao().count())
            assertEquals(17, database.customListDao().count())
            assertEquals(17, database.customScoreDao().count())
            assertEquals(17, mediaListCountView(USER_ID))
        }

    @Test
    fun entryPersistsUserMediaAndCustomRows() =
        runTest {
            seedCatalog()
            val mediaId = 830_001L
            val mapper = get<MediaListMapper.Entry>()
            val container =
                MediaListContainerModel.Entry(
                    entry = mediaListExtended(listId = 710_001L, mediaId = mediaId),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals("Entry $mediaId", view.media.title.romaji)
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            assertEquals(GENRES.sorted(), view.genres.map { it.genre.genre }.sorted())
            val list = assertNotNull(view.mediaList)
            assertEquals(710_001L, list.mediaList.id)
            assertEquals(mediaId, list.mediaList.mediaId)
            assertEquals(MediaListStatus.CURRENT, list.mediaList.status)
            assertEquals(USER_ID, list.mediaList.userId)
            assertEquals(USER_NAME, list.mediaList.userName)
            val customList = list.customList.single()
            assertEquals(CUSTOM_LIST_NAME, customList.listName)
            assertEquals(true, customList.enabled)
            assertEquals(USER_ID, customList.userId)
            assertEquals(USER_NAME, customList.userName)
            val customScore = list.customScore.single()
            assertEquals("Story", customScore.scoreName)
            assertEquals(7f, customScore.score)
            assertEquals(USER_NAME, userName(USER_ID))
        }

    @Test
    fun saveEntryPersistsListUserAndCustomRowsForSeededMediaParent() =
        runTest {
            seedCatalog()
            seedOrdinaryMedia(id = 830_002L, title = "Parent Two")
            val mapper = get<MediaListMapper.SaveEntry>()
            val container =
                MediaListContainerModel.SavedEntry(
                    entry = mediaListCore(listId = 710_002L, mediaId = 830_002L),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(830_002L).first())
            assertEquals("Parent Two", view.media.title.romaji)
            assertEquals(0, view.tags.size)
            val list = assertNotNull(view.mediaList)
            assertEquals(710_002L, list.mediaList.id)
            assertEquals(MediaListStatus.CURRENT, list.mediaList.status)
            assertEquals(USER_ID, list.mediaList.userId)
            assertEquals(USER_NAME, list.mediaList.userName)
            assertEquals(CUSTOM_LIST_NAME, list.customList.single().listName)
            assertEquals(true, list.customList.single().enabled)
            assertEquals("Story", list.customScore.single().scoreName)
            assertEquals(7f, list.customScore.single().score)
            assertEquals(1, database.mediaDao().count())
            assertEquals(1, database.mediaListDao().count())
            assertEquals(USER_NAME, userName(USER_ID))
        }

    @Test
    fun saveEntriesPersistsListUserAndCustomRowsForSeededMediaParents() =
        runTest {
            seedCatalog()
            seedOrdinaryMedia(id = 830_003L, title = "Parent Three")
            seedOrdinaryMedia(id = 830_004L, title = "Parent Four")
            val mapper = get<MediaListMapper.SaveEntries>()
            val container =
                MediaListContainerModel.SavedEntries(
                    entries =
                        listOf(
                            mediaListCore(listId = 710_003L, mediaId = 830_003L),
                            mediaListCore(listId = 710_004L, mediaId = 830_004L),
                        ),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            assertEquals(2, database.mediaListDao().count())
            assertEquals(2, database.mediaDao().count())
            assertEquals(1, database.userDao().count())
            assertEquals(2, database.customListDao().count())
            assertEquals(2, database.customScoreDao().count())
            assertEquals(0, database.tagConnectionDao().count())
            val view = assertNotNull(database.mediaDao().mediaByIdFlow(830_003L).first())
            val list = assertNotNull(view.mediaList)
            assertEquals(710_003L, list.mediaList.id)
            assertEquals(CUSTOM_LIST_NAME, list.customList.single().listName)
            assertEquals(7f, list.customScore.single().score)
            assertEquals(USER_NAME, userName(USER_ID))
        }

    @Test
    fun collectionPersistsUserMediaAndCustomRows() =
        runTest {
            seedCatalog()
            val mapper = get<MediaListMapper.Collection>()
            val container =
                MediaListContainerModel.Collection(
                    mediaListCollection =
                        MediaListContainerModel.Collection.ListCollection(
                            lists =
                                listOf(
                                    MediaListContainerModel.Collection.Group(
                                        entries =
                                            listOf(
                                                mediaListExtended(listId = 710_005L, mediaId = 830_005L),
                                                mediaListExtended(listId = 710_006L, mediaId = 830_006L),
                                            ),
                                    ),
                                ),
                            hasNextChunk = false,
                            user = user(),
                        ),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            assertEquals(2, database.mediaDao().count())
            assertEquals(2, database.mediaListDao().count())
            assertEquals(1, database.userDao().count())
            assertEquals(2, database.customListDao().count())
            assertEquals(2, database.customScoreDao().count())
            val view = assertNotNull(database.mediaDao().mediaByIdFlow(830_005L).first())
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            val list = assertNotNull(view.mediaList)
            assertEquals(710_005L, list.mediaList.id)
            assertEquals(USER_NAME, list.mediaList.userName)
            assertEquals(CUSTOM_LIST_NAME, list.customList.single().listName)
            assertEquals(USER_NAME, userName(USER_ID))
        }

    @Test
    fun embedPersistsUserAndCustomRowsWithoutMedia() =
        runTest {
            seedCatalog()
            seedOrdinaryMedia(id = 830_007L, title = "Parent Seven")
            val mapper = get<MediaListMapper.Embed>()
            val source = listOf(mediaListCore(listId = 710_007L, mediaId = 830_007L))

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(source))

            assertEquals(1, database.mediaDao().count())
            assertEquals(1, database.mediaListDao().count())
            assertEquals(1, database.userDao().count())
            assertEquals(1, database.customListDao().count())
            assertEquals(1, database.customScoreDao().count())
            assertEquals(0, database.tagConnectionDao().count())
            val view = assertNotNull(database.mediaDao().mediaByIdFlow(830_007L).first())
            val list = assertNotNull(view.mediaList)
            assertEquals(710_007L, list.mediaList.id)
            assertEquals(USER_ID, list.mediaList.userId)
            assertEquals(USER_NAME, list.mediaList.userName)
            assertEquals(CUSTOM_LIST_NAME, list.customList.single().listName)
            assertEquals(true, list.customList.single().enabled)
            assertEquals("Story", list.customScore.single().scoreName)
            assertEquals(7f, list.customScore.single().score)
            assertEquals(USER_NAME, userName(USER_ID))
        }

    @Test
    fun embedWithMediaPersistsUserAndMediaWithoutCustomRows() =
        runTest {
            seedCatalog()
            val mediaId = 830_008L
            val mapper = get<MediaListMapper.EmbedWithMedia>()
            val source = listOf(mediaListExtended(listId = 710_008L, mediaId = mediaId))

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(source))

            assertEquals(1, database.mediaDao().count())
            assertEquals(1, database.mediaListDao().count())
            assertEquals(1, database.userDao().count())
            assertEquals(0, database.customListDao().count())
            assertEquals(0, database.customScoreDao().count())
            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals("Entry $mediaId", view.media.title.romaji)
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            val list = assertNotNull(view.mediaList)
            assertEquals(710_008L, list.mediaList.id)
            assertTrue(list.customList.isEmpty())
            assertTrue(list.customScore.isEmpty())
            assertEquals(USER_NAME, userName(USER_ID))
        }

    @Test
    fun entryRefreshReplacesCustomListAndScoreValues() =
        runTest {
            seedCatalog()
            seedOrdinaryMedia(id = 830_010L, title = "Probe Parent")
            val mapper = get<MediaListMapper.Entry>()
            val enabled =
                MediaListContainerModel.Entry(
                    entry = mediaListExtended(listId = 710_010L, mediaId = 830_010L, customEnabled = true, storyScore = 7f),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(enabled))

            val first = assertNotNull(database.mediaDao().mediaByIdFlow(830_010L).first()).mediaList!!
            assertEquals(true, first.customList.single().enabled)
            assertEquals(7f, first.customScore.single().score)
            assertEquals(1, customListCountView(USER_ID, CUSTOM_LIST_NAME))

            val disabled =
                MediaListContainerModel.Entry(
                    entry = mediaListExtended(listId = 710_010L, mediaId = 830_010L, customEnabled = false, storyScore = 9f),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(disabled))

            val second = assertNotNull(database.mediaDao().mediaByIdFlow(830_010L).first()).mediaList!!
            assertEquals(false, second.customList.single().enabled)
            assertEquals(9f, second.customScore.single().score)
            assertEquals(1, database.customListDao().count())
            assertEquals(1, database.customScoreDao().count())
            assertEquals(0, customListCountView(USER_ID, CUSTOM_LIST_NAME))
        }

    @Test
    fun customListDaoReplacesBothUpsertOverloadsOnSameLogicalKey() =
        runTest {
            seedCatalog()
            seedOrdinaryMedia(id = 830_020L, title = "Boundary Parent")
            val entryMapper = get<MediaListMapper.Entry>()
            entryMapper.onResponseDatabaseInsert(
                entryMapper.onResponseMapFrom(
                    MediaListContainerModel.Entry(
                        entry = mediaListExtended(listId = 710_020L, mediaId = 830_020L),
                    ),
                ),
            )

            val entity = { enabled: Boolean ->
                CustomListEntity(
                    enabled = enabled,
                    listName = CUSTOM_LIST_NAME,
                    mediaListId = 710_020L,
                    userId = USER_ID,
                    userName = USER_NAME,
                )
            }
            database.customListDao().upsert(entity(true))
            database.customListDao().upsert(entity(false))
            assertEquals(1, database.customListDao().count())
            assertEquals("0", scalar("select enabled from custom_list where media_list_id = 710020"))

            database.customListDao().upsert(listOf(entity(true)))
            database.customListDao().upsert(listOf(entity(false)))
            assertEquals(1, database.customListDao().count())
            assertEquals("0", scalar("select enabled from custom_list where media_list_id = 710020"))
        }

    @Test
    fun customScoreDaoReplacesBothUpsertOverloadsOnSameLogicalKey() =
        runTest {
            seedCatalog()
            seedOrdinaryMedia(id = 830_021L, title = "Boundary Parent Two")
            val entryMapper = get<MediaListMapper.Entry>()
            entryMapper.onResponseDatabaseInsert(
                entryMapper.onResponseMapFrom(
                    MediaListContainerModel.Entry(
                        entry = mediaListExtended(listId = 710_021L, mediaId = 830_021L),
                    ),
                ),
            )

            val entity = { score: Float ->
                CustomScoreEntity(
                    score = score,
                    scoreName = "Story",
                    mediaListId = 710_021L,
                    userId = USER_ID,
                    userName = USER_NAME,
                )
            }
            database.customScoreDao().upsert(entity(7f))
            database.customScoreDao().upsert(entity(9f))
            assertEquals(1, database.customScoreDao().count())
            assertEquals(9f, scalar("select score from custom_score where media_list_id = 710021")?.toFloat())

            database.customScoreDao().upsert(listOf(entity(7f)))
            database.customScoreDao().upsert(listOf(entity(9f)))
            assertEquals(1, database.customScoreDao().count())
            assertEquals(9f, scalar("select score from custom_score where media_list_id = 710021")?.toFloat())
        }

    private fun scalar(sql: String): String? =
        database.openHelper.readableDatabase
            .query(sql)
            .use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }

    private suspend fun seedCatalog() {
        TAG_IDS.forEach { id ->
            database.tagDao().upsert(
                TagEntity(
                    name = "Tag $id",
                    description = null,
                    category = null,
                    isGeneralSpoiler = false,
                    isAdult = false,
                    id = id,
                ),
            )
        }
        GENRES.forEach { genre ->
            database.genreDao().upsert(
                GenreEntity(
                    id = genre.toHashId(),
                    genre = genre,
                    emoji = "",
                ),
            )
        }
    }

    private suspend fun seedOrdinaryMedia(
        id: Long,
        title: String,
    ) {
        database.mediaDao().upsert(
            MediaModelConverter().convertFrom(
                MediaModel.Core(
                    isReviewBlocked = false,
                    title = MediaModel.Title(romaji = title),
                    type = MediaType.ANIME,
                    countryOfOrigin = "JP",
                    favourites = 0,
                    siteUrl = "https://anilist.co/anime/$id",
                    id = id,
                ),
            ),
        )
    }

    private fun userName(userId: Long): String? =
        database.openHelper.readableDatabase
            .query("select user_name from user where id = $userId")
            .use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }

    private fun customListCountView(
        userId: Long,
        listName: String,
    ): Int =
        database.openHelper.readableDatabase
            .query(
                "select list_count from custom_list_count " +
                    "where user_id = $userId and list_name = '$listName'",
            ).use { cursor ->
                if (cursor.moveToFirst()) cursor.getInt(0) else 0
            }

    private fun mediaListCountView(userId: Long): Int =
        database.openHelper.readableDatabase
            .query(
                "select list_count from media_list_count " +
                    "where user_id = $userId and media_type = 'ANIME' and list_status = 'CURRENT'",
            ).use { cursor ->
                if (cursor.moveToFirst()) cursor.getInt(0) else -1
            }

    private fun user() =
        UserModel.Core(
            name = USER_NAME,
            siteUrl = "https://anilist.co/user/$USER_NAME",
            id = USER_ID,
        )

    private fun mediaForList(id: Long) =
        MediaModel.Media(
            isReviewBlocked = false,
            title = MediaModel.Title(romaji = if (id == 21L) "ONE PIECE" else "Entry $id"),
            tags =
                TAG_IDS.map {
                    TagModel.Extended(
                        rank = 80,
                        isMediaSpoiler = false,
                        name = "Tag $it",
                        id = it,
                    )
                },
            genres = GENRES,
            type = MediaType.ANIME,
            countryOfOrigin = "JP",
            favourites = 0,
            siteUrl = "https://anilist.co/anime/$id",
            id = id,
        )

    private fun mediaListExtended(
        listId: Long,
        mediaId: Long,
        customEnabled: Boolean = true,
        storyScore: Float = 7f,
    ) = MediaListModel.Extended(
        media = mediaForList(mediaId),
        advancedScores = mapOf("Story" to storyScore),
        customLists = listOf(MediaListModel.CustomList(name = CUSTOM_LIST_NAME, enabled = customEnabled)),
        createdAt = 1L,
        mediaId = mediaId,
        status = MediaListStatus.CURRENT,
        user = user(),
        id = listId,
    )

    private fun mediaListCore(
        listId: Long,
        mediaId: Long,
    ) = MediaListModel.Core(
        mediaCategory = MediaListModel.Category(MediaType.ANIME),
        advancedScores = mapOf("Story" to 7f),
        customLists = listOf(MediaListModel.CustomList(name = CUSTOM_LIST_NAME, enabled = true)),
        createdAt = 1L,
        mediaId = mediaId,
        status = MediaListStatus.CURRENT,
        user = user(),
        id = listId,
    )

    private companion object {
        const val USER_ID = 933_001L
        const val USER_NAME = "user-1333"
        const val CUSTOM_LIST_NAME = "Current Anime"
        val TAG_IDS = listOf(101L, 102L, 103L)
        val GENRES = listOf("Action", "Comedy", "Drama")
    }
}
