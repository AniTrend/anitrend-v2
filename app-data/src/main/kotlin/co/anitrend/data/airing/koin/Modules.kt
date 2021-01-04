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

import co.anitrend.data.airing.converters.AiringConverter
import co.anitrend.data.airing.converters.AiringEntityConverter
import co.anitrend.data.airing.converters.AiringModelConverter
import co.anitrend.data.airing.mapper.detail.AiringScheduleMapper
import co.anitrend.data.airing.mapper.paged.AiringSchedulePagedMapper
import co.anitrend.data.arch.extension.db
import org.koin.dsl.module

private val mapperModule = module {
    factory {
        AiringScheduleMapper(
            localSource = db().airingScheduleDao()
        )
    }
    factory {
        AiringSchedulePagedMapper(
            localSource = db().airingScheduleDao(),
            converter = get()
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

internal val airingModules = listOf(
    mapperModule, converterModule
)