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
package co.anitrend.data.media.koin

import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.core.extensions.transaction
import co.anitrend.data.edge.core.store.IEdgeStore
import co.anitrend.data.media.GetDetailMediaInteractor
import co.anitrend.data.media.GetMediaCharactersInteractor
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.data.media.GetMediaRecommendationsPagingInteractor
import co.anitrend.data.media.GetMediaStatsInteractor
import co.anitrend.data.media.GetMediaStudiosInteractor
import co.anitrend.data.media.GetMediaRecommendationsInteractor
import co.anitrend.data.media.GetMediaRelationsInteractor
import co.anitrend.data.media.GetMediaStaffInteractor
import co.anitrend.data.media.MediaCharactersRepository
import co.anitrend.data.media.MediaDetailRepository
import co.anitrend.data.media.MediaPagingRepository
import co.anitrend.data.media.MediaRecommendationsPagingRepository
import co.anitrend.data.media.MediaRecommendationsRepository
import co.anitrend.data.media.MediaRelationsRepository
import co.anitrend.data.media.MediaStaffRepository
import co.anitrend.data.media.MediaStatsRepository
import co.anitrend.data.media.MediaStudiosRepository
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.converter.MediaCharacterConnectionEntityConverter
import co.anitrend.data.media.converter.MediaCharacterEdgeConverter
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.MediaRelationConnectionEntityConverter
import co.anitrend.data.media.converter.MediaStaffConnectionEntityConverter
import co.anitrend.data.media.converter.MediaStaffEdgeConverter
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.converter.MediaStatsEntityConverter
import co.anitrend.data.media.entity.filter.MediaQueryFilter
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.media.mapper.MediaDetailWriter
import co.anitrend.data.media.mapper.MediaDetailWriterContract
import co.anitrend.data.media.mapper.MediaEmbedWithAiringWriter
import co.anitrend.data.media.mapper.MediaEmbedWithAiringWriterContract
import co.anitrend.data.media.mapper.MediaEmbedWithMediaListWriter
import co.anitrend.data.media.mapper.MediaEmbedWithMediaListWriterContract
import co.anitrend.data.media.mapper.MediaEmbedWriter
import co.anitrend.data.media.mapper.MediaEmbedWriterContract
import co.anitrend.data.media.mapper.MediaPeopleMapper
import co.anitrend.data.media.mapper.MediaPagedWriter
import co.anitrend.data.media.mapper.MediaPagedWriterContract
import co.anitrend.data.media.mapper.MediaRelationMapper
import co.anitrend.data.media.mapper.MediaStatsMapper
import co.anitrend.data.media.mapper.MediaStatsWriter
import co.anitrend.data.media.mapper.MediaStatsWriterContract
import co.anitrend.data.media.repository.MediaRepository
import co.anitrend.data.media.source.MediaConnectionSourceImpl
import co.anitrend.data.media.source.MediaPeopleSourceImpl
import co.anitrend.data.media.source.contract.MediaConnectionSource
import co.anitrend.data.media.source.contract.MediaPeopleSource
import co.anitrend.data.media.source.MediaSourceImpl
import co.anitrend.data.media.source.contract.MediaSource
import co.anitrend.data.media.usecase.MediaInteractor
import co.anitrend.data.recommendation.converter.MediaRecommendationConnectionEntityConverter
import co.anitrend.data.recommendation.mapper.MediaRecommendationMapper
import co.anitrend.data.studio.converter.MediaStudioConnectionEntityConverter
import co.anitrend.data.studio.mapper.MediaStudioMapper
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<MediaSource.Detail> {
            MediaSourceImpl.Detail(
                remoteSource = aniListApi(),
                localSource = store().mediaDao(),
                controller =
                    graphQLController(
                        mapper = get<MediaMapper.Detail>(),
                        strategy = offline(),
                    ),
                converter = get(),
                clearDataHelper = get(),
                edgeSource = get(),
                cachePolicy = get<MediaCache>(),
                dispatcher = get(),
            )
        }
        factory<MediaConnectionSource.Relations> {
            val mapper = get<MediaRelationMapper>()

            MediaConnectionSourceImpl.Relations(
                remoteSource = aniListApi(),
                localSource = store().mediaRelationConnectionDao(),
                controller =
                    graphQLController(
                        mapper = mapper,
                        strategy = offline(),
                    ),
                mapper = mapper,
                converter = get(),
                clearDataHelper = get(),
                cachePolicy =
                    MediaCache(
                        localSource = cacheLocalSource(),
                        request = CacheRequest.MEDIA_RELATIONS,
                    ),
                dispatcher = get(),
            )
        }
        factory<MediaConnectionSource.Recommendations> {
            val mapper = get<MediaRecommendationMapper>()

            MediaConnectionSourceImpl.Recommendations(
                remoteSource = aniListApi(),
                localSource = store().mediaRecommendationConnectionDao(),
                controller =
                    graphQLController(
                        mapper = mapper,
                        strategy = offline(),
                    ),
                mapper = mapper,
                converter = get(),
                clearDataHelper = get(),
                cachePolicy =
                    MediaCache(
                        localSource = cacheLocalSource(),
                        request = CacheRequest.MEDIA_RECOMMENDATIONS,
                    ),
                dispatcher = get(),
            )
        }
        factory<MediaConnectionSource.RecommendationsPaged> {
            val mapper = get<MediaRecommendationMapper>()

            MediaConnectionSourceImpl.RecommendationsPaged(
                remoteSource = aniListApi(),
                localSource = store().mediaRecommendationConnectionDao(),
                controller =
                    graphQLController(
                        mapper = mapper,
                        strategy = offline(),
                    ),
                mapper = mapper,
                converter = get(),
                cachePolicy =
                    MediaCache(
                        localSource = cacheLocalSource(),
                        request = CacheRequest.MEDIA_RECOMMENDATIONS,
                    ),
                dispatcher = get(),
            )
        }
        factory<MediaSource.Studios> {
            val mapper = get<MediaStudioMapper>()

            MediaSourceImpl.Studios(
                remoteSource = aniListApi(),
                localSource = store().mediaStudioConnectionDao(),
                edgeLocalSource = get<IEdgeStore>().edgeMediaDao(),
                controller =
                    graphQLController(
                        mapper = mapper,
                        strategy = offline(),
                    ),
                mapper = mapper,
                converter = get(),
                enricher = get(),
                clearDataHelper = get(),
                edgeSource = get(),
                cachePolicy =
                    MediaCache(
                        localSource = cacheLocalSource(),
                        request = CacheRequest.MEDIA_STUDIOS,
                    ),
                dispatcher = get(),
            )
        }
        factory<MediaSource.Stats> {
            val mapper = get<MediaStatsMapper>()

            MediaSourceImpl.Stats(
                remoteSource = aniListApi(),
                localSource = store().mediaStatsDao(),
                controller =
                    graphQLController(
                        mapper = mapper,
                        strategy = offline(),
                    ),
                mapper = mapper,
                converter = get(),
                clearDataHelper = get(),
                cachePolicy =
                    MediaCache(
                        localSource = cacheLocalSource(),
                        request = CacheRequest.MEDIA_STATS,
                    ),
                dispatcher = get(),
            )
        }
        factory<MediaSource.Paging> {
            MediaSourceImpl.Paging(
                remoteSource = aniListApi(),
                localSource = store().mediaDao(),
                carouselSource = get(),
                controller =
                    graphQLController(
                        mapper = get<MediaMapper.Paged>(),
                        strategy = offline(),
                    ),
                clearDataHelper = get(),
                converter = get(),
                filter = get(),
                dispatcher = get(),
            )
        }
        factory<MediaPeopleSource.Characters> {
            val mapper = get<MediaPeopleMapper.Characters>()

            MediaPeopleSourceImpl.Characters(
                remoteSource = aniListApi(),
                localSource = store().mediaDao(),
                controller =
                    graphQLController(
                        mapper = mapper,
                        strategy = offline(),
                    ),
                mapper = mapper,
                converter = get(),
                cachePolicy =
                    MediaCache(
                        localSource = cacheLocalSource(),
                        request = CacheRequest.MEDIA_CHARACTERS,
                    ),
                dispatcher = get(),
            )
        }
        factory<MediaPeopleSource.Staff> {
            val mapper = get<MediaPeopleMapper.Staff>()

            MediaPeopleSourceImpl.Staff(
                remoteSource = aniListApi(),
                localSource = store().mediaDao(),
                controller =
                    graphQLController(
                        mapper = mapper,
                        strategy = offline(),
                    ),
                mapper = mapper,
                converter = get(),
                cachePolicy =
                    MediaCache(
                        localSource = cacheLocalSource(),
                        request = CacheRequest.MEDIA_STAFF,
                    ),
                dispatcher = get(),
            )
        }
    }

