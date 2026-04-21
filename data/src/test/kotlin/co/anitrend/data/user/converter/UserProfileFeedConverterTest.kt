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
package co.anitrend.data.user.converter

import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.status.enums.StatusType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserProfileFeedConverterTest {
    @Test
    fun `toProfileFeed maps review and list activity previews`() {
        val reviews =
            listOf(
                reviewEntity(
                    id = 1L,
                    summary = "Tight, character-driven review",
                    score = 90,
                    rating = 120,
                    ratingAmount = 30,
                    mediaId = 44L,
                    mediaType = MediaType.ANIME,
                    mediaTitleUserPreferred = "Monster",
                ),
            )
        val activities =
            listOf(
                activityEntity(
                    id = 10L,
                    status = "completed",
                    progress = "26 of 26",
                    mediaId = 55L,
                    mediaTitleUserPreferred = "Mob Psycho 100",
                    mediaListStatus = MediaListStatus.COMPLETED,
                    mediaListProgress = 26,
                ),
            )

        val result = UserProfileFeedConverter.toProfileFeed(reviews, activities)

        assertEquals(1, result.reviews.size)
        assertEquals("Monster", result.reviews.first().media?.title?.userPreferred)
        assertEquals(MediaListStatus.COMPLETED, result.listActivity.first().mediaListStatus)
        assertEquals(26, result.listActivity.first().mediaListProgress)
    }

    @Test
    fun `toProfileFeed tolerates zero mediaId on review (maps to null media preview)`() {
        val reviews =
            listOf(
                reviewEntity(
                    id = 2L,
                    summary = "Minimal snapshot",
                    score = 70,
                    rating = 0,
                    ratingAmount = 0,
                    mediaId = 0L,
                    mediaType = null,
                    mediaTitleUserPreferred = null,
                ),
            )

        val result = UserProfileFeedConverter.toProfileFeed(reviews, emptyList())

        assertNull(result.reviews.first().media)
        assertEquals(0, result.listActivity.size)
    }

    private fun reviewEntity(
        id: Long,
        summary: String = "A review",
        score: Int = 80,
        rating: Int = 10,
        ratingAmount: Int = 5,
        mediaId: Long = 1L,
        mediaType: MediaType? = MediaType.ANIME,
        mediaTitleUserPreferred: String? = "AniTrend",
    ) = UserProfileReviewEntity(
        userId = 9L,
        reviewId = id,
        sortIndex = 0,
        summary = summary,
        score = score,
        rating = rating,
        ratingAmount = ratingAmount,
        siteUrl = "https://anilist.co/review/$id",
        createdAt = 1_700_000_000L,
        updatedAt = 1_700_000_500L,
        mediaId = mediaId,
        mediaType = mediaType,
        mediaTitleRomaji = mediaTitleUserPreferred,
        mediaTitleEnglish = mediaTitleUserPreferred,
        mediaTitleNative = null,
        mediaTitleUserPreferred = mediaTitleUserPreferred,
        mediaCoverColor = null,
        mediaCoverLarge = if (mediaId != 0L) "https://example.com/$mediaId.jpg" else null,
        mediaCoverMedium = if (mediaId != 0L) "https://example.com/$mediaId.jpg" else null,
        mediaEntityType = mediaType,
        mediaFormat = if (mediaId != 0L) MediaFormat.TV else null,
        mediaStatus = if (mediaId != 0L) MediaStatus.FINISHED else null,
        mediaEpisodes = if (mediaId != 0L) 24 else null,
        mediaChapters = null,
        mediaVolumes = null,
        mediaIsFavourite = if (mediaId != 0L) false else null,
        mediaMeanScore = if (mediaId != 0L) 82 else null,
        mediaAverageScore = if (mediaId != 0L) 80 else null,
        mediaSiteUrl = if (mediaId != 0L) "https://anilist.co/media/$mediaId" else null,
        mediaListStatus = null,
        mediaListProgress = null,
        mediaListVolumeProgress = null,
    )

    private fun activityEntity(
        id: Long,
        status: String? = "completed",
        progress: String? = "26 of 26",
        mediaId: Long? = 55L,
        mediaTitleUserPreferred: String? = null,
        mediaListStatus: MediaListStatus? = null,
        mediaListProgress: Int? = null,
    ) = StatusEntity.ListStatus(
        id = id,
        userId = 9L,
        sortIndex = 0,
        createdAt = 1_700_000_200L,
        status = status,
        progress = progress,
        siteUrl = "https://anilist.co/activity/$id",
        type = StatusType.ANIME_LIST,
        mediaId = mediaId,
        mediaTitleRomaji = mediaTitleUserPreferred,
        mediaTitleEnglish = mediaTitleUserPreferred,
        mediaTitleNative = null,
        mediaTitleUserPreferred = mediaTitleUserPreferred,
        mediaCoverColor = null,
        mediaCoverLarge = if (mediaId != null) "https://example.com/$mediaId.jpg" else null,
        mediaCoverMedium = if (mediaId != null) "https://example.com/$mediaId.jpg" else null,
        mediaType = if (mediaId != null) MediaType.ANIME else null,
        mediaFormat = if (mediaId != null) MediaFormat.TV else null,
        mediaStatus = if (mediaId != null) MediaStatus.RELEASING else null,
        mediaEpisodes = if (mediaId != null) 12 else null,
        mediaChapters = null,
        mediaVolumes = null,
        mediaIsFavourite = if (mediaId != null) false else null,
        mediaMeanScore = if (mediaId != null) 84 else null,
        mediaAverageScore = if (mediaId != null) 83 else null,
        mediaSiteUrl = if (mediaId != null) "https://anilist.co/media/$mediaId" else null,
        mediaListStatus = mediaListStatus,
        mediaListProgress = mediaListProgress,
        mediaListVolumeProgress = null,
    )
}
