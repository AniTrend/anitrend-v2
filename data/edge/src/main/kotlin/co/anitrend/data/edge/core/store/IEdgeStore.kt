/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.core.store

import co.anitrend.data.edge.config.datasource.local.IEdgeConfigStore
import co.anitrend.data.edge.episode.datasource.IEdgeEpisodeStore
import co.anitrend.data.edge.genre.datasource.IEdgeGenreStore
import co.anitrend.data.edge.image.datasource.IEdgeImageStore
import co.anitrend.data.edge.media.datasource.local.IEdgeMediaStore
import co.anitrend.data.edge.navigation.datasource.IEdgeNavigationStore
import co.anitrend.data.edge.network.datasource.IEdgeNetworkStore
import co.anitrend.data.edge.news.datasource.IEdgeNewsStore
import co.anitrend.data.edge.season.datasource.IEdgeSeasonStore
import co.anitrend.data.edge.theme.datasource.IEdgeThemeStore
import co.anitrend.data.edge.trailer.datasource.IEdgeTrailerStore

/**
 * Aggregated store interface for edge slices.
 * Implemented by the Room database wrapper exposing DAOs for media and related slices.
 */
interface IEdgeStore :
    IEdgeConfigStore,
    IEdgeGenreStore,
    IEdgeNavigationStore,
    IEdgeImageStore,
    IEdgeNewsStore,
    IEdgeNetworkStore,
    IEdgeMediaStore,
    IEdgeSeasonStore,
    IEdgeEpisodeStore,
    IEdgeTrailerStore,
    IEdgeThemeStore
