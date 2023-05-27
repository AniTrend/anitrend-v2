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

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.auth.AuthUserInteractor
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.auth.repository.AuthRepositoryImpl
import co.anitrend.data.auth.source.AuthSourceImpl
import co.anitrend.data.auth.source.contract.AuthSource
import co.anitrend.data.auth.usecase.AuthUseCaseImpl
import co.anitrend.data.core.extensions.graphApi
import co.anitrend.data.core.extensions.store
import org.koin.dsl.module

private val sourceModule = module {
    factory<AuthSource> {
        AuthSourceImpl(
            remoteSource = graphApi(),
            localSource = store().authDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<AuthMapper>()
            ),
            settings = get(),
            converter = get(),
            userLocalSource = store().userDao(),
            authenticationHelper = get(),
            dispatcher = get()
        )
    }
}

private val mapperModule = module {
    factory {
        AuthMapper(
            settings = get(),
            generalOptionMapper = get(),
            mediaOptionMapper = get(),
            notificationMapper = get(),
            localSource = store().userDao(),
            converter = get(),
        )
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

internal val authModules = module {
    includes(
        sourceModule,
        mapperModule,
        useCaseModule,
        repositoryModule
    )
}
