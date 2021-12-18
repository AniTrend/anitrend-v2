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

package co.anitrend.forum.koin

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.forum.component.content.ForumContent
import co.anitrend.forum.component.screen.ForumScreen
import co.anitrend.forum.component.viewmodel.ForumViewModel
import co.anitrend.forum.component.viewmodel.state.ForumState
import co.anitrend.forum.provider.FeatureProvider
import co.anitrend.navigation.ForumRouter

private val fragmentModule = module {
    scope<ForumScreen> {
        fragment {
            ForumContent()
        }
    }
}

private val viewModelModule = module {
    viewModel {
        ForumViewModel(
            state = ForumState(
                interactor = get()
            )
        )
    }
}

private val featureModule = module {
    factory<ForumRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(fragmentModule, viewModelModule, featureModule)
)