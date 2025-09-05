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

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.datasource.local.IEdgeMediaStore
import co.anitrend.data.edge.media.datasource.remote.EdgeMediaRemoteSource
import co.anitrend.data.edge.media.mapper.EdgeMediaMapper
import co.anitrend.data.edge.media.mapper.EdgeMediaModelConverter
import co.anitrend.data.edge.media.mapper.EdgeMediaEntityConverter
import co.anitrend.data.edge.media.repository.EdgeMediaRepository
import co.anitrend.data.edge.media.source.EdgeMediaSourceImpl
import co.anitrend.data.edge.media.source.contract.EdgeMediaSource
import co.anitrend.data.edge.media.EdgeMediaController
import org.koin.dsl.bind
import org.koin.dsl.module

internal val edgeMediaModules =
    module {
        single { get<IEdgeMediaStore>().edgeMediaDao() } bind EdgeMediaLocalSource::class
        single<EdgeMediaRemoteSource> { get() }

        factory { EdgeMediaModelConverter() }
        factory { EdgeMediaEntityConverter() }
        factory {
            EdgeMediaMapper(
                localSource = get(),
                converter = get(),
            )
        }
        factory<EdgeMediaController> {
            graphQLController(
                mapper = get<EdgeMediaMapper>(),
            )
        }

        factory<EdgeMediaSource> {
            EdgeMediaSourceImpl(
                remoteSource = get(),
                localSource = get(),
                controller = get(),
                converter = get(),
                clearDataHelper = get(),
                dispatcher = get(),
                cachePolicy = get(),
            )
        }

        factory { EdgeMediaRepository(source = get()) }
    }
