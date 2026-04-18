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
package co.anitrend.data.user.model.container

import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.status.enums.StatusType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class UserSidecarModelContainer {
    @Serializable
    data class Overview(
        @SerialName("User") val user: User? = null,
        @SerialName("Page") val page: OverviewPage? = null,
    ) : UserSidecarModelContainer() {
        @Serializable
        data class User(
            @SerialName("id") val id: Long? = null,
            @SerialName("favourites") val favourites: Favourites? = null,
        ) {
            @Serializable
            data class Favourites(
                @SerialName("anime") val anime: MediaConnection? = null,
                @SerialName("manga") val manga: MediaConnection? = null,
            )
        }

        @Serializable
        data class OverviewPage(
            @SerialName("activities") val activities: List<ListActivityPayload> = emptyList(),
        )
    }

    @Serializable
    data class Feed(
        @SerialName("User") val user: User? = null,
        @SerialName("reviewPage") val reviewPage: ReviewPage? = null,
        @SerialName("activityPage") val activityPage: ActivityPage? = null,
    ) : UserSidecarModelContainer() {
        @Serializable
        data class User(
            @SerialName("id") val id: Long? = null,
        )

        @Serializable
        data class ReviewPage(
            @SerialName("reviews") val reviews: List<ReviewPreviewPayload> = emptyList(),
        )

        @Serializable
        data class ActivityPage(
            @SerialName("listActivity") val listActivity: List<ListActivityPayload> = emptyList(),
        )
    }

    @Serializable
    data class MediaConnection(
        @SerialName("edges") val edges: List<MediaEdge> = emptyList(),
    ) {
        @Serializable
        data class MediaEdge(
            @SerialName("favouriteOrder") val favouriteOrder: Int? = null,
            @SerialName("node") val node: MediaPreviewPayload? = null,
        )
    }

    @Serializable
    data class MediaPreviewPayload(
        @SerialName("id") val id: Long,
        @SerialName("title") val title: Title? = null,
        @SerialName("coverImage") val image: Image? = null,
        @SerialName("type") val type: MediaType? = null,
        @SerialName("format") val format: MediaFormat? = null,
        @SerialName("status") val status: MediaStatus? = null,
        @SerialName("episodes") val episodes: Int? = null,
        @SerialName("chapters") val chapters: Int? = null,
        @SerialName("volumes") val volumes: Int? = null,
        @SerialName("isFavourite") val isFavourite: Boolean? = null,
        @SerialName("meanScore") val meanScore: Int? = null,
        @SerialName("averageScore") val averageScore: Int? = null,
        @SerialName("siteUrl") val siteUrl: String? = null,
        @SerialName("mediaListEntry") val mediaList: MediaList? = null,
    ) {
        @Serializable
        data class Title(
            @SerialName("english") val english: String? = null,
            @SerialName("native") val nativeTitle: String? = null,
            @SerialName("romaji") val romaji: String? = null,
            @SerialName("userPreferred") val userPreferred: String? = null,
        )

        @Serializable
        data class Image(
            @SerialName("color") val color: String? = null,
            @SerialName("large") val large: String? = null,
            @SerialName("medium") val medium: String? = null,
        )

        @Serializable
        data class MediaList(
            @SerialName("status") val status: MediaListStatus? = null,
            @SerialName("progress") val progress: Int? = null,
            @SerialName("progressVolumes") val progressVolumes: Int? = null,
            @SerialName("updatedAt") val updatedAt: Long? = null,
        )
    }

    @Serializable
    data class ListActivityPayload(
        @SerialName("id") val id: Long,
        @SerialName("createdAt") val createdAt: Long,
        @SerialName("status") val status: String? = null,
        @SerialName("progress") val progress: String? = null,
        @SerialName("siteUrl") val siteUrl: String? = null,
        @SerialName("type") val type: StatusType? = null,
        @SerialName("media") val media: MediaPreviewPayload? = null,
        @SerialName("mediaListStatus") val mediaListStatus: MediaListStatus? = null,
        @SerialName("mediaListProgress") val mediaListProgress: Int? = null,
        @SerialName("mediaListVolumeProgress") val mediaListVolumeProgress: Int? = null,
    )

    @Serializable
    data class ReviewPreviewPayload(
        @SerialName("id") val id: Long,
        @SerialName("summary") val summary: String? = null,
        @SerialName("score") val score: Int? = null,
        @SerialName("rating") val rating: Int? = null,
        @SerialName("ratingAmount") val ratingAmount: Int? = null,
        @SerialName("siteUrl") val siteUrl: String? = null,
        @SerialName("createdAt") val createdAt: Long,
        @SerialName("updatedAt") val updatedAt: Long,
        @SerialName("mediaId") val mediaId: Long,
        @SerialName("mediaType") val mediaType: MediaType? = null,
        @SerialName("media") val media: MediaPreviewPayload? = null,
    )
}
