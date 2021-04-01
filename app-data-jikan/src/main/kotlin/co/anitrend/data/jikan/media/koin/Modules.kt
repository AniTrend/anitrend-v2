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

package co.anitrend.data.jikan.media.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.defaultController
import co.anitrend.data.jikan.extensions.jikanLocalSource
import co.anitrend.data.jikan.extensions.remoteSource
import co.anitrend.data.jikan.media.cache.JikanCache
import co.anitrend.data.jikan.media.converters.JikanModelConverter
import co.anitrend.data.jikan.media.datasource.local.IJikanStore
import co.anitrend.data.jikan.media.mapper.JikanMapper
import co.anitrend.data.jikan.media.source.JikanSourceImpl
import co.anitrend.data.jikan.media.source.contract.JikanSource
import org.koin.dsl.module

private val sourceModule = module {
    factory<JikanSource> { 
        JikanSourceImpl(
            remoteSource = remoteSource(),
            localSource = jikanLocalSource(),
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
            localSource = cacheLocalSource()
        )
    }
}

private val mapperModule = module {
    factory {
        JikanMapper(
            localSource = jikanLocalSource(),
            converter = get(),
        )
    }
}

val jikanModules = listOf(
    sourceModule,
    converterModule,
    cacheModule,
    mapperModule
)