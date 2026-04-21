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
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.status.enums.StatusType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserProfileOverviewConverterTest {
    @Test
    fun `toProfileOverview maps flat favourite and activity entities into domain sidecar`() {
        val favourites =
            listOf(
                favouriteEntity(mediaId = 10L, title = "Cowboy Bebop", category = "ANIME", type = MediaType.ANIME),
                favouriteEntity(mediaId = 11L, title = "Vagabond", category = "MANGA", type = MediaType.MANGA),
            )
        val activities =
            listOf(
                activityEntity(
                    id = 101L,
                    status = "watched episode",
                    progress = "12 of 26",
                    mediaId = 310L,
                    mediaListStatus = MediaListStatus.CURRENT,
                    mediaListProgress = 12,
                ),
            )

        val result = UserProfileOverviewConverter.toProfileOverview(favourites, activities)

        assertEquals(1, result.animeFavourites.size)
        assertEquals("Cowboy Bebop", result.animeFavourites.first().title.userPreferred)
        assertEquals(1, result.mangaFavourites.size)
        assertEquals(MediaType.MANGA, result.mangaFavourites.first().type)
        assertEquals(MediaListStatus.CURRENT, result.recentActivity.first().mediaListStatus)
        assertEquals(12, result.recentActivity.first().mediaListProgress)
    }

    @Test
    fun `toProfileOverview maps null mediaId in activity to null media preview`() {
        val favourites =
            listOf(
                favouriteEntity(
                    mediaId = 77L,
                    title = null,
                    imageUrl = null,
                    siteUrl = null,
                    category = "ANIME",
                ),
            )
        val activities =
            listOf(
                activityEntity(id = 205L, mediaId = null, mediaListStatus = null, mediaListProgress = null),
            )

        val result = UserProfileOverviewConverter.toProfileOverview(favourites, activities)

        assertNull(result.animeFavourites.first().title.userPreferred)
        assertNull(result.animeFavourites.first().image.large)
        assertNull(result.animeFavourites.first().siteUrl)
        assertNull(result.recentActivity.first().media)
        assertNull(result.recentActivity.first().mediaListStatus)
    }

    private fun favouriteEntity(
        mediaId: Long,
        title: String? = "AniTrend",
        category: String = "ANIME",
        type: MediaType? = MediaType.ANIME,
        imageUrl: String? = "https://example.com/image.jpg",
        siteUrl: String? = "https://anilist.co/media/$mediaId",
    ) = UserProfileFavouriteMediaEntity(
        userId = 42L,
        mediaId = mediaId,
        category = category,
        sortIndex = 0,
        titleRomaji = title,
        titleEnglish = title,
        titleNative = null,
        titleUserPreferred = title,
        coverColor = "#101010",
        coverLarge = imageUrl,
        coverMedium = imageUrl,
        type = type,
        format = MediaFormat.TV,
        status = MediaStatus.FINISHED,
        episodes = 26,
        chapters = 0,
        volumes = 0,
        isFavourite = true,
        meanScore = 90,
        averageScore = 88,
        siteUrl = siteUrl,
        mediaListStatus = MediaListStatus.CURRENT,
        mediaListProgress = 12,
        mediaListVolumeProgress = null,
    )

    private fun activityEntity(
        id: Long,
        status: String? = "read chapter",
        progress: String? = "4 of 12",
        mediaId: Long? = 310L,
        mediaListStatus: MediaListStatus? = MediaListStatus.CURRENT,
        mediaListProgress: Int? = 4,
    ) = StatusEntity.ListStatus(
        id = id,
        userId = 42L,
        sortIndex = 0,
        createdAt = 1_700_000_100L,
        status = status,
        progress = progress,
        siteUrl = "https://anilist.co/activity/$id",
        type = StatusType.ANIME_LIST,
        mediaId = mediaId,
        mediaTitleRomaji = if (mediaId != null) "AniTrend" else null,
        mediaTitleEnglish = if (mediaId != null) "AniTrend" else null,
        mediaTitleNative = null,
        mediaTitleUserPreferred = if (mediaId != null) "AniTrend" else null,
        mediaCoverColor = if (mediaId != null) "#202020" else null,
        mediaCoverLarge = if (mediaId != null) "https://example.com/$mediaId.jpg" else null,
        mediaCoverMedium = if (mediaId != null) "https://example.com/$mediaId.jpg" else null,
        mediaType = if (mediaId != null) MediaType.MANGA else null,
        mediaFormat = if (mediaId != null) MediaFormat.MANGA else null,
        mediaStatus = if (mediaId != null) MediaStatus.RELEASING else null,
        mediaEpisodes = null,
        mediaChapters = if (mediaId != null) 12 else null,
        mediaVolumes = null,
        mediaIsFavourite = if (mediaId != null) false else null,
        mediaMeanScore = if (mediaId != null) 85 else null,
        mediaAverageScore = if (mediaId != null) 83 else null,
        mediaSiteUrl = if (mediaId != null) "https://anilist.co/media/$mediaId" else null,
        mediaListStatus = mediaListStatus,
        mediaListProgress = mediaListProgress,
        mediaListVolumeProgress = null,
    )
}
