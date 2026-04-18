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

import co.anitrend.data.user.entity.sidecar.UserProfileOverviewEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer
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
    fun `convertFrom maps cached favourites and recent activity into domain sidecar`() {
        val source =
            UserProfileOverviewEntity(
                id = 42L,
                animeFavourites = listOf(mediaPreviewPayload(id = 10L, title = "Cowboy Bebop", type = MediaType.ANIME)),
                mangaFavourites = listOf(mediaPreviewPayload(id = 11L, title = "Vagabond", type = MediaType.MANGA)),
                recentActivity =
                    listOf(
                        listActivityPayload(
                            id = 101L,
                            status = "watched episode",
                            progress = "12 of 26",
                            mediaListStatus = MediaListStatus.CURRENT,
                            mediaListProgress = 12,
                        ),
                    ),
            )

        val result = UserProfileOverviewConverter().convertFrom(source)

        assertEquals(1, result.animeFavourites.size)
        assertEquals("Cowboy Bebop", result.animeFavourites.first().title.userPreferred)
        assertEquals(MediaType.MANGA, result.mangaFavourites.first().type)
        assertEquals(MediaListStatus.CURRENT, result.recentActivity.first().mediaListStatus)
        assertEquals(12, result.recentActivity.first().mediaListProgress)
    }

    @Test
    fun `convertFrom keeps partial overview payloads usable`() {
        val source =
            UserProfileOverviewEntity(
                id = 42L,
                animeFavourites = listOf(mediaPreviewPayload(id = 77L, title = null, imageUrl = null, siteUrl = null)),
                mangaFavourites = emptyList(),
                recentActivity = listOf(listActivityPayload(id = 205L, media = null, mediaListStatus = null, mediaListProgress = null)),
            )

        val result = UserProfileOverviewConverter().convertFrom(source)

        assertNull(result.animeFavourites.first().title.userPreferred)
        assertNull(result.animeFavourites.first().image.large)
        assertNull(result.animeFavourites.first().siteUrl)
        assertNull(result.recentActivity.first().media)
        assertNull(result.recentActivity.first().mediaListStatus)
    }

    private fun mediaPreviewPayload(
        id: Long,
        title: String? = "AniTrend",
        type: MediaType? = MediaType.ANIME,
        imageUrl: String? = "https://example.com/image.jpg",
        siteUrl: String? = "https://anilist.co/media/$id",
    ) =
        UserSidecarModelContainer.MediaPreviewPayload(
            id = id,
            title =
                UserSidecarModelContainer.MediaPreviewPayload.Title(
                    english = title,
                    nativeTitle = null,
                    romaji = title,
                    userPreferred = title,
                ),
            image =
                UserSidecarModelContainer.MediaPreviewPayload.Image(
                    color = "#101010",
                    large = imageUrl,
                    medium = imageUrl,
                ),
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
            mediaList =
                UserSidecarModelContainer.MediaPreviewPayload.MediaList(
                    status = MediaListStatus.CURRENT,
                    progress = 12,
                    progressVolumes = null,
                    updatedAt = 1_700_000_000L,
                ),
        )

    private fun listActivityPayload(
        id: Long,
        status: String? = "read chapter",
        progress: String? = "4 of 12",
        media: UserSidecarModelContainer.MediaPreviewPayload? = mediaPreviewPayload(id = 310L),
        mediaListStatus: MediaListStatus? = MediaListStatus.CURRENT,
        mediaListProgress: Int? = 4,
    ) =
        UserSidecarModelContainer.ListActivityPayload(
            id = id,
            createdAt = 1_700_000_100L,
            status = status,
            progress = progress,
            siteUrl = "https://anilist.co/activity/$id",
            type = StatusType.ANIME_LIST,
            media = media,
            mediaListStatus = mediaListStatus,
            mediaListProgress = mediaListProgress,
            mediaListVolumeProgress = null,
        )
}
