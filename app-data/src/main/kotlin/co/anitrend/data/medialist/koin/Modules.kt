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
import co.anitrend.data.medialist.GetPagedMediaListInteractor
import co.anitrend.data.medialist.converter.MediaListEntityConverter
import co.anitrend.data.medialist.converter.MediaListModelConverter
import co.anitrend.data.medialist.mapper.MediaListMapper
import co.anitrend.data.medialist.repository.MediaListRepository
import co.anitrend.data.medialist.source.paged.MediaListPagedSourceImpl
import co.anitrend.data.medialist.source.paged.contract.MediaListPagedSource
import co.anitrend.data.medialist.usecase.MediaListInteractor.Paged
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaListPagedSource> {
        MediaListPagedSourceImpl(
            remoteSource = graphApi(),
            localSource = store().mediaListDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<MediaListMapper.Paged>()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val mapperModule = module {
    factory {
        MediaListMapper.Paged(
            mediaMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.Collection(
            userMapper = get(),
            localSource = store().mediaListDao(),
            converter = get()
        )
    }
    factory {
        MediaListMapper.Embed(
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
}

private val useCaseModule = module {
    factory<GetPagedMediaListInteractor> {
        Paged(repository = get())
    }
}

private val repositoryModule = module {
    factory {
        MediaListRepository.Paged(
            source = get()
        )
    }
}

internal val mediaListModules = listOf(
    sourceModule,
    mapperModule,
    converterModule,
    useCaseModule,
    repositoryModule
)