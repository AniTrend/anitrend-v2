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
package co.anitrend.data.edge.media.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.edge.core.extensions.aniTrendApi
import co.anitrend.data.edge.core.extensions.edgeStore
import co.anitrend.data.edge.media.cache.EdgeMediaCache
import co.anitrend.data.edge.media.converters.EdgeMediaModelConverter
import co.anitrend.data.edge.media.mapper.EdgeMediaMapper
import co.anitrend.data.edge.media.source.EdgeMediaSourceImpl
import co.anitrend.data.edge.media.source.contract.EdgeMediaSource
import org.koin.dsl.module

internal val edgeMediaModules =
    module {
        factory { EdgeMediaModelConverter() }
        factory {
            EdgeMediaCache(
                localSource = cacheLocalSource()
            )
        }
        factory {
            EdgeMediaMapper(
                localSource = edgeStore().edgeMediaDao(),
                converter = get(),
                imageMapper = get(),
                networkMapper = get(),
                trailerMapper = get(),
                themeMapper = get(),
                seasonMapper = get(),
                episodeMapper = get(),
            )
        }

        factory<EdgeMediaSource> {
            EdgeMediaSourceImpl(
                remoteSource = aniTrendApi(),
                localSource = edgeStore().edgeMediaDao(),
                controller = graphQLController(
                    mapper = get<EdgeMediaMapper>(),
                    strategy = offline(),
                ),
                clearDataHelper = get(),
                dispatcher = get(),
                cachePolicy = get<EdgeMediaCache>(),
            )
        }
    }
