/*
 * Copyright (C) 2020 AniTrend
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
import co.anitrend.data.auth.mapper.AuthenticatedUserWriter
import co.anitrend.data.auth.mapper.AuthenticatedUserWriterContract
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.auth.mapper.UserGeneralOptionWriter
import co.anitrend.data.auth.mapper.UserGeneralOptionWriterContract
import co.anitrend.data.auth.mapper.UserMediaOptionWriter
import co.anitrend.data.auth.mapper.UserMediaOptionWriterContract
import co.anitrend.data.auth.repository.AuthRepositoryImpl
import co.anitrend.data.auth.source.AuthSourceImpl
import co.anitrend.data.auth.source.contract.AuthSource
import co.anitrend.data.auth.usecase.AuthUseCaseImpl
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.core.extensions.transaction
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<AuthSource> {
            AuthSourceImpl(
                remoteSource = aniListApi(),
                localSource = store().authDao(),
                clearDataHelper = get(),
                controller =
                    graphQLController(
                        mapper = get<AuthMapper>(),
                    ),
                settings = get(),
                converter = get(),
                userLocalSource = store().userDao(),
                authenticationHelper = get(),
                dispatcher = get(),
            )
        }
    }

private val mapperModule =
    module {
        factory {
            AuthMapper(
                generalOptionMapper = get(),
                mediaOptionMapper = get(),
                notificationMapper = get(),
                writer = get(),
                transactionRunner = transaction(),
                converter = get(),
            )
        }
        factory<AuthenticatedUserWriterContract> {
            AuthenticatedUserWriter(
                generalOptionWriter = get(),
                mediaOptionWriter = get(),
                notificationMapper = get(),
                localSource = store().userDao(),
            )
        }
        factory<UserGeneralOptionWriterContract> {
            UserGeneralOptionWriter(
                settings = get(),
                mapper = get(),
            )
        }
        factory<UserMediaOptionWriterContract> {
            UserMediaOptionWriter(
                settings = get(),
                mapper = get(),
            )
        }
    }

private val useCaseModule =
    module {
        factory<AuthUserInteractor> {
            AuthUseCaseImpl(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory {
            AuthRepositoryImpl(
                source = get(),
            )
        }
    }

internal val authModules =
    module {
        includes(
            sourceModule,
            mapperModule,
            useCaseModule,
            repositoryModule,
        )
    }
