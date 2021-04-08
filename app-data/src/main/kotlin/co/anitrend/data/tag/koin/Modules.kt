/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.tag.koin

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.core.extensions.graphApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.tag.TagInteractor
import co.anitrend.data.tag.TagListRepository
import co.anitrend.data.tag.cache.TagCache
import co.anitrend.data.tag.converter.TagEntityConverter
import co.anitrend.data.tag.converter.TagModelConverter
import co.anitrend.data.tag.entity.filter.TagQueryFilter
import co.anitrend.data.tag.mapper.TagMapper
import co.anitrend.data.tag.repository.TagRepository
import co.anitrend.data.tag.source.TagSourceImpl
import co.anitrend.data.tag.source.contract.TagSource
import co.anitrend.data.tag.usecase.TagUseCaseImpl
import org.koin.dsl.bind
import org.koin.dsl.module

private val sourceModule = module {
    factory<TagSource> {
        TagSourceImpl(
            localSource = store().tagDao(),
            remoteSource = graphApi(),
            controller = graphQLController(
                mapper = get<TagMapper.Core>()
            ),
            cachePolicy = get<TagCache>(),
            clearDataHelper = get(),
            filter = get(),
            converter = get(),
            dispatcher = get(),
        )
    }
}

private val filterModule = module {
    factory {
        TagQueryFilter()
    }
}

private val cacheModule = module {
    factory {
        TagCache(
            localSource = store().cacheDao()
        )
    }
}

private val converterModule = module {
    factory {
        TagEntityConverter()
    }
    factory {
        TagModelConverter()
    }
}

private val mapperModule = module {
    factory {
        TagMapper.Core(
            localSource = store().tagDao(),
            converter = get()
        )
    }
    factory {
        TagMapper.Embed(
            localSource = store().tagConnectionDao()
        )
    }
}

private val useCaseModule = module {
    factory<TagInteractor> {
        TagUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<TagListRepository> {
        TagRepository(
            source = get()
        )
    }
}

internal val tagModules = listOf(
    sourceModule,
    filterModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)