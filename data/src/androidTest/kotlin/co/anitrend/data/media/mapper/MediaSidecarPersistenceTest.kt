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
package co.anitrend.data.media.mapper

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.core.IsolatedSidecarTestSuite
import co.anitrend.data.core.extensions.toHashId
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.link.model.LinkModel
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.media.model.container.MediaModelContainer
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.data.rank.model.RankModel
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.model.remote.TagModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class MediaSidecarPersistenceTest : IsolatedSidecarTestSuite() {
    @Test
    fun pagedStagesTagGenreLinkRankAiringAndMediaList() =
        runTest {
            seedCatalog()
            val mediaId = 810_001L
            val mapper = get<MediaMapper.Paged>()
            val source =
                MediaModelContainer.Paged(
                    page =
                        MediaModelContainer.Paged.Page(
                            media = listOf(mediaCore(id = mediaId, listEntryId = 820_001L)),
                        ),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(source))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            assertEquals(setOf(80), view.tags.map { it.connection.rank }.toSet())
            assertEquals(GENRES.sorted(), view.genres.map { it.genre.genre }.sorted())
            assertEquals(listOf(LINK_ID), view.links.map { it.id })
            assertEquals(listOf("https://example.com/media"), view.links.map { it.url })
            assertEquals(listOf(RANK_ID), view.ranks.map { it.id })
            assertEquals(listOf(1), view.ranks.map { it.rank })
            val airing = assertNotNull(view.nextAiring)
            assertEquals(mediaId, airing.mediaId)
            val list = assertNotNull(view.mediaList)
            assertEquals(820_001L, list.mediaList.id)
            assertEquals(mediaId, list.mediaList.mediaId)
            assertEquals(MediaListStatus.CURRENT, list.mediaList.status)
            assertEquals(USER_ID, list.mediaList.userId)
            assertEquals(USER_NAME, list.mediaList.userName)

            assertEquals(3, database.tagConnectionDao().count())
            assertEquals(3, database.genreConnectionDao().count())
            assertEquals(1, database.linkDao().count())
            assertEquals(1, database.rankDao().count())
            assertEquals(1, database.airingScheduleDao().count())
            assertEquals(1, database.mediaListDao().count())
            assertEquals(1, database.userDao().count())
        }

    @Test
    fun detailStagesTagGenreLinkRankAiringAndMediaList() =
        runTest {
            seedCatalog()
            val mediaId = 810_002L
            val mapper = get<MediaMapper.Detail>()
            val source =
                MediaModelContainer.Detail(
                    media = mediaExtended(id = mediaId, listEntryId = 820_002L),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(source))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            assertEquals(GENRES.sorted(), view.genres.map { it.genre.genre }.sorted())
            assertEquals(listOf("https://example.com/media"), view.links.map { it.url })
            assertEquals(listOf(1), view.ranks.map { it.rank })
            assertNotNull(view.nextAiring)
            val list = assertNotNull(view.mediaList)
            assertEquals(820_002L, list.mediaList.id)
            assertEquals(USER_ID, list.mediaList.userId)
        }

    @Test
    fun embedStagesTagGenreLinkRankOnly() =
        runTest {
            seedCatalog()
            val mediaId = 810_003L
            val mapper = get<MediaMapper.Embed>()
            val source = listOf(mediaCore(id = mediaId))

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(source))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            assertEquals(GENRES.sorted(), view.genres.map { it.genre.genre }.sorted())
            assertEquals(listOf(LINK_ID), view.links.map { it.id })
            assertEquals(listOf(RANK_ID), view.ranks.map { it.id })
            assertNull(view.nextAiring)
            assertNull(view.mediaList)
            assertEquals(0, database.airingScheduleDao().count())
            assertEquals(0, database.mediaListDao().count())
        }

    @Test
    fun embedWithAiringStagesTagGenreLinkRankAndAiring() =
        runTest {
            seedCatalog()
            val mediaId = 810_004L
            val mapper = get<MediaMapper.EmbedWithAiring>()
            val source = listOf(mediaCore(id = mediaId))

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(source))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            assertEquals(GENRES.sorted(), view.genres.map { it.genre.genre }.sorted())
            assertEquals(listOf(LINK_ID), view.links.map { it.id })
            assertEquals(listOf(RANK_ID), view.ranks.map { it.id })
            val airing = assertNotNull(view.nextAiring)
            assertEquals(mediaId, airing.mediaId)
            assertNull(view.mediaList)
            assertEquals(0, database.mediaListDao().count())
        }

    @Test
    fun embedWithMediaListStagesTagGenreLinkRankAiringAndMediaList() =
        runTest {
            seedCatalog()
            val mediaId = 810_005L
            val mapper = get<MediaMapper.EmbedWithMediaList>()
            val source = listOf(mediaCore(id = mediaId, listEntryId = 820_005L))

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(source))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals(TAG_IDS, view.tags.map { it.tag.id }.sorted())
            assertEquals(GENRES.sorted(), view.genres.map { it.genre.genre }.sorted())
            assertEquals(listOf(LINK_ID), view.links.map { it.id })
            assertEquals(listOf(RANK_ID), view.ranks.map { it.id })
            assertNotNull(view.nextAiring)
            val list = assertNotNull(view.mediaList)
            assertEquals(820_005L, list.mediaList.id)
            assertEquals(USER_ID, list.mediaList.userId)
        }

    @Test
    fun repeatMappingOnSameMapperReplacesValuesAndKeepsCardinality() =
        runTest {
            seedCatalog()
            val mediaId = 810_006L
            val mapper = get<MediaMapper.Paged>()
            val first =
                MediaModelContainer.Paged(
                    page =
                        MediaModelContainer.Paged.Page(
                            media =
                                listOf(
                                    mediaCore(
                                        id = mediaId,
                                        listEntryId = 820_006L,
                                        linkUrl = "https://example.com/media-v1",
                                        tagRank = 50,
                                        rankValue = 1,
                                    ),
                                ),
                        ),
                )
            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(first))

            val second =
                MediaModelContainer.Paged(
                    page =
                        MediaModelContainer.Paged.Page(
                            media =
                                listOf(
                                    mediaCore(
                                        id = mediaId,
                                        listEntryId = 820_006L,
                                        linkUrl = "https://example.com/media-v2",
                                        tagRank = 80,
                                        rankValue = 2,
                                    ),
                                ),
                        ),
                )
            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(second))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals(1, view.links.size)
            assertEquals("https://example.com/media-v2", view.links.single().url)
            assertEquals(3, view.tags.size)
            assertEquals(
                80,
                view.tags
                    .first { it.tag.id == 101L }
                    .connection.rank,
            )
            assertEquals(1, view.ranks.size)
            assertEquals(2, view.ranks.single().rank)
            assertEquals(3, view.genres.size)
            assertEquals(1, database.mediaListDao().count())
            assertEquals(3, database.tagConnectionDao().count())
        }

    @Test
    fun distinctDetailMappersKeepStagedBuffersIsolated() =
        runTest {
            seedCatalog()
            val firstId = 810_007L
            val secondId = 810_008L
            val firstMapper = get<MediaMapper.Detail>()
            val secondMapper = get<MediaMapper.Detail>()

            val firstMapped =
                firstMapper.onResponseMapFrom(
                    MediaModelContainer.Detail(
                        media =
                            mediaExtended(
                                id = firstId,
                                listEntryId = 820_007L,
                                linkId = 710_002L,
                                rankId = 720_002L,
                            ),
                    ),
                )
            val secondMapped =
                secondMapper.onResponseMapFrom(
                    MediaModelContainer.Detail(media = mediaExtended(id = secondId, listEntryId = 820_008L)),
                )
            secondMapper.onResponseDatabaseInsert(secondMapped)

            val secondView = assertNotNull(database.mediaDao().mediaByIdFlow(secondId).first())
            assertEquals(3, secondView.tags.size)
            assertEquals(1, secondView.links.size)

            // the first mapper's staged buffers must still hold its rows: only the
            // second mapper's own instances may have been drained by the second persist
            assertNull(database.mediaDao().mediaByIdFlow(firstId).first())
            assertEquals(1, database.mediaDao().count())
            assertEquals(3, database.tagConnectionDao().count())
            assertEquals(3, database.genreConnectionDao().count())
            assertEquals(1, database.linkDao().count())
            assertEquals(1, database.rankDao().count())
            assertEquals(1, database.mediaListDao().count())

            firstMapper.onResponseDatabaseInsert(firstMapped)

            val drainedFirst = assertNotNull(database.mediaDao().mediaByIdFlow(firstId).first())
            assertEquals(TAG_IDS, drainedFirst.tags.map { it.tag.id }.sorted())
            assertEquals(1, drainedFirst.links.size)
            assertEquals(1, drainedFirst.ranks.size)
            assertEquals(3, drainedFirst.genres.size)
            assertEquals(820_007L, assertNotNull(drainedFirst.mediaList).mediaList.id)
            assertEquals(6, database.tagConnectionDao().count())
            assertEquals(2, database.linkDao().count())
            assertEquals(2, database.rankDao().count())
        }

    @Test
    fun absentOptionalSidecarsDoNotInheritPreviousDrain() =
        runTest {
            seedCatalog()
            val mapper = get<MediaMapper.Paged>()
            val full =
                MediaModelContainer.Paged(
                    page =
                        MediaModelContainer.Paged.Page(
                            media = listOf(mediaCore(id = 810_009L, listEntryId = 820_009L)),
                        ),
                )
            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(full))

            val bare =
                MediaModelContainer.Paged(
                    page =
                        MediaModelContainer.Paged.Page(
                            media =
                                listOf(
                                    mediaCore(
                                        id = 810_010L,
                                        withSidecars = false,
                                        withAiring = false,
                                    ),
                                ),
                        ),
                )
            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(bare))

            val bareView = assertNotNull(database.mediaDao().mediaByIdFlow(810_010L).first())
            assertEquals(0, bareView.tags.size)
            assertEquals(0, bareView.genres.size)
            assertEquals(0, bareView.links.size)
            assertEquals(0, bareView.ranks.size)
            assertNull(bareView.nextAiring)
            assertNull(bareView.mediaList)

            val fullView = assertNotNull(database.mediaDao().mediaByIdFlow(810_009L).first())
            assertEquals(TAG_IDS, fullView.tags.map { it.tag.id }.sorted())
            assertEquals(3, fullView.genres.size)
            assertEquals(1, fullView.links.size)
            assertEquals(1, fullView.ranks.size)
            assertNotNull(fullView.nextAiring)
            assertNotNull(fullView.mediaList)
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

    private fun user() =
        UserModel.Core(
            name = USER_NAME,
            siteUrl = "https://anilist.co/user/$USER_NAME",
            id = USER_ID,
        )

    private fun mediaListEntry(
        id: Long,
        mediaId: Long,
    ) = MediaListModel.Core(
        mediaCategory = MediaListModel.Category(MediaType.ANIME),
        createdAt = 1L,
        mediaId = mediaId,
        status = MediaListStatus.CURRENT,
        user = user(),
        id = id,
    )

    private fun mediaCore(
        id: Long,
        listEntryId: Long? = null,
        withSidecars: Boolean = true,
        withAiring: Boolean = true,
        linkUrl: String = "https://example.com/media",
        tagRank: Int = 80,
        rankValue: Int = 1,
    ) = MediaModel.Core(
        isReviewBlocked = false,
        mediaListEntry = listEntryId?.let { mediaListEntry(it, id) },
        rankings =
            if (withSidecars) {
                listOf(
                    RankModel(
                        context = "highest rated",
                        format = MediaFormat.TV,
                        rank = rankValue,
                        type = MediaRankType.RATED,
                        id = RANK_ID,
                    ),
                )
            } else {
                emptyList()
            },
        externalLinks =
            if (withSidecars) {
                listOf(
                    LinkModel(
                        site = "Official",
                        url = linkUrl,
                        id = LINK_ID,
                    ),
                )
            } else {
                emptyList()
            },
        title = MediaModel.Title(romaji = "Media $id"),
        tags =
            if (withSidecars) {
                TAG_IDS.map {
                    TagModel.Extended(
                        rank = tagRank,
                        isMediaSpoiler = false,
                        name = "Tag $it",
                        id = it,
                    )
                }
            } else {
                emptyList()
            },
        genres = if (withSidecars) GENRES else emptyList(),
        type = MediaType.ANIME,
        countryOfOrigin = "JP",
        favourites = 0,
        siteUrl = "https://anilist.co/anime/$id",
        nextAiringEpisode =
            if (withAiring) {
                AiringScheduleModel.Core(
                    airingAt = 1_700_000_000L,
                    episode = 1,
                    mediaId = id,
                    timeUntilAiring = 3_600L,
                    id = AIRING_ID,
                )
            } else {
                null
            },
        id = id,
    )

    private fun mediaExtended(
        id: Long,
        listEntryId: Long? = null,
        withSidecars: Boolean = true,
        withAiring: Boolean = true,
        linkId: Long = LINK_ID,
        rankId: Long = RANK_ID,
    ) = MediaModel.Extended(
        isReviewBlocked = false,
        mediaListEntry = listEntryId?.let { mediaListEntry(it, id) },
        rankings =
            if (withSidecars) {
                listOf(
                    RankModel(
                        context = "highest rated",
                        format = MediaFormat.TV,
                        rank = 1,
                        type = MediaRankType.RATED,
                        id = rankId,
                    ),
                )
            } else {
                emptyList()
            },
        externalLinks =
            if (withSidecars) {
                listOf(
                    LinkModel(
                        site = "Official",
                        url = "https://example.com/media",
                        id = linkId,
                    ),
                )
            } else {
                emptyList()
            },
        title = MediaModel.Title(romaji = "Media $id"),
        tags =
            if (withSidecars) {
                TAG_IDS.map {
                    TagModel.Extended(
                        rank = 80,
                        isMediaSpoiler = false,
                        name = "Tag $it",
                        id = it,
                    )
                }
            } else {
                emptyList()
            },
        genres = if (withSidecars) GENRES else emptyList(),
        type = MediaType.ANIME,
        countryOfOrigin = "JP",
        favourites = 0,
        siteUrl = "https://anilist.co/anime/$id",
        nextAiringEpisode =
            if (withAiring) {
                AiringScheduleModel.Core(
                    airingAt = 1_700_000_000L,
                    episode = 1,
                    mediaId = id,
                    timeUntilAiring = 3_600L,
                    id = AIRING_ID,
                )
            } else {
                null
            },
        id = id,
    )

    private companion object {
        const val USER_ID = 933_001L
        const val USER_NAME = "user-1333"
        const val LINK_ID = 710_001L
        const val RANK_ID = 720_001L
        const val AIRING_ID = 730_001L
        val TAG_IDS = listOf(101L, 102L, 103L)
        val GENRES = listOf("Action", "Comedy", "Drama")
    }
}
