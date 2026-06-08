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
package co.anitrend.data.staff.koin

import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.staff.GetPagingStaffInteractor
import co.anitrend.data.staff.StaffPagingRepository
import co.anitrend.data.staff.cache.StaffCache
import co.anitrend.data.staff.converter.StaffConverter
import co.anitrend.data.staff.converter.StaffEntityConverter
import co.anitrend.data.staff.converter.StaffModelConverter
import co.anitrend.data.staff.entity.filter.StaffQueryFilter
import co.anitrend.data.staff.mapper.StaffMapper
import co.anitrend.data.staff.repository.StaffRepository
import co.anitrend.data.staff.source.StaffSourceImpl
import co.anitrend.data.staff.source.contract.StaffSource
import co.anitrend.data.staff.usecase.StaffInteractor
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<StaffSource.Paging> {
            StaffSourceImpl.Paging(
                remoteSource = aniListApi(),
                localSource = store().staffDao(),
                controller =
                    graphQLController(
                        mapper = get<StaffMapper.Paged>(),
                        strategy = offline(),
                    ),
                converter = get(),
                clearDataHelper = get(),
                filter = get(),
                dispatcher = get(),
            )
        }
    }

private val filterModule =
    module {
        factory {
            StaffQueryFilter.Paged()
        }
    }

private val cacheModule =
    module {
        factory {
            StaffCache(
                localSource = cacheLocalSource(),
                request = CacheRequest.STAFF,
            )
        }
    }

private val converterModule =
    module {
        factory {
            StaffConverter()
        }
        factory {
            StaffModelConverter()
        }
        factory {
            StaffEntityConverter()
        }
    }

private val mapperModule =
    module {
        factory {
            StaffMapper.Paged(
                localSource = store().staffDao(),
                converter = get(),
            )
        }
    }

private val useCaseModule =
    module {
        factory<GetPagingStaffInteractor> {
            StaffInteractor.Paging(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory<StaffPagingRepository> {
            StaffRepository.Paging(
                source = get(),
            )
        }
    }

internal val staffModules =
    listOf(
        sourceModule,
        filterModule,
        cacheModule,
        converterModule,
        mapperModule,
        useCaseModule,
        repositoryModule,
    )
