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
package co.anitrend.domain.user.entity.profile

import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.status.enums.StatusType

data class ProfileOverview(
    val animeFavourites: List<MediaPreview>,
    val mangaFavourites: List<MediaPreview>,
    val recentActivity: List<ListActivityPreview>,
) {
    data class MediaPreview(
        val id: Long,
        val title: MediaTitle,
        val image: MediaImage,
        val type: MediaType?,
        val format: MediaFormat?,
        val status: MediaStatus?,
        val episodes: Int,
        val chapters: Int,
        val volumes: Int,
        val isFavourite: Boolean,
        val meanScore: Int,
        val averageScore: Int,
        val siteUrl: String?,
    )

    data class ListActivityPreview(
        val id: Long,
        val createdAt: Long,
        val status: CharSequence?,
        val progress: CharSequence?,
        val siteUrl: String?,
        val type: StatusType?,
        val media: MediaPreview?,
        val mediaListStatus: MediaListStatus?,
        val mediaListProgress: Int?,
        val mediaListVolumeProgress: Int?,
    )
}
