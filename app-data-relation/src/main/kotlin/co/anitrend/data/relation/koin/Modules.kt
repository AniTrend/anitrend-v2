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

package co.anitrend.data.relation.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.defaultController
import co.anitrend.data.relation.api.RelationApiFactory
import co.anitrend.data.relation.cache.RelationCache
import co.anitrend.data.relation.converters.RelationEntityConverter
import co.anitrend.data.relation.converters.RelationModelConverter
import co.anitrend.data.relation.extensions.relationLocalSource
import co.anitrend.data.relation.extensions.remoteSource
import co.anitrend.data.relation.mapper.RelationMapper
import co.anitrend.data.relation.source.RelationSourceImpl
import co.anitrend.data.relation.source.contract.RelationSource
import org.koin.dsl.module

private val coreModule = module {
    single {
        RelationApiFactory()
    }
}

private val sourceModule = module {
    factory<RelationSource> {
        RelationSourceImpl(
            remoteSource = remoteSource(),
            localSource = relationLocalSource(),
            controller = defaultController(
                mapper = get<RelationMapper>()
            ),
            clearDataHelper = get(),
            converter = get(),
            cachePolicy = get<RelationCache>(),
            dispatcher = get()
        )
    }
}

private val converterModule = module {
    factory {
        RelationEntityConverter()
    }
    factory {
        RelationModelConverter()
    }
}

private val cacheModule = module {
    factory {
        RelationCache(
            localSource = cacheLocalSource()
        )
    }
}

private val mapperModule = module {
    factory {
        RelationMapper(
            localSource = relationLocalSource(),
            converter = get(),
        )
    }
}

val sourceModules = listOf(
    coreModule,
    sourceModule,
    converterModule,
    cacheModule,
    mapperModule
)