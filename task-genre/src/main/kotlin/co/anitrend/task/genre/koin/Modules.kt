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

package co.anitrend.task.genre.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.GenreTaskRouter
import co.anitrend.task.genre.component.GenreWorker
import co.anitrend.task.genre.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

private val workManagerModule = module {
    worker { params ->
        GenreWorker(
            context = androidContext(),
            parameters = params.get(),
            interactor = get()
        )
    }
}

private val featureModule = module {
    factory<GenreTaskRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(workManagerModule, featureModule)
)