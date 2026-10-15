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
package co.anitrend.data.review.mapper

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.core.IsolatedSidecarTestSuite
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.review.model.container.ReviewContainerModel
import co.anitrend.data.review.model.remote.ReviewModel
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class ReviewSidecarPersistenceTest : IsolatedSidecarTestSuite() {
    @Test
    fun entryStagesEmbeddedMediaWithPresentAuthor() =
        runTest {
            val mediaId = 860_001L
            val authorId = 934_001L
            seedUser(authorId, "reviewer-one")
            val mapper = get<ReviewMapper.Entry>()
            val container =
                ReviewContainerModel.Entry(
                    entry = reviewExtended(id = 870_001L, mediaId = mediaId, userId = authorId, userName = "reviewer-one"),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            val view = assertNotNull(database.mediaDao().mediaByIdFlow(mediaId).first())
            assertEquals("Review Media $mediaId", view.media.title.romaji)
            assertEquals(1, database.reviewDao().count())
            assertEquals(1, database.userDao().count())
        }

    @Test
    fun pagedStagesMediaUsersAndReviewsAcrossRefreshes() =
        runTest {
            val mapper = get<ReviewMapper.Paged>()
            val container =
                ReviewContainerModel.Paged(
                    page =
                        ReviewContainerModel.Paged.Page(
                            entries =
                                listOf(
                                    reviewExtended(id = 870_002L, mediaId = 860_002L, userId = 934_002L, userName = "reviewer-two"),
                                    reviewExtended(id = 870_003L, mediaId = 860_003L, userId = 934_003L, userName = "reviewer-three"),
                                ),
                        ),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            assertEquals(2, database.reviewDao().count())
            assertEquals(2, database.userDao().count())
            assertEquals(2, database.mediaDao().count())
            val firstView = assertNotNull(database.mediaDao().mediaByIdFlow(860_002L).first())
            assertEquals("Review Media 860002", firstView.media.title.romaji)
            val secondView = assertNotNull(database.mediaDao().mediaByIdFlow(860_003L).first())
            assertEquals("Review Media 860003", secondView.media.title.romaji)

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            assertEquals(2, database.reviewDao().count())
            assertEquals(2, database.userDao().count())
            assertEquals(2, database.mediaDao().count())
        }

    private suspend fun seedUser(
        id: Long,
        name: String,
    ) {
        database.userDao().upsert(
            UserModelConverter().convertFrom(
                UserModel.Core(
                    name = name,
                    siteUrl = "https://anilist.co/user/$name",
                    id = id,
                ),
            ),
        )
    }

    private fun mediaPayload(id: Long) =
        MediaModel.Core(
            id = id,
            title = MediaModel.Title(romaji = "Review Media $id", english = "Review Media $id"),
            type = MediaType.ANIME,
            format = MediaFormat.TV,
            status = MediaStatus.RELEASING,
            favourites = 1,
            siteUrl = "https://anilist.co/media/$id",
            isReviewBlocked = false,
        )

    private fun reviewExtended(
        id: Long,
        mediaId: Long,
        userId: Long,
        userName: String,
    ) = ReviewModel.Extended(
        media = mediaPayload(mediaId),
        body = "body-$id",
        createdAt = 100L,
        mediaId = mediaId,
        mediaType = MediaType.ANIME,
        score = 80,
        summary = "summary-$id",
        updatedAt = 120L,
        user = UserModel.Core(name = userName, siteUrl = "https://anilist.co/user/$userName", id = userId),
        userId = userId,
        id = id,
    )
}
