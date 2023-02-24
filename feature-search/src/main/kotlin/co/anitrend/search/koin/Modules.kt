/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.search.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.SearchRouter
import co.anitrend.search.component.content.SearchContent
import co.anitrend.search.component.presenter.SearchPresenter
import co.anitrend.search.component.screen.SearchScreen
import co.anitrend.search.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module


private val presenterModule = module {
    scope<SearchContent> {
        scoped {
            SearchPresenter(
                context = androidContext(),
                settings = get()
            )
        }
    }
}

private val fragmentModule = module {
    scope<SearchScreen> {
        fragment {
            SearchContent()
        }
    }
}

private val viewModelModule = module {

}

private val featureModule = module {
    factory<SearchRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
        listOf(presenterModule, fragmentModule, featureModule)
)
