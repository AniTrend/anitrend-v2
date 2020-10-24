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

package co.anitrend.data.moe.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.online
import co.anitrend.data.moe.mapper.MoeResponseMapper
import co.anitrend.data.moe.source.MoeSourceImpl
import co.anitrend.data.moe.source.contract.MoeSource
import org.koin.dsl.module

private val sourceModule = module {
    factory<MoeSource> {
        MoeSourceImpl(
            remoteSource = api(EndpointType.RELATION_MOE),
            localSource = db().sourceDao(),
            mapper = get(),
            clearDataHelper = get(),
            strategy = online(),
            dispatchers = get()
        )
    }
}

private val mapperModule = module {
    factory {
        MoeResponseMapper(
            localSource = db().sourceDao()
        )
    }
}


internal val sourceModules = listOf(
    sourceModule,
    mapperModule
)