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

import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.status.entity.view.ListStatusEntityView
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.entity.view.UserProfileFavouriteMediaEntityView
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.user.entity.profile.ProfileOverview

internal object UserProfileOverviewConverter {
    fun toProfileOverview(
        favourites: List<UserProfileFavouriteMediaEntityView>,
        activities: List<ListStatusEntityView>,
    ): ProfileOverview =
        ProfileOverview(
            animeFavourites = favourites.filter { it.favourite.category == MediaType.ANIME }.map { mediaPreview(it.media) },
            mangaFavourites = favourites.filter { it.favourite.category == MediaType.MANGA }.map { mediaPreview(it.media) },
            recentActivity = activities.map { listActivityPreview(it) },
        )

    internal fun mediaPreview(source: MediaEntity): ProfileOverview.MediaPreview =
        ProfileOverview.MediaPreview(
            id = source.id,
            title =
                MediaTitle(
                    romaji = source.title.romaji,
                    english = source.title.english,
                    native = source.title.original,
                    userPreferred = source.title.userPreferred,
                ),
            image =
                MediaImage(
                    color = source.coverImage.color,
                    extraLarge = source.coverImage.extraLarge,
                    large = source.coverImage.large,
                    medium = source.coverImage.medium,
                    banner = source.coverImage.banner,
                ),
            type = source.type,
            format = source.format,
            status = source.status,
            episodes = source.episodes ?: 0,
            chapters = source.chapters ?: 0,
            volumes = source.volumes ?: 0,
            isFavourite = source.isFavourite,
            meanScore = source.meanScore ?: 0,
            averageScore = source.averageScore ?: 0,
            siteUrl = source.siteUrl,
        )

    internal fun activityMediaPreview(source: ListStatusEntityView): ProfileOverview.MediaPreview? = source.media?.let(::mediaPreview)

    internal fun listActivityPreview(source: ListStatusEntityView): ProfileOverview.ListActivityPreview =
        ProfileOverview.ListActivityPreview(
            id = source.activity.id,
            createdAt = source.activity.createdAt,
            status = source.activity.status,
            progress = source.activity.progress,
            siteUrl = source.activity.siteUrl,
            type = source.activity.type,
            media = activityMediaPreview(source),
            mediaListStatus = null,
            mediaListProgress = null,
            mediaListVolumeProgress = null,
        )
}
