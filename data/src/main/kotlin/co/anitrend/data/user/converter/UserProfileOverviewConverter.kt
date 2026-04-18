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

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.user.entity.sidecar.UserProfileOverviewEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.user.entity.profile.ProfileOverview

internal class UserProfileOverviewConverter(
    override val fromType: (UserProfileOverviewEntity) -> ProfileOverview = ::transform,
    override val toType: (ProfileOverview) -> UserProfileOverviewEntity = { throw NotImplementedError() },
) : SupportConverter<UserProfileOverviewEntity, ProfileOverview>() {
    private companion object : ISupportTransformer<UserProfileOverviewEntity, ProfileOverview> {
        override fun transform(source: UserProfileOverviewEntity) =
            ProfileOverview(
                animeFavourites = source.animeFavourites.map(::mediaPreview),
                mangaFavourites = source.mangaFavourites.map(::mediaPreview),
                recentActivity = source.recentActivity.map(::listActivityPreview),
            )

        private fun mediaPreview(source: UserSidecarModelContainer.MediaPreviewPayload) =
            ProfileOverview.MediaPreview(
                id = source.id,
                title =
                    MediaTitle(
                        romaji = source.title?.romaji,
                        english = source.title?.english,
                        native = source.title?.nativeTitle,
                        userPreferred = source.title?.userPreferred,
                    ),
                image =
                    MediaImage(
                        color = source.image?.color,
                        extraLarge = null,
                        large = source.image?.large,
                        medium = source.image?.medium,
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

        private fun listActivityPreview(source: UserSidecarModelContainer.ListActivityPayload) =
            ProfileOverview.ListActivityPreview(
                id = source.id,
                createdAt = source.createdAt,
                status = source.status,
                progress = source.progress,
                siteUrl = source.siteUrl,
                type = source.type,
                media = source.media?.let(::mediaPreview),
                mediaListStatus = source.mediaListStatus ?: source.media?.mediaList?.status,
                mediaListProgress = source.mediaListProgress ?: source.media?.mediaList?.progress,
                mediaListVolumeProgress = source.mediaListVolumeProgress ?: source.media?.mediaList?.progressVolumes,
            )
    }
}
