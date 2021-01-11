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
import co.anitrend.data.auth.AuthUserInteractor
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.auth.repository.AuthRepositoryImpl
import co.anitrend.data.auth.source.AuthSourceImpl
import co.anitrend.data.auth.source.contract.AuthSource
import co.anitrend.data.auth.usecase.AuthUseCaseImpl
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
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
            userSettings = get(),
            settings = get(),
            converter = get(),
            userLocalSource = db().userDao(),
            dispatcher = get()
        )
    }
}

private val mapperModule = module {
    factory {
        AuthMapper(
            settings = get(),
            userLocalSource = db().userDao(),
            converter = get(),
            generalOptionConverter = UserGeneralOptionModelConverter(),
            mediaOptionConverter = UserMediaOptionModelConverter(),
        )
    }
}

private val converterModule = module {
    factory {
        UserModelConverter()
    }
}

private val useCaseModule = module {
    factory<AuthUserInteractor> {
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
    converterModule,
    useCaseModule,
    repositoryModule
)