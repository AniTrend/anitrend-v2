/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.task.review.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.ReviewTaskRouter
import co.anitrend.task.review.component.ReviewDeleteEntryWorker
import co.anitrend.task.review.component.ReviewSaveEntryWorker
import co.anitrend.task.review.component.ReviewVoteEntryWorker
import co.anitrend.task.review.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

private val workManagerModule =
    module {
        worker { scope ->
            ReviewSaveEntryWorker(
                context = androidContext(),
                parameters = scope.get(),
                interactor = get(),
            )
        }
        worker { scope ->
            ReviewDeleteEntryWorker(
                context = androidContext(),
                parameters = scope.get(),
                interactor = get(),
            )
        }
        worker { scope ->
            ReviewVoteEntryWorker(
                context = androidContext(),
                parameters = scope.get(),
                interactor = get(),
            )
        }
    }

private val featureModule =
    module {
        factory<ReviewTaskRouter.Provider> {
            FeatureProvider()
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(workManagerModule, featureModule),
    )
