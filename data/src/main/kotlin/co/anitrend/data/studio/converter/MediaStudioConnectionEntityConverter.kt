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
import co.anitrend.domain.media.entity.MediaStudioEntry
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
                        favourites = 0,
                        isFavourite = false,
                        isFavouriteBlocked = false,
                        image = null,
                        isAnimationStudio = false,
                        name = source.studioName,
                        siteUrl = null,
                        id = source.studioId,
                    ),
                isMain = source.isMain,
                id = source.entryId,
            )
    }
}
