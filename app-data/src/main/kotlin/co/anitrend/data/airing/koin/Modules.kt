/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.airing.koin

import co.anitrend.data.airing.AiringSchedulePagedRepository
import co.anitrend.data.airing.GetPagedAiringScheduleInteractor
import co.anitrend.data.airing.cache.AiringCache
import co.anitrend.data.airing.converters.AiringConverter
import co.anitrend.data.airing.converters.AiringEntityConverter
import co.anitrend.data.airing.converters.AiringModelConverter
import co.anitrend.data.airing.entity.filter.AiringQueryFilter
import co.anitrend.data.airing.mapper.AiringMapper
import co.anitrend.data.airing.repository.AiringScheduleRepository
import co.anitrend.data.airing.source.AiringScheduleSourceImpl
import co.anitrend.data.airing.source.contract.AiringScheduleSource
import co.anitrend.data.airing.usecase.AiringScheduleInteractor
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.core.extensions.graphApi
import co.anitrend.data.core.extensions.store
import org.koin.dsl.module

private val sourceModule = module {
    factory<AiringScheduleSource.Paged> {
        AiringScheduleSourceImpl.Paged(
            remoteSource = graphApi(),
            localSource = store().airingScheduleDao(),
            mediaLocalSource = store().mediaDao(),
            controller = graphQLController(
                mapper = get<AiringMapper.Paged>()
            ),
            converter = get(),
            clearDataHelper = get(),
            filter = get(),
            dispatcher = get(),
        )
    }
}

private val filterModule = module {
    factory {
        AiringQueryFilter.Paged(
            authentication = get()
        )
    }
}

private val cacheModule = module {
    factory {
        AiringCache(
            localSource = store().cacheDao()
        )
    }
}

private val converterModule = module {
    factory {
        AiringConverter()
    }
    factory {
        AiringModelConverter()
    }
    factory {
        AiringEntityConverter()
    }
}

private val mapperModule = module {
    factory {
        AiringMapper.Airing(
            localSource = store().airingScheduleDao(),
            converter = get()
        )
    }
    factory {
        AiringMapper.Paged(
            mediaMapper = get(),
            localSource = store().airingScheduleDao(),
            converter = get()
        )
    }
    factory {
        AiringMapper.Embed(
            localSource = store().airingScheduleDao(),
            converter = get()
        )
    }
}

private val useCaseModule = module {
    factory<GetPagedAiringScheduleInteractor>{
        AiringScheduleInteractor.Paged(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<AiringSchedulePagedRepository> {
        AiringScheduleRepository.Paged(
            source = get()
        )
    }
}

internal val airingModules = listOf(
    sourceModule,
    filterModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)