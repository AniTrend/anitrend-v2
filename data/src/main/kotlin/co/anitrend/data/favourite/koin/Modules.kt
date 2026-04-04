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
package co.anitrend.data.favourite.koin

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.favourite.datasource.remote.FavouriteRemoteSource
import co.anitrend.data.favourite.mapper.FavouriteMapper
import co.anitrend.data.favourite.repository.FavouriteRepository
import co.anitrend.data.favourite.source.FavouriteSourceImpl
import co.anitrend.data.favourite.source.contract.FavouriteSource
import co.anitrend.data.favourite.usecase.FavouriteInteractor
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<FavouriteSource.Toggle> {
            FavouriteSourceImpl.Toggle(
                remoteSource = aniListApi<FavouriteRemoteSource>(),
                controller =
                    graphQLController(
                        mapper = get<FavouriteMapper>(),
                    ),
                dispatcher = get(),
            )
        }
    }

private val mapperModule =
    module {
        factory {
            FavouriteMapper()
        }
    }

private val useCaseModule =
    module {
        factory {
            FavouriteInteractor.Toggle(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory {
            FavouriteRepository(
                source = get(),
            )
        }
    }

internal val favouriteModules =
    module {
        includes(
            sourceModule,
            mapperModule,
            useCaseModule,
            repositoryModule,
        )
    }