private val filterModule =
    module {
        factory {
            MediaQueryFilter.Paged(
                authentication = get(),
            )
        }
    }

private val cacheModule =
    module {
        factory {
            MediaCache(
                localSource = cacheLocalSource(),
            )
        }
    }

private val converterModule =
    module {
        factory {
            MediaModelConverter()
        }
        factory {
            MediaEntityViewConverter()
        }
        factory {
            MediaConverter()
        }
        factory {
            MediaCharacterEdgeConverter()
        }
        factory {
            MediaCharacterConnectionEntityConverter()
        }
        factory {
            MediaStaffEdgeConverter()
        }
        factory {
            MediaStaffConnectionEntityConverter()
        }
        factory {
            MediaRelationConnectionEntityConverter()
        }
        factory {
            MediaStatsEntityConverter()
        }
    }

private val mapperModule =
    module {
        factory<MediaPagedWriterContract> {
            MediaPagedWriter(
                localSource = store().mediaDao(),
                tagPersistence = get<co.anitrend.data.tag.mapper.TagMapper.Embed>(),
                genrePersistence = get<co.anitrend.data.genre.mapper.GenreMapper.Embed>(),
                airingPersistence = get<co.anitrend.data.airing.mapper.AiringMapper.Embed>(),
                mediaListPersistence = get<co.anitrend.data.medialist.mapper.MediaListMapper.Embed>(),
            )
        }
        factory<MediaDetailWriterContract> {
            MediaDetailWriter(
                localSource = store().mediaDao(),
                linkPersistence = get<co.anitrend.data.link.mapper.LinkMapper.Embed>(),
                rankPersistence = get<co.anitrend.data.rank.mapper.RankMapper.Embed>(),
                tagPersistence = get<co.anitrend.data.tag.mapper.TagMapper.Embed>(),
                genrePersistence = get<co.anitrend.data.genre.mapper.GenreMapper.Embed>(),
                airingPersistence = get<co.anitrend.data.airing.mapper.AiringMapper.Embed>(),
                mediaListPersistence = get<co.anitrend.data.medialist.mapper.MediaListMapper.Embed>(),
            )
        }
        factory<MediaEmbedWriterContract> {
            MediaEmbedWriter(
                localSource = store().mediaDao(),
                tagPersistence = get<co.anitrend.data.tag.mapper.TagMapper.Embed>(),
                genrePersistence = get<co.anitrend.data.genre.mapper.GenreMapper.Embed>(),
            )
        }
        factory<MediaEmbedWithAiringWriterContract> {
            MediaEmbedWithAiringWriter(
                localSource = store().mediaDao(),
                tagPersistence = get<co.anitrend.data.tag.mapper.TagMapper.Embed>(),
                genrePersistence = get<co.anitrend.data.genre.mapper.GenreMapper.Embed>(),
                airingPersistence = get<co.anitrend.data.airing.mapper.AiringMapper.Embed>(),
            )
        }
        factory<MediaEmbedWithMediaListWriterContract> {
            MediaEmbedWithMediaListWriter(
                localSource = store().mediaDao(),
                tagPersistence = get<co.anitrend.data.tag.mapper.TagMapper.Embed>(),
                genrePersistence = get<co.anitrend.data.genre.mapper.GenreMapper.Embed>(),
                airingPersistence = get<co.anitrend.data.airing.mapper.AiringMapper.Embed>(),
                mediaListPersistence = get<co.anitrend.data.medialist.mapper.MediaListMapper.Embed>(),
            )
        }
        factory {
            MediaMapper.Detail(
                mediaListMapper = get(),
                genreMapper = get(),
                tagMapper = get(),
                writer = get(),
                linkMapper = get(),
                rankMapper = get(),
                airingMapper = get(),
                localSource = store().mediaDao(),
                converter = get(),
            )
        }
        factory {
            MediaRelationMapper(
                localSource = store().mediaRelationConnectionDao(),
                converter = get(),
            )
        }
        factory {
            MediaMapper.Paged(
                mediaListMapper = get(),
                genreMapper = get(),
                tagMapper = get(),
                writer = get(),
                linkMapper = get(),
                rankMapper = get(),
                airingMapper = get(),
                localSource = store().mediaDao(),
                converter = get(),
            )
        }
        factory {
            MediaPeopleMapper.Characters(
                localSource = store().mediaDao(),
                converter = get(),
            )
        }
        factory {
            MediaPeopleMapper.Staff(
                localSource = store().mediaDao(),
                converter = get(),
            )
        }
        factory {
            MediaStatsMapper(
                writer = get(),
                transactionRunner = transaction(),
            )
        }
        factory<MediaStatsWriterContract> {
            MediaStatsWriter(
                localSource = store().mediaStatsDao(),
            )
        }
        factory {
            MediaMapper.Embed(
                genreMapper = get(),
                tagMapper = get(),
                linkMapper = get(),
                rankMapper = get(),
                writer = get(),
                localSource = store().mediaDao(),
                converter = get(),
            )
        }
        factory {
            MediaMapper.EmbedWithAiring(
                airingMapper = get(),
                genreMapper = get(),
                tagMapper = get(),
                linkMapper = get(),
                rankMapper = get(),
                writer = get(),
                localSource = store().mediaDao(),
                converter = get(),
            )
        }
        factory {
            MediaMapper.EmbedWithMediaList(
                mediaListMapper = get(),
                airingMapper = get(),
                genreMapper = get(),
                tagMapper = get(),
                linkMapper = get(),
                rankMapper = get(),
                writer = get(),
                localSource = store().mediaDao(),
                converter = get(),
            )
        }
    }

