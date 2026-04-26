/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.user.converter

import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.status.entity.view.ListStatusEntityView
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.data.user.entity.view.UserProfileFavouriteMediaEntityView
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.status.enums.StatusType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserProfileOverviewConverterTest {
    @Test
    fun `toProfileOverview maps favourite and activity views`() {
        val result =
            UserProfileOverviewConverter.toProfileOverview(
                favourites =
                    listOf(
                        favouriteView(mediaId = 10L, title = "Cowboy Bebop", category = MediaType.ANIME),
                        favouriteView(mediaId = 11L, title = "Vagabond", category = MediaType.MANGA),
                    ),
                activities = listOf(activityView(id = 101L, mediaId = 310L, title = "AniTrend")),
            )

        assertEquals(1, result.animeFavourites.size)
        assertEquals("Cowboy Bebop", result.animeFavourites.first().title.userPreferred)
        assertEquals(1, result.mangaFavourites.size)
        assertEquals(MediaType.MANGA, result.mangaFavourites.first().type)
        assertEquals("AniTrend", result.recentActivity.first().media?.title?.userPreferred)
        assertNull(result.recentActivity.first().mediaListStatus)
    }

    @Test
    fun `toProfileOverview returns null activity media when relation is missing`() {
        val result =
            UserProfileOverviewConverter.toProfileOverview(
                favourites = listOf(favouriteView(mediaId = 77L, title = null, category = MediaType.ANIME)),
                activities = listOf(activityView(id = 205L, mediaId = null, title = null)),
            )

        assertNull(result.animeFavourites.first().title.userPreferred)
        assertNull(result.recentActivity.first().media)
    }

    private fun favouriteView(mediaId: Long, title: String?, category: MediaType) =
        UserProfileFavouriteMediaEntityView(
            favourite =
                UserProfileFavouriteMediaEntity(
                    userId = 42L,
                    mediaId = mediaId,
                    category = category,
                    sortIndex = 0,
                ),
            media = mediaEntity(mediaId = mediaId, title = title, type = category),
        )

    private fun activityView(id: Long, mediaId: Long?, title: String?) =
        ListStatusEntityView(
            activity =
                StatusEntity.ListStatus(
                    id = id,
                    userId = 42L,
                    sortIndex = 0,
                    createdAt = 1_700_000_100L,
                    status = "watched episode",
                    progress = "12 of 26",
                    siteUrl = "https://anilist.co/activity/$id",
                    type = StatusType.ANIME_LIST,
                    mediaId = mediaId,
                ),
            media = mediaId?.let { mediaEntity(mediaId = it, title = title, type = MediaType.ANIME) },
        )

    private fun mediaEntity(mediaId: Long, title: String?, type: MediaType) =
        MediaEntity(
            coverImage = MediaEntity.CoverImage(color = null, extraLarge = null, large = null, medium = null, banner = null),
            title = MediaEntity.Title(romaji = title, english = title, original = null, userPreferred = title),
            trailer = null,
            nextAiringId = null,
            averageScore = 88,
            chapters = 0,
            countryOfOrigin = null,
            description = null,
            duration = null,
            endDate = null,
            episodes = 26,
            favourites = 1,
            format = MediaFormat.TV,
            hashTag = null,
            isAdult = null,
            isFavourite = true,
            isFavouriteBlocked = false,
            isLicensed = null,
            isRecommendationBlocked = false,
            isReviewBlocked = false,
            isLocked = null,
            meanScore = 90,
            popularity = null,
            season = null,
            siteUrl = if (title != null) "https://anilist.co/media/$mediaId" else null,
            source = null,
            startDate = null,
            status = MediaStatus.FINISHED,
            synonyms = emptyList(),
            trending = null,
            type = type,
            updatedAt = null,
            volumes = 0,
            malId = null,
            id = mediaId,
        )
}
