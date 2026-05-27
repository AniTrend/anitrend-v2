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
package co.anitrend.data.studio.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.studio.entity.Studio

internal class MediaStudioConnectionEntityConverter(
    override val fromType: (MediaStudioConnectionEntity) -> MediaStudioEntry = ::transform,
    override val toType: (MediaStudioEntry) -> MediaStudioConnectionEntity = { throw NotImplementedError() },
) : SupportConverter<MediaStudioConnectionEntity, MediaStudioEntry>() {
    private companion object : ISupportTransformer<MediaStudioConnectionEntity, MediaStudioEntry> {
        override fun transform(source: MediaStudioConnectionEntity) =
            MediaStudioEntry(
                studio =
                    Studio.Core(
                        favourites = source.studioFavourites ?: 0,
                        isFavourite = false,
                        isFavouriteBlocked = false,
                        image = null,
                        isAnimationStudio = source.studioIsAnimationStudio ?: false,
                        name = source.studioName,
                        siteUrl = source.studioSiteUrl,
                        id = source.studioId,
                    ),
                mediaTitle = source.mediaTitle ?: "",
                mediaCoverImage =
                    if (source.mediaCoverImageLarge != null || source.mediaCoverImageMedium != null) {
                        CoverImage(
                            large = source.mediaCoverImageLarge,
                            medium = source.mediaCoverImageMedium,
                        )
                    } else {
                        null
                    },
                mediaFormat = source.mediaFormat?.let { runCatching { MediaFormat.valueOf(it) }.getOrNull() },
                mediaStartYear = source.mediaStartYear,
                mediaAverageScore = source.mediaAverageScore,
                isMain = source.isMain,
                id = source.entryId,
            )
    }
}
