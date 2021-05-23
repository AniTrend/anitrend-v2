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

package co.anitrend.data.medialist.koin

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.core.extensions.graphApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.medialist.*
import co.anitrend.data.medialist.MediaListCollectionRepository
import co.anitrend.data.medialist.MediaListEntryRepository
import co.anitrend.data.medialist.MediaListPagedRepository
import co.anitrend.data.medialist.cache.MediaListCache
import co.anitrend.data.medialist.converter.MediaListEntityConverter
import co.anitrend.data.medialist.converter.MediaListEntityViewConverter
import co.anitrend.data.medialist.converter.MediaListModelConverter
import co.anitrend.data.medialist.entity.filter.MediaListQueryFilter
import co.anitrend.data.medialist.mapper.MediaListMapper
import co.anitrend.data.medialist.repository.MediaListRepository
import co.anitrend.data.medialist.source.MediaListSourceImpl
import co.anitrend.data.medialist.source.contract.MediaListSource
import co.anitrend.data.medialist.usecase.MediaListInteractor
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaListSource.Sync> {
        MediaListSourceImpl.Sync(
            remoteSource = graphApi(),
            controller = graphQLController(
                mapper = get<MediaListMapper.Collection>()
            ),
            dispatcher = get()
        )
    }
    factory<MediaListSource.Collection> {
        MediaListSourceImpl.Collection(
            remoteSource = graphApi(),
            localSource = store().mediaListDao(),
            mediaLocalSource = store().mediaDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<MediaListMapper.Collection>()
            ),
            filter = MediaListQueryFilter.Collection(
                authentication = get()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
    factory<MediaListSource.Paged> {
        MediaListSourceImpl.Paged(
            remoteSource = graphApi(),
            localSource = store().mediaListDao(),
            mediaLocalSource = store().mediaDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<MediaListMapper.Paged>()
            ),
            filter = MediaListQueryFilter.Paged(
                authentication = get()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
    factory<MediaListSource.DeleteCustomList> {
        MediaListSourceImpl.DeleteCustomList(
            remoteSource = graphApi(),
            localSource = store().customListDao(),
            userSource = get(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<MediaListMapper.DeletedCustomList>()
            ),
            dispatcher = get(),
            settings = get()
        )
    }
    factory<MediaListSource.DeleteEntry> {
        MediaListSourceImpl.DeleteEntry(
            remoteSource = graphApi(),
            localSource = store().mediaListDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<MediaListMapper.DeletedEntry>()
            ),
            dispatcher = get(),
            settings = get()
        )
    }
    factory<MediaListSource.Entry> {
        MediaListSourceImpl.Entry(
            remoteSource = graphApi(),
            localSource = store().mediaListDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<MediaListMapper.Entry>()
            ),
            cachePolicy = get<MediaListCache>(),
            converter = get(),
            dispatcher = get()
        )
    }
    factory<MediaListSource.SaveEntry> {
        MediaListSourceImpl.SaveEntry(
            remoteSource = graphApi(),
            controller = graphQLController(
                mapper = get<MediaListMapper.SaveEntry>()
            ),
            dispatcher = get()
        )
    }
    factory<MediaListSource.SaveEntries> {
        MediaListSourceImpl.SaveEntries(
            remoteSource = graphApi(),
            controller = graphQLController(
                mapper = get<MediaListMapper.SaveEntries>()
            ),
            dispatcher = get()
        )
    }
}

private val cacheModule = module {
    factory {
        MediaListCache(
            localSource = store().cacheDao()
        )
    }
}

private val mapperModule = module {
    factory {
        MediaListMapper.DeletedEntry(
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.DeletedCustomList(
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.Entry(
            mediaMapper = get(),
            customListMapper = get(),
            customScoreMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.SaveEntry(
            mediaMapper = get(),
            customListMapper = get(),
            customScoreMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.SaveEntries(
            mediaMapper = get(),
            customListMapper = get(),
            customScoreMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.Paged(
            mediaMapper = get(),
            customListMapper = get(),
            customScoreMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.Collection(
            userMapper = get(),
            mediaMapper = get(),
            customListMapper = get(),
            customScoreMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.Embed(
            customListMapper = get(),
            customScoreMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.EmbedWithMedia(
            mediaMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
}

private val converterModule = module {
    factory {
        MediaListModelConverter()
    }
    factory {
        MediaListEntityConverter()
    }
    factory {
        MediaListEntityViewConverter()
    }
}

private val useCaseModule = module {
    factory<SyncMediaListEntryInteractor> {
        MediaListInteractor.Sync(repository = get())
    }
    factory<GetMediaListEntryInteractor> {
        MediaListInteractor.Entry(repository = get())
    }
    factory<GetPagedMediaListInteractor> {
        MediaListInteractor.Paged(repository = get())
    }
    factory<GetCollectionMediaListInteractor> {
        MediaListInteractor.Collection(repository = get())
    }
    factory<SaveMediaListEntryInteractor> {
        MediaListInteractor.SaveEntry(repository = get())
    }
    factory<SaveMediaListEntriesInteractor> {
        MediaListInteractor.SaveEntries(repository = get())
    }
    factory<DeleteMediaListEntryInteractor> {
        MediaListInteractor.DeleteEntry(repository = get())
    }
    factory<DeleteCustomMediaListInteractor> {
        MediaListInteractor.DeleteCustomList(repository = get())
    }
}

private val repositoryModule = module {
    factory<MediaListSyncRepository> {
        MediaListRepository.Sync(
            source = get()
        )
    }
    factory<MediaListEntryRepository> {
        MediaListRepository.Entry(
            source = get()
        )
    }
    factory<MediaListCollectionRepository> {
        MediaListRepository.Collection(
            source = get()
        )
    }
    factory<MediaListPagedRepository> {
        MediaListRepository.Paged(
            source = get()
        )
    }
    factory<MediaListSaveEntryRepository> {
        MediaListRepository.SaveEntry(
            source = get()
        )
    }
    factory<MediaListSaveEntriesRepository> {
        MediaListRepository.SaveEntries(
            source = get()
        )
    }
    factory<MediaListDeleteEntryRepository> {
        MediaListRepository.DeleteEntry(
            source = get()
        )
    }
    factory<DeleteCustomMediaListRepository> {
        MediaListRepository.DeleteCustomList(
            source = get()
        )
    }
}

internal val mediaListModules = listOf(
    sourceModule,
    cacheModule,
    mapperModule,
    converterModule,
    useCaseModule,
    repositoryModule
)