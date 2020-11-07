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

package co.anitrend.data.auth.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.graphQLController
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.auth.repository.AuthRepositoryImpl
import co.anitrend.data.auth.source.AuthSourceImpl
import co.anitrend.data.auth.source.contract.AuthSource
import co.anitrend.data.auth.usecase.AuthUseCaseContract
import co.anitrend.data.auth.usecase.AuthUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<AuthSource> {
        AuthSourceImpl(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().authDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<AuthMapper>()
            ),
            settings = get(),
            userLocalSource = db().userDao(),
            dispatchers = get()
        )
    }
}

private val mapperModule = module {
    factory {
        AuthMapper(
            userLocalSource = db().userDao()
        )
    }
}

private val useCaseModule = module {
    factory<AuthUseCaseContract> {
        AuthUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        AuthRepositoryImpl(
            source = get()
        )
    }
}

internal val authModules = listOf(
    sourceModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)