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
package co.anitrend.data.studio.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.model.remote.StudioPagedContainer

internal class StudioPagedMapper(
    private val localSource: StudioLocalSource,
) : DefaultMapper<StudioPagedContainer, List<StudioEntity>>() {
    override suspend fun persist(data: List<StudioEntity>) {
        localSource.upsert(data)
    }

    override suspend fun onResponseMapFrom(source: StudioPagedContainer): List<StudioEntity> =
        source.page.studios.map { studio ->
            StudioEntity(
                favourites = studio.favourites ?: 0,
                isAnimationStudio = studio.isAnimationStudio,
                isFavourite = studio.isFavourite,
                isFavouriteBlocked = studio.isFavouriteBlocked ?: false,
                name = studio.name,
                siteUrl = studio.siteUrl,
                id = studio.id,
            )
        }
}
