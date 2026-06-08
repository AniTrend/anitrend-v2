/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.studio.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.studio.GetSearchStudioInteractor
import co.anitrend.data.studio.StudioDetailInteractor
import co.anitrend.data.studio.StudioDetailRepository
import co.anitrend.data.studio.StudioSearchRepository
import co.anitrend.data.studio.cache.StudioCache
import co.anitrend.data.studio.converter.MediaStudioConnectionEntityConverter
import co.anitrend.data.studio.converter.MediaStudioEntryEnricher
import co.anitrend.data.studio.converter.StudioConverter
import co.anitrend.data.studio.converter.StudioEntityConverter
import co.anitrend.data.studio.converter.StudioModelConverter
import co.anitrend.data.studio.entity.filter.StudioQueryFilter
import co.anitrend.data.studio.mapper.MediaStudioMapper
import co.anitrend.data.studio.mapper.StudioDetailMapper
import co.anitrend.data.studio.mapper.StudioPagedMapper
import co.anitrend.data.studio.repository.StudioDetailRepository as StudioDetailRepositoryImpl
import co.anitrend.data.studio.repository.StudioRepository
import co.anitrend.data.studio.source.StudioDetailSourceImpl
import co.anitrend.data.studio.source.StudioSourceImpl
import co.anitrend.data.studio.source.contract.StudioDetailSource
import co.anitrend.data.studio.source.contract.StudioSource
import co.anitrend.data.studio.usecase.StudioDetailUseCaseImpl
import co.anitrend.data.studio.usecase.StudioInteractor
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<StudioDetailSource> {
            StudioDetailSourceImpl(
                remoteSource = aniListApi(),
                localSource = store().studioDao(),
                connectionLocalSource = store().mediaStudioConnectionDao(),
                edgeNetworkLocalSource = store().edgeNetworkDao(),
                controller = graphQLController(mapper = get<StudioDetailMapper>()),
                clearDataHelper = get(),
                entityConverter = get(),
                enricher = get(),
                cachePolicy = get<StudioCache>(),
                dispatcher = get(),
            )
        }
        factory<StudioSource.Search> {
            StudioSourceImpl.Search(
                remoteSource = aniListApi(),
                localSource = store().studioDao(),
                controller =
                    graphQLController(
                        mapper = get<StudioPagedMapper>(),
                        strategy = offline(),
                    ),
                converter = get(),
                clearDataHelper = get(),
                filter = get(),
                dispatcher = get(),
            )
        }
    }

private val filterModule =
    module {
        factory {
            StudioQueryFilter.Search()
        }
    }

private val cacheModule =
    module {
        factory {
            StudioCache(
                localSource = cacheLocalSource(),
            )
        }
    }

private val converterModule =
    module {
        factory {
            MediaStudioConnectionEntityConverter()
        }
        factory {
            MediaStudioEntryEnricher()
        }
        factory {
            StudioConverter()
        }
        factory {
            StudioModelConverter()
        }
        factory {
            StudioEntityConverter()
        }
    }

private val mapperModule =
    module {
        factory {
            MediaStudioMapper(
                localSource = store().mediaStudioConnectionDao(),
            )
        }
        factory {
            StudioDetailMapper(
                studioLocalSource = store().studioDao(),
                connectionLocalSource = store().mediaStudioConnectionDao(),
            )
        }
        factory {
            StudioPagedMapper(
                localSource = store().studioDao(),
            )
        }
    }

private val useCaseModule =
    module {
        factory<StudioDetailInteractor> {
            StudioDetailUseCaseImpl(
                repository = get(),
            )
        }
        factory<GetSearchStudioInteractor> {
            StudioInteractor.Search(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory<StudioDetailRepository> {
            StudioDetailRepositoryImpl(
                source = get(),
            )
        }
        factory<StudioSearchRepository> {
            StudioRepository.Search(
                source = get(),
            )
        }
    }

internal val studioModules =
    listOf(
        cacheModule,
        filterModule,
        sourceModule,
        converterModule,
        mapperModule,
        useCaseModule,
        repositoryModule,
    )
