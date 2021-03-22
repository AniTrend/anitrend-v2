/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.jikan.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.defaultController
import co.anitrend.data.jikan.cache.JikanCache
import co.anitrend.data.jikan.converters.JikanModelConverter
import co.anitrend.data.jikan.mapper.JikanMapper
import co.anitrend.data.jikan.source.JikanSourceImpl
import co.anitrend.data.jikan.source.contract.JikanSource
import org.koin.dsl.module

private val sourceModule = module {
    factory<JikanSource> { 
        JikanSourceImpl(
            remoteSource = api(EndpointType.JIKAN),
            localSource = db().jikanDao(),
            controller = defaultController(
                mapper = get<JikanMapper>()
            ),
            clearDataHelper = get(),
            dispatcher = get(),
            cachePolicy = get<JikanCache>()
        )
    }
}

private val converterModule = module {
    factory {
        JikanModelConverter()
    }
}

private val cacheModule = module {
    factory {
        JikanCache(
            localSource = db().cacheDao()
        )
    }
}

private val mapperModule = module {
    factory {
        JikanMapper(
            localSource = db().jikanDao(),
            converter = get(),
        )
    }
}

internal val jikanModules = listOf(
    sourceModule,
    converterModule,
    cacheModule,
    mapperModule
)