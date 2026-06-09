/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.koin

import co.anitrend.core.R
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.push.PushConnector
import co.anitrend.push.PushRegistrationCoordinator
import co.anitrend.push.UnifiedPushConnector
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val variantModule =
    module(createdAtStart = true) {
        factory<PushConnector> {
            UnifiedPushConnector(
                context = androidApplication(),
            )
        }
        factory {
            PushRegistrationCoordinator(
                connector = get(),
                messageForDistributor =
                    androidContext().getString(R.string.application_name),
            )
        }
    }

internal val githubAppModules =
    DynamicFeatureModuleHelper(
        listOf(variantModule),
    )
