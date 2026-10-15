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

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.core.IsolatedSidecarTestSuite
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.model.UserModel
import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.data.user.model.option.UserOptionsModel
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.status.enums.StatusType
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
internal class ProfileSidecarPersistenceTest : IsolatedSidecarTestSuite() {
    @Test
    fun statisticPersistsUserAndStatisticRows() =
        runTest {
            val userId = 935_001L
            val mapper = get<UserMapper.Statistic>()
            val container =
                UserModelContainer.WithStatistic(
                    user =
                        UserModel.WithStatistic(
                            name = "stats-user",
                            siteUrl = "https://anilist.co/user/stats-user",
                            id = userId,
                            statistics = null,
                        ),
                )

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            assertNotNull(database.userDao().userById(userId))
            assertEquals(1, database.userDao().count())
            assertEquals(1, database.userStatisticDao().count())
        }

    @Test
    fun overviewPersistsFavouritesActivitiesAndMediaWithoutCountGrowth() =
        runTest {
            val userId = 935_002L
            seedOwner(userId)
            val mapper = get<UserProfileOverviewMapper>()
            val container = overview(userId)

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            val favourites = database.userProfileFavouriteMediaDao().entryByUserIdFlow(userId).first()
            assertEquals(2, favourites.size)
            assertEquals(setOf(861_001L, 861_002L), favourites.map { it.favourite.mediaId }.toSet())
            assertEquals(MediaType.ANIME, favourites.first { it.favourite.mediaId == 861_001L }.favourite.category)
            assertEquals(MediaType.MANGA, favourites.first { it.favourite.mediaId == 861_002L }.favourite.category)
            val activities = database.statusDao().listStatusByUserIdFlow(userId).first()
            assertEquals(1, activities.size)
            val activity = activities.single()
            assertEquals("watched episode", activity.activity.status)
            assertEquals("1 of 12", activity.activity.progress)
            assertEquals(861_003L, activity.activity.mediaId)
            assertEquals(3, database.mediaDao().count())

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            assertEquals(2, database.userProfileFavouriteMediaDao().count())
            assertEquals(1, database.statusDao().count())
            assertEquals(3, database.mediaDao().count())
            assertEquals(
                2,
                database
                    .userProfileFavouriteMediaDao()
                    .entryByUserIdFlow(userId)
                    .first()
                    .size,
            )
        }

    @Test
    fun feedPersistsReviewConnectionsActivitiesAndMediaWithoutCountGrowth() =
        runTest {
            val userId = 935_003L
            seedOwner(userId)
            val mapper = get<UserProfileFeedMapper>()
            val container = feed(userId)

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            val reviews = database.userProfileReviewDao().entryByUserIdFlow(userId).first()
            assertEquals(1, reviews.size)
            val reviewConnection = reviews.single().connection
            assertEquals(871_001L, reviewConnection.reviewId)
            assertEquals(861_011L, reviewConnection.mediaId)
            val activities = database.statusDao().listStatusByUserIdFlow(userId).first()
            assertEquals(1, activities.size)
            assertEquals("5 of 12", activities.single().activity.progress)
            assertEquals(861_012L, activities.single().activity.mediaId)
            assertEquals(2, database.mediaDao().count())
            assertEquals(1, database.reviewDao().count())

            mapper.onResponseDatabaseInsert(mapper.onResponseMapFrom(container))

            assertEquals(1, database.userProfileReviewDao().count())
            assertEquals(1, database.statusDao().count())
            assertEquals(1, database.reviewDao().count())
            assertEquals(2, database.mediaDao().count())
        }

    @Test
    fun overviewRefreshReplacesFavouriteOrdering() =
        runTest {
            val userId = 935_010L
            val otherUserId = 935_011L
            seedOwner(userId)
            seedOwner(otherUserId)
            val mapper = get<UserProfileOverviewMapper>()
            val otherMapper = get<UserProfileOverviewMapper>()

            mapper.onResponseDatabaseInsert(
                mapper.onResponseMapFrom(
                    overviewWithFavourites(userId, listOf(favEdge(862_001L, "Fav A", 1), favEdge(862_002L, "Fav B", 2))),
                ),
            )
            otherMapper.onResponseDatabaseInsert(
                otherMapper.onResponseMapFrom(
                    overviewWithFavourites(otherUserId, listOf(favEdge(863_001L, "Other Fav", 1))),
                ),
            )

            val first = database.userProfileFavouriteMediaDao().entryByUserIdFlow(userId).first()
            assertEquals(listOf(862_001L, 862_002L), first.map { it.favourite.mediaId })
            assertEquals(listOf(0, 1), first.map { it.favourite.sortIndex })

            mapper.onResponseDatabaseInsert(
                mapper.onResponseMapFrom(
                    overviewWithFavourites(userId, listOf(favEdge(862_002L, "Fav B", 1), favEdge(862_001L, "Fav A", 2))),
                ),
            )

            val second = database.userProfileFavouriteMediaDao().entryByUserIdFlow(userId).first()
            assertEquals(2, second.size)
            assertEquals(listOf(862_002L, 862_001L), second.map { it.favourite.mediaId })
            assertEquals(listOf(0, 1), second.map { it.favourite.sortIndex })
            val other = database.userProfileFavouriteMediaDao().entryByUserIdFlow(otherUserId).first()
            assertEquals(listOf(863_001L), other.map { it.favourite.mediaId })
            assertEquals(3, database.userProfileFavouriteMediaDao().count())
        }

    @Test
    fun feedRefreshReplacesReviewOrdering() =
        runTest {
            val userId = 935_013L
            val otherUserId = 935_014L
            seedOwner(userId)
            seedOwner(otherUserId)
            val mapper = get<UserProfileFeedMapper>()
            val otherMapper = get<UserProfileFeedMapper>()

            mapper.onResponseDatabaseInsert(
                mapper.onResponseMapFrom(
                    feedWithReviews(userId, listOf(reviewPreview(874_001L, 862_011L), reviewPreview(874_002L, 862_012L))),
                ),
            )
            otherMapper.onResponseDatabaseInsert(
                otherMapper.onResponseMapFrom(
                    feedWithReviews(otherUserId, listOf(reviewPreview(875_001L, 863_011L))),
                ),
            )

            val first = database.userProfileReviewDao().entryByUserIdFlow(userId).first()
            assertEquals(listOf(874_001L, 874_002L), first.map { it.connection.reviewId })
            assertEquals(listOf(0, 1), first.map { it.connection.sortIndex })

            mapper.onResponseDatabaseInsert(
                mapper.onResponseMapFrom(
                    feedWithReviews(userId, listOf(reviewPreview(874_002L, 862_012L), reviewPreview(874_001L, 862_011L))),
                ),
            )

            val second = database.userProfileReviewDao().entryByUserIdFlow(userId).first()
            assertEquals(2, second.size)
            assertEquals(listOf(874_002L, 874_001L), second.map { it.connection.reviewId })
            assertEquals(listOf(0, 1), second.map { it.connection.sortIndex })
            val other = database.userProfileReviewDao().entryByUserIdFlow(otherUserId).first()
            assertEquals(listOf(875_001L), other.map { it.connection.reviewId })
            assertEquals(3, database.userProfileReviewDao().count())
        }

    @Test
    fun viewerRefreshReplacesOptionAndNotificationValues() =
        runTest {
            val userId = 935_012L
            val mapper = get<AuthMapper>()

            mapper.onResponseDatabaseInsert(
                mapper.onResponseMapFrom(
                    UserModelContainer.Viewer(
                        user =
                            UserModel.Viewer(
                                name = "viewer-user",
                                siteUrl = "https://anilist.co/user/viewer-user",
                                id = userId,
                                unreadNotificationCount = 8,
                                options = UserOptionsModel.Viewer(displayAdultContent = false),
                                mediaListOptions =
                                    UserModel.MediaListOptions(
                                        scoreFormat = ScoreFormat.POINT_100,
                                        rowOrder = "score",
                                        mangaList = null,
                                    ),
                            ),
                    ),
                ),
            )

            mapper.onResponseDatabaseInsert(
                mapper.onResponseMapFrom(
                    UserModelContainer.Viewer(
                        user =
                            UserModel.Viewer(
                                name = "viewer-user",
                                siteUrl = "https://anilist.co/user/viewer-user",
                                id = userId,
                                unreadNotificationCount = 3,
                                options = UserOptionsModel.Viewer(displayAdultContent = true),
                                mediaListOptions =
                                    UserModel.MediaListOptions(
                                        scoreFormat = ScoreFormat.POINT_100,
                                        rowOrder = "title",
                                        mangaList = null,
                                    ),
                            ),
                    ),
                ),
            )

            val auth = assertNotNull(database.userDao().userAuthenticated(userId).first())
            assertEquals(true, auth.generalOption.displayAdultContent)
            assertEquals("title", auth.mediaListOption.rowOrder)
            assertEquals(3, auth.notification.unreadNotifications)
        }

    @Test
    fun profileDaosReplaceBothUpsertOverloadsOnSameLogicalKey() =
        runTest {
            seedOwner(935_020L)
            val feedMapper = get<UserProfileFeedMapper>()
            feedMapper.onResponseDatabaseInsert(
                feedMapper.onResponseMapFrom(
                    feedWithReviews(935_020L, listOf(reviewPreview(877_001L, 866_001L))),
                ),
            )

            val favourite = { index: Int ->
                UserProfileFavouriteMediaEntity(
                    userId = 935_020L,
                    mediaId = 866_001L,
                    category = MediaType.ANIME,
                    sortIndex = index,
                )
            }
            database.userProfileFavouriteMediaDao().upsert(favourite(0))
            database.userProfileFavouriteMediaDao().upsert(favourite(1))
            database.userProfileFavouriteMediaDao().upsert(listOf(favourite(0)))
            database.userProfileFavouriteMediaDao().upsert(listOf(favourite(1)))
            assertEquals(1, database.userProfileFavouriteMediaDao().count())
            assertEquals(
                1,
                database
                    .userProfileFavouriteMediaDao()
                    .entryByUserIdFlow(935_020L)
                    .first()
                    .single()
                    .favourite.sortIndex,
            )

            val review = { index: Int ->
                UserProfileReviewEntity(
                    userId = 935_020L,
                    reviewId = 877_001L,
                    sortIndex = index,
                    mediaId = 866_001L,
                )
            }
            database.userProfileReviewDao().upsert(review(0))
            database.userProfileReviewDao().upsert(review(1))
            database.userProfileReviewDao().upsert(listOf(review(0)))
            database.userProfileReviewDao().upsert(listOf(review(1)))
            assertEquals(1, database.userProfileReviewDao().count())
            assertEquals(
                1,
                database
                    .userProfileReviewDao()
                    .entryByUserIdFlow(935_020L)
                    .first()
                    .single()
                    .connection.sortIndex,
            )

            val option = { rowOrder: String ->
                UserMediaOptionEntity(
                    userId = 935_020L,
                    scoreFormat = ScoreFormat.POINT_100,
                    rowOrder = rowOrder,
                    anime = UserMediaOptionEntity.MediaOption(emptyList(), emptyList(), emptyList(), false, false),
                    manga = UserMediaOptionEntity.MediaOption(emptyList(), emptyList(), emptyList(), false, false),
                )
            }
            database.userMediaOptionDao().upsert(option("score"))
            database.userMediaOptionDao().upsert(option("title"))
            database.userMediaOptionDao().upsert(listOf(option("score")))
            database.userMediaOptionDao().upsert(listOf(option("title")))
            assertEquals(1, database.userMediaOptionDao().count())
            assertEquals("title", scalar("select list_row_order from user_media_option where user_id = 935020"))
        }

    private fun scalar(sql: String): String? =
        database.openHelper.readableDatabase
            .query(sql)
            .use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }

    private suspend fun seedOwner(
        userId: Long,
        name: String = "profile-owner-$userId",
    ) {
        database.userDao().upsert(
            UserModelConverter().convertFrom(
                UserModel.Core(
                    name = name,
                    siteUrl = "https://anilist.co/user/$name",
                    id = userId,
                ),
            ),
        )
    }

    private fun mediaPayload(
        id: Long,
        title: String,
        type: MediaType = MediaType.ANIME,
    ) = MediaModel.Core(
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

    private fun mediaEdge(
        id: Long,
        title: String,
        type: MediaType = MediaType.ANIME,
    ) = UserSidecarModelContainer.MediaConnection.MediaEdge(
        favouriteOrder = 1,
        node = mediaPayload(id, title, type),
    )

    private fun overview(userId: Long) =
        UserSidecarModelContainer.Overview(
            user =
                UserSidecarModelContainer.Overview.User(
                    id = userId,
                    favourites =
                        UserSidecarModelContainer.Overview.User.Favourites(
                            anime = UserSidecarModelContainer.MediaConnection(edges = listOf(mediaEdge(861_001L, "Overview Anime"))),
                            manga = UserSidecarModelContainer.MediaConnection(edges = listOf(mediaEdge(861_002L, "Overview Manga", MediaType.MANGA))),
                        ),
                ),
            page =
                UserSidecarModelContainer.Overview.OverviewPage(
                    activities =
                        listOf(
                            UserSidecarModelContainer.ListActivityPayload(
                                id = 873_001L,
                                createdAt = 1_700_000_000L,
                                status = "watched episode",
                                progress = "1 of 12",
                                siteUrl = "https://anilist.co/activity/873001",
                                type = StatusType.ANIME_LIST,
                                media = mediaPayload(861_003L, "Overview Activity Media"),
                            ),
                        ),
                ),
        )

    private fun favEdge(
        id: Long,
        title: String,
        order: Int,
    ) = UserSidecarModelContainer.MediaConnection.MediaEdge(
        favouriteOrder = order,
        node = mediaPayload(id, title),
    )

    private fun overviewWithFavourites(
        userId: Long,
        edges: List<UserSidecarModelContainer.MediaConnection.MediaEdge>,
    ) = UserSidecarModelContainer.Overview(
        user =
            UserSidecarModelContainer.Overview.User(
                id = userId,
                favourites =
                    UserSidecarModelContainer.Overview.User.Favourites(
                        anime = UserSidecarModelContainer.MediaConnection(edges = edges),
                        manga = UserSidecarModelContainer.MediaConnection(edges = emptyList()),
                    ),
            ),
        page = UserSidecarModelContainer.Overview.OverviewPage(activities = emptyList()),
    )

    private fun reviewPreview(
        id: Long,
        mediaId: Long,
    ) = UserSidecarModelContainer.ReviewPreviewPayload(
        id = id,
        summary = "review-$id",
        score = 80,
        rating = 1,
        ratingAmount = 1,
        siteUrl = "https://anilist.co/review/$id",
        createdAt = 100L,
        updatedAt = 120L,
        mediaId = mediaId,
        mediaType = MediaType.ANIME,
        media = mediaPayload(mediaId, "Review Media $mediaId"),
    )

    private fun feedWithReviews(
        userId: Long,
        reviews: List<UserSidecarModelContainer.ReviewPreviewPayload>,
    ) = UserSidecarModelContainer.Feed(
        user = UserSidecarModelContainer.Feed.User(id = userId),
        reviewPage = UserSidecarModelContainer.Feed.ReviewPage(reviews = reviews),
        activityPage = UserSidecarModelContainer.Feed.ActivityPage(listActivity = emptyList()),
    )

    private fun feed(userId: Long) =
        UserSidecarModelContainer.Feed(
            user = UserSidecarModelContainer.Feed.User(id = userId),
            reviewPage =
                UserSidecarModelContainer.Feed.ReviewPage(
                    reviews =
                        listOf(
                            UserSidecarModelContainer.ReviewPreviewPayload(
                                id = 871_001L,
                                summary = "Sharp review",
                                score = 89,
                                rating = 12,
                                ratingAmount = 4,
                                siteUrl = "https://anilist.co/review/871001",
                                createdAt = 100L,
                                updatedAt = 120L,
                                mediaId = 861_011L,
                                mediaType = MediaType.ANIME,
                                media = mediaPayload(861_011L, "Feed Review Media"),
                            ),
                        ),
                ),
            activityPage =
                UserSidecarModelContainer.Feed.ActivityPage(
                    listActivity =
                        listOf(
                            UserSidecarModelContainer.ListActivityPayload(
                                id = 872_001L,
                                createdAt = 130L,
                                status = "watched episode",
                                progress = "5 of 12",
                                siteUrl = "https://anilist.co/activity/872001",
                                type = StatusType.ANIME_LIST,
                                media = mediaPayload(861_012L, "Feed Activity Media"),
                            ),
                        ),
                ),
        )
}
