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

import co.anitrend.data.user.entity.sidecar.UserProfileFeedEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserProfileFeedConverterTest {
    @Test
    fun `convertFrom maps review and list activity previews`() {
        val source =
            UserProfileFeedEntity(
                id = 9L,
                reviews =
                    listOf(
                        UserSidecarModelContainer.ReviewPreviewPayload(
                            id = 1L,
                            summary = "Tight, character-driven review",
                            score = 90,
                            rating = 120,
                            ratingAmount = 30,
                            siteUrl = "https://anilist.co/review/1",
                            createdAt = 1_700_000_000L,
                            updatedAt = 1_700_000_500L,
                            mediaId = 44L,
                            mediaType = MediaType.ANIME,
                            media = overviewMediaPayload(44L, "Monster"),
                        ),
                    ),
                listActivity =
                    listOf(
                        UserSidecarModelContainer.ListActivityPayload(
                            id = 10L,
                            createdAt = 1_700_000_200L,
                            status = "completed",
                            progress = "26 of 26",
                            siteUrl = "https://anilist.co/activity/10",
                            type = co.anitrend.domain.status.enums.StatusType.ANIME_LIST,
                            media = overviewMediaPayload(55L, "Mob Psycho 100"),
                            mediaListStatus = MediaListStatus.COMPLETED,
                            mediaListProgress = 26,
                            mediaListVolumeProgress = null,
                        ),
                    ),
            )

        val result = UserProfileFeedConverter().convertFrom(source)

        assertEquals(1, result.reviews.size)
        assertEquals("Monster", result.reviews.first().media?.title?.userPreferred)
        assertEquals(MediaListStatus.COMPLETED, result.listActivity.first().mediaListStatus)
        assertEquals(26, result.listActivity.first().mediaListProgress)
    }

    @Test
    fun `convertFrom tolerates empty media snapshots`() {
        val source =
            UserProfileFeedEntity(
                id = 9L,
                reviews =
                    listOf(
                        UserSidecarModelContainer.ReviewPreviewPayload(
                            id = 2L,
                            summary = "Minimal snapshot",
                            score = 70,
                            rating = 0,
                            ratingAmount = 0,
                            siteUrl = "https://anilist.co/review/2",
                            createdAt = 1L,
                            updatedAt = 2L,
                            mediaId = 0L,
                            mediaType = null,
                            media = null,
                        ),
                    ),
                listActivity = emptyList(),
            )

        val result = UserProfileFeedConverter().convertFrom(source)

        assertNull(result.reviews.first().media)
        assertEquals(0, result.listActivity.size)
    }

    private fun overviewMediaPayload(id: Long, title: String) =
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
                    color = null,
                    large = "https://example.com/$id.jpg",
                    medium = "https://example.com/$id.jpg",
                ),
            type = MediaType.ANIME,
            format = co.anitrend.domain.media.enums.MediaFormat.TV,
            status = co.anitrend.domain.media.enums.MediaStatus.FINISHED,
            episodes = 24,
            chapters = 0,
            volumes = 0,
            isFavourite = false,
            meanScore = 82,
            averageScore = 80,
            siteUrl = "https://anilist.co/media/$id",
            mediaList = null,
        )
}