private val useCaseModule =
    module {
        factory<GetDetailMediaInteractor> {
            MediaInteractor.Detail(
                repository = get(),
            )
        }
        factory<GetMediaRelationsInteractor> {
            MediaInteractor.Relations(
                repository = get(),
            )
        }
        factory<GetMediaRecommendationsInteractor> {
            MediaInteractor.Recommendations(
                repository = get(),
            )
        }
        factory<GetMediaRecommendationsPagingInteractor> {
            MediaInteractor.RecommendationsPaged(
                repository = get(),
            )
        }
        factory<GetMediaStatsInteractor> {
            MediaInteractor.Stats(
                repository = get(),
            )
        }
        factory<GetMediaStudiosInteractor> {
            MediaInteractor.Studios(
                repository = get(),
            )
        }
        factory<GetPagingMediaInteractor> {
            MediaInteractor.Paging(
                repository = get(),
            )
        }
        factory<GetMediaCharactersInteractor> {
            MediaInteractor.Characters(
                repository = get(),
            )
        }
        factory<GetMediaStaffInteractor> {
            MediaInteractor.Staff(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory<MediaDetailRepository> {
            MediaRepository.Detail(
                source = get(),
            )
        }
        factory<MediaRelationsRepository> {
            MediaRepository.Relations(
                source = get(),
            )
        }
        factory<MediaRecommendationsRepository> {
            MediaRepository.Recommendations(
                source = get(),
            )
        }
        factory<MediaRecommendationsPagingRepository> {
            MediaRepository.RecommendationsPaging(
                source = get(),
            )
        }
        factory<MediaStatsRepository> {
            MediaRepository.Stats(
                source = get(),
            )
        }
        factory<MediaStudiosRepository> {
            MediaRepository.Studios(
                source = get(),
            )
        }
        factory<MediaPagingRepository> {
            MediaRepository.Paging(
                source = get(),
            )
        }
        factory<MediaCharactersRepository> {
            MediaRepository.Characters(
                source = get(),
            )
        }
        factory<MediaStaffRepository> {
            MediaRepository.Staff(
                source = get(),
            )
        }
    }

internal val mediaModules =
    module {
        includes(
            sourceModule,
            filterModule,
            cacheModule,
            converterModule,
            mapperModule,
            useCaseModule,
            repositoryModule,
        )
    }
