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
package co.anitrend.data.review.koin

import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.review.DeleteReviewInteractor
import co.anitrend.data.review.GetPagingReviewInteractor
import co.anitrend.data.review.GetReviewInteractor
import co.anitrend.data.review.RateReviewInteractor
import co.anitrend.data.review.ReviewDeleteRepository
import co.anitrend.data.review.ReviewEntryRepository
import co.anitrend.data.review.ReviewPagingRepository
import co.anitrend.data.review.ReviewRateRepository
import co.anitrend.data.review.ReviewSaveRepository
import co.anitrend.data.review.SaveReviewInteractor
import co.anitrend.data.review.cache.ReviewCache
import co.anitrend.data.review.converter.ReviewEntityViewConverter
import co.anitrend.data.review.converter.ReviewModelConverter
import co.anitrend.data.review.entity.filter.ReviewQueryFilter
import co.anitrend.data.review.mapper.ReviewMapper
import co.anitrend.data.review.mapper.ReviewWriter
import co.anitrend.data.review.mapper.ReviewWriterContract
import co.anitrend.data.review.repository.ReviewRepository
import co.anitrend.data.review.source.ReviewSourceImpl
import co.anitrend.data.review.source.contract.ReviewSource
import co.anitrend.data.review.usecase.ReviewInteractor
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<ReviewSource.Entry> {
            ReviewSourceImpl.Entry(
                remoteSource = aniListApi(),
                localSource = store().reviewDao(),
                controller =
                    graphQLController(
                        mapper = get<ReviewMapper.Entry>(),
                    ),
                converter = get(),
                filter = ReviewQueryFilter.Entry(),
                clearDataHelper = get(),
                cachePolicy = get<ReviewCache>(),
                dispatcher = get(),
            )
        }
        factory<ReviewSource.Save> {
            ReviewSourceImpl.Save(
                remoteSource = aniListApi(),
                controller =
                    graphQLController(
                        mapper = get<ReviewMapper.Save>(),
                    ),
                dispatcher = get(),
            )
        }
        factory<ReviewSource.Delete> {
            ReviewSourceImpl.Delete(
                remoteSource = aniListApi(),
                localSource = store().reviewDao(),
                controller =
                    graphQLController(
                        mapper = get<ReviewMapper.Delete>(),
                    ),
                clearDataHelper = get(),
                dispatcher = get(),
            )
        }
        factory<ReviewSource.Rate> {
            ReviewSourceImpl.Rate(
                remoteSource = aniListApi(),
                controller =
                    graphQLController(
                        mapper = get<ReviewMapper.Rate>(),
                    ),
                dispatcher = get(),
            )
        }
        factory<ReviewSource.Paging> {
            ReviewSourceImpl.Paging(
                remoteSource = aniListApi(),
                localSource = store().reviewDao(),
                controller =
                    graphQLController(
                        mapper = get<ReviewMapper.Paged>(),
                        strategy = offline(),
                    ),
                filter = ReviewQueryFilter.Paged(),
                converter = get(),
                clearDataHelper = get(),
                dispatcher = get(),
            )
        }
    }

private val cacheModule =
    module {
        factory {
            ReviewCache(
                localSource = cacheLocalSource(),
                request = CacheRequest.REVIEW,
            )
        }
    }

private val mapperModule =
    module {
        factory<ReviewWriterContract> {
            ReviewWriter(
                mediaPersistence = get<co.anitrend.data.media.mapper.MediaMapper.EmbedWithAiring>(),
                userPersistence = get<co.anitrend.data.user.mapper.UserMapper.Embed>(),
                localSource = store().reviewDao(),
            )
        }
        factory {
            ReviewMapper.Entry(
                mediaMapper = get(),
                writer = get(),
                localSource = store().reviewDao(),
                converter = get(),
            )
        }
        factory {
            ReviewMapper.Delete(
                localSource = store().reviewDao(),
                converter = get(),
            )
        }
        factory {
            ReviewMapper.Save(
                localSource = store().reviewDao(),
                converter = get(),
            )
        }
        factory {
            ReviewMapper.Rate(
                localSource = store().reviewDao(),
                converter = get(),
            )
        }
        factory {
            ReviewMapper.Paged(
                mediaMapper = get(),
                userMapper = get(),
                writer = get(),
                localSource = store().reviewDao(),
                converter = get(),
            )
        }
    }

private val converterModule =
    module {
        factory {
            ReviewModelConverter()
        }
        factory {
            ReviewEntityViewConverter()
        }
    }

private val useCaseModule =
    module {
        factory<GetReviewInteractor> {
            ReviewInteractor.Entry(
                repository = get(),
            )
        }
        factory<DeleteReviewInteractor> {
            ReviewInteractor.Delete(
                repository = get(),
            )
        }
        factory<SaveReviewInteractor> {
            ReviewInteractor.Save(
                repository = get(),
            )
        }
        factory<RateReviewInteractor> {
            ReviewInteractor.Rate(
                repository = get(),
            )
        }
        factory<GetPagingReviewInteractor> {
            ReviewInteractor.Paging(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory<ReviewEntryRepository> {
            ReviewRepository.Entry(
                source = get(),
            )
        }
        factory<ReviewPagingRepository> {
            ReviewRepository.Paging(
                source = get(),
            )
        }
        factory<ReviewRateRepository> {
            ReviewRepository.Rate(
                source = get(),
            )
        }
        factory<ReviewSaveRepository> {
            ReviewRepository.Save(
                source = get(),
            )
        }
        factory<ReviewDeleteRepository> {
            ReviewRepository.Delete(
                source = get(),
            )
        }
    }

internal val reviewModules =
    module {
        includes(
            sourceModule,
            cacheModule,
            mapperModule,
            converterModule,
            useCaseModule,
            repositoryModule,
        )
    }
