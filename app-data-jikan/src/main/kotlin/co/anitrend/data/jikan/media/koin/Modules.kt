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
import co.anitrend.data.jikan.author.mapper.JikanAuthorMapper
import co.anitrend.data.jikan.extensions.db
import co.anitrend.data.jikan.extensions.jikanLocalSource
import co.anitrend.data.jikan.extensions.remoteSource
import co.anitrend.data.jikan.licensor.mapper.JikanLicensorMapper
import co.anitrend.data.jikan.media.cache.JikanCache
import co.anitrend.data.jikan.media.converters.*
import co.anitrend.data.jikan.media.mapper.JikanMapper
import co.anitrend.data.jikan.media.source.JikanSourceImpl
import co.anitrend.data.jikan.media.source.contract.JikanSource
import co.anitrend.data.jikan.producer.mapper.JikanProducerMapper
import co.anitrend.data.jikan.studio.mapper.JikanStudioMapper
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
    factory {
        JikanAuthorModelConverter()
    }
    factory {
        JikanProducerModelConverter()
    }
    factory {
        JikanLicensorModelConverter()
    }
    factory {
        JikanStudioModelConverter()
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
            authorMapper = get(),
            licensorMapper = get(),
            producerMapper = get(),
            studioMapper = get(),
            localSource = jikanLocalSource(),
            converter = get(),
        )
    }
    factory {
        JikanAuthorMapper.Embed(
            localSource = db().jikanAuthorDao(),
            converter = get()
        )
    }
    factory {
        JikanProducerMapper.Embed(
            localSource = db().jikanProducerDao(),
            converter = get()
        )
    }
    factory {
        JikanLicensorMapper.Embed(
            localSource = db().jikanLicensorDao(),
            converter = get()
        )
    }
    factory {
        JikanStudioMapper.Embed(
            localSource = db().jikanStudioDao(),
            converter = get()
        )
    }
}

internal val mediaModules = listOf(
    sourceModule,
    converterModule,
    cacheModule,
    mapperModule
)