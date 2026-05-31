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
package co.anitrend.data.character.koin

import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.character.CharacterSearchRepository
import co.anitrend.data.character.GetSearchCharacterInteractor
import co.anitrend.data.character.cache.CharacterCache
import co.anitrend.data.character.converter.CharacterConverter
import co.anitrend.data.character.converter.CharacterEntityConverter
import co.anitrend.data.character.converter.CharacterModelConverter
import co.anitrend.data.character.mapper.CharacterMapper
import co.anitrend.data.character.repository.CharacterRepository
import co.anitrend.data.character.source.CharacterSourceImpl
import co.anitrend.data.character.source.contract.CharacterSource
import co.anitrend.data.character.usecase.CharacterInteractor
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<CharacterSource.Search> {
            CharacterSourceImpl.Search(
                remoteSource = aniListApi(),
                localSource = store().characterDao(),
                controller =
                    graphQLController(
                        mapper = get<CharacterMapper.Paged>(),
                        strategy = offline(),
                    ),
                converter = get(),
                clearDataHelper = get(),
                dispatcher = get(),
            )
        }
    }

private val cacheModule =
    module {
        factory {
            CharacterCache(
                localSource = cacheLocalSource(),
                request = CacheRequest.CHARACTER,
            )
        }
    }

private val converterModule =
    module {
        factory {
            CharacterConverter()
        }
        factory {
            CharacterModelConverter()
        }
        factory {
            CharacterEntityConverter()
        }
    }

private val mapperModule =
    module {
        factory {
            CharacterMapper.Paged(
                localSource = store().characterDao(),
                converter = get(),
            )
        }
    }

private val useCaseModule =
    module {
        factory<GetSearchCharacterInteractor> {
            CharacterInteractor.Search(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory<CharacterSearchRepository> {
            CharacterRepository.Search(
                source = get(),
            )
        }
    }

internal val characterModules =
    module {
        includes(
            sourceModule,
            cacheModule,
            converterModule,
            mapperModule,
            useCaseModule,
            repositoryModule,
        )
    }
