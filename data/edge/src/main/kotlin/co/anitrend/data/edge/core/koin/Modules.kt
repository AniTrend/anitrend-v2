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
package co.anitrend.data.edge.core.koin

import co.anitrend.data.edge.config.koin.edgeConfigModules
import co.anitrend.data.edge.core.api.factory.EdgeApiFactory
import co.anitrend.data.edge.episode.koin.edgeEpisodeModules
import co.anitrend.data.edge.genre.koin.edgeGenreModule
import co.anitrend.data.edge.image.koin.edgeImageModule
import co.anitrend.data.edge.media.koin.edgeMediaModules
import co.anitrend.data.edge.navigation.koin.edgeNavigationModule
import co.anitrend.data.edge.network.koin.edgeNetworkModule
import co.anitrend.data.edge.news.koin.edgeNewsModules
import co.anitrend.data.edge.season.koin.edgeSeasonModules
import co.anitrend.data.edge.theme.koin.edgeThemeModule
import co.anitrend.data.edge.trailer.koin.edgeTrailerModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val coreModule =
    module {
        singleOf(::EdgeApiFactory)
    }

val edgeModules =
    module {
        includes(
            coreModule,
            edgeConfigModules,
            edgeNavigationModule,
            edgeGenreModule,
            edgeNewsModules,
            edgeImageModule,
            edgeNetworkModule,
            edgeTrailerModule,
            edgeThemeModule,
            edgeSeasonModules,
            edgeEpisodeModules,
            edgeMediaModules,
        )
    }
