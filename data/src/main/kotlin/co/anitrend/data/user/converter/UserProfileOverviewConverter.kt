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
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.user.entity.profile.ProfileOverview

internal object UserProfileOverviewConverter {

    fun toProfileOverview(
        favourites: List<UserProfileFavouriteMediaEntity>,
        activities: List<StatusEntity.ListStatus>,
    ): ProfileOverview =
        ProfileOverview(
            animeFavourites = favourites.filter { it.category == "ANIME" }.map { mediaPreview(it) },
            mangaFavourites = favourites.filter { it.category == "MANGA" }.map { mediaPreview(it) },
            recentActivity = activities.map { listActivityPreview(it) },
        )

    internal fun mediaPreview(source: UserProfileFavouriteMediaEntity): ProfileOverview.MediaPreview =
        ProfileOverview.MediaPreview(
            id = source.mediaId,
            title =
                MediaTitle(
                    romaji = source.titleRomaji,
                    english = source.titleEnglish,
                    native = source.titleNative,
                    userPreferred = source.titleUserPreferred,
                ),
            image =
                MediaImage(
                    color = source.coverColor,
                    extraLarge = null,
                    large = source.coverLarge,
                    medium = source.coverMedium,
                    banner = null,
                ),
            type = source.type,
            format = source.format,
            status = source.status,
            episodes = source.episodes ?: 0,
            chapters = source.chapters ?: 0,
            volumes = source.volumes ?: 0,
            isFavourite = source.isFavourite ?: false,
            meanScore = source.meanScore ?: 0,
            averageScore = source.averageScore ?: 0,
            siteUrl = source.siteUrl,
        )

    internal fun activityMediaPreview(source: StatusEntity.ListStatus): ProfileOverview.MediaPreview? {
        val mediaId = source.mediaId ?: return null
        return ProfileOverview.MediaPreview(
            id = mediaId,
            title =
                MediaTitle(
                    romaji = source.mediaTitleRomaji,
                    english = source.mediaTitleEnglish,
                    native = source.mediaTitleNative,
                    userPreferred = source.mediaTitleUserPreferred,
                ),
            image =
                MediaImage(
                    color = source.mediaCoverColor,
                    extraLarge = null,
                    large = source.mediaCoverLarge,
                    medium = source.mediaCoverMedium,
                    banner = null,
                ),
            type = source.mediaType,
            format = source.mediaFormat,
            status = source.mediaStatus,
            episodes = source.mediaEpisodes ?: 0,
            chapters = source.mediaChapters ?: 0,
            volumes = source.mediaVolumes ?: 0,
            isFavourite = source.mediaIsFavourite ?: false,
            meanScore = source.mediaMeanScore ?: 0,
            averageScore = source.mediaAverageScore ?: 0,
            siteUrl = source.mediaSiteUrl,
        )
    }

    internal fun listActivityPreview(source: StatusEntity.ListStatus): ProfileOverview.ListActivityPreview =
        ProfileOverview.ListActivityPreview(
            id = source.id,
            createdAt = source.createdAt,
            status = source.status,
            progress = source.progress,
            siteUrl = source.siteUrl,
            type = source.type,
            media = activityMediaPreview(source),
            mediaListStatus = source.mediaListStatus,
            mediaListProgress = source.mediaListProgress,
            mediaListVolumeProgress = source.mediaListVolumeProgress,
        )
}
