/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.task.medialist.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.MediaListTaskRouter
import co.anitrend.task.medialist.component.worker.MediaListDeleteCustomListWorker
import co.anitrend.task.medialist.component.worker.MediaListDeleteEntryWorker
import co.anitrend.task.medialist.component.worker.MediaListSaveEntriesWorker
import co.anitrend.task.medialist.component.worker.MediaListSaveEntryWorker
import co.anitrend.task.medialist.component.sync.MediaListAnimeSyncWorker
import co.anitrend.task.medialist.component.sync.MediaListMangaSyncWorker
import co.anitrend.task.medialist.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

private val workManagerModule = module {
    worker { scope ->
        MediaListSaveEntryWorker(
            context = androidContext(),
            parameters = scope.get(),
            interactor = get()
        )
    }
    worker { scope ->
        MediaListSaveEntriesWorker(
            context = androidContext(),
            parameters = scope.get(),
            interactor = get()
        )
    }
    worker { scope ->
        MediaListDeleteEntryWorker(
            context = androidContext(),
            parameters = scope.get(),
            interactor = get()
        )
    }
    worker { scope ->
        MediaListDeleteCustomListWorker(
            context = androidContext(),
            parameters = scope.get(),
            interactor = get()
        )
    }
    worker { scope ->
        MediaListAnimeSyncWorker(
            context = androidContext(),
            parameters = scope.get(),
            interactor = get(),
            settings = get()
        )
    }
    worker { scope ->
        MediaListMangaSyncWorker(
            context = androidContext(),
            parameters = scope.get(),
            interactor = get(),
            settings = get()
        )
    }
}

private val featureModule = module {
    factory<MediaListTaskRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(workManagerModule, featureModule)
)