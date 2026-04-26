/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.user.converter

import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.status.entity.view.ListStatusEntityView
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import co.anitrend.data.user.entity.view.UserProfileReviewEntityView
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.domain.status.enums.StatusType
import kotlin.test.Test
import kotlin.test.assertEquals

class UserProfileFeedConverterTest {
    @Test
    fun `toProfileFeed maps review and activity relation views`() {
        val result =
            UserProfileFeedConverter.toProfileFeed(
                reviews = listOf(reviewView(reviewId = 1L, mediaId = 44L, title = "Monster")),
                activities = listOf(activityView(id = 10L, mediaId = 55L, title = "Mob Psycho 100")),
            )

        assertEquals(1, result.reviews.size)
        assertEquals("Monster", result.reviews.first().media?.title?.userPreferred)
        assertEquals("Mob Psycho 100", result.listActivity.first().media?.title?.userPreferred)
    }

    private fun reviewView(reviewId: Long, mediaId: Long, title: String) =
        UserProfileReviewEntityView(
            connection = UserProfileReviewEntity(userId = 9L, reviewId = reviewId, sortIndex = 0, mediaId = mediaId),
            review =
                ReviewEntity(
                    body = "",
                    createdAt = 1_700_000_000L,
                    mediaId = mediaId,
                    isPrivate = false,
                    rating = 120,
                    ratingAmount = 30,
                    score = 90,
                    siteUrl = "https://anilist.co/review/$reviewId",
                    summary = "Tight review",
                    updatedAt = 1_700_000_500L,
                    userId = 9L,
                    userRating = ReviewRating.NO_VOTE,
                    id = reviewId,
                ),
            media = mediaEntity(mediaId, title),
        )

    private fun activityView(id: Long, mediaId: Long, title: String) =
        ListStatusEntityView(
            activity =
                StatusEntity.ListStatus(
                    id = id,
                    userId = 9L,
                    sortIndex = 0,
                    createdAt = 1_700_000_200L,
                    status = "completed",
                    progress = "26 of 26",
                    siteUrl = "https://anilist.co/activity/$id",
                    type = StatusType.ANIME_LIST,
                    mediaId = mediaId,
                ),
            media = mediaEntity(mediaId, title),
        )

    private fun mediaEntity(mediaId: Long, title: String) =
        MediaEntity(
            coverImage = MediaEntity.CoverImage(color = null, extraLarge = null, large = null, medium = null, banner = null),
            title = MediaEntity.Title(romaji = title, english = title, original = null, userPreferred = title),
            trailer = null,
            nextAiringId = null,
            averageScore = 80,
            chapters = null,
            countryOfOrigin = null,
            description = null,
            duration = null,
            endDate = null,
            episodes = 24,
            favourites = 1,
            format = MediaFormat.TV,
            hashTag = null,
            isAdult = null,
            isFavourite = false,
            isFavouriteBlocked = false,
            isLicensed = null,
            isRecommendationBlocked = false,
            isReviewBlocked = false,
            isLocked = null,
            meanScore = 82,
            popularity = null,
            season = null,
            siteUrl = "https://anilist.co/media/$mediaId",
            source = null,
            startDate = null,
            status = MediaStatus.FINISHED,
            synonyms = emptyList(),
            trending = null,
            type = MediaType.ANIME,
            updatedAt = null,
            volumes = null,
            malId = null,
            id = mediaId,
        )
}
