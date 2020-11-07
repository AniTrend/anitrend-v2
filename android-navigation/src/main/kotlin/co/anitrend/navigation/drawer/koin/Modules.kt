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

package co.anitrend.navigation.drawer.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.core.settings.Settings
import co.anitrend.navigation.NavigationDrawerRouter
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.adapter.AccountAdapter
import co.anitrend.navigation.drawer.adapter.NavigationAdapter
import co.anitrend.navigation.drawer.component.content.BottomDrawerContent
import co.anitrend.navigation.drawer.component.presenter.DrawerPresenter
import co.anitrend.navigation.drawer.component.viewmodel.BottomDrawerViewModel
import co.anitrend.navigation.drawer.component.viewmodel.state.AccountState
import co.anitrend.navigation.drawer.component.viewmodel.state.NavigationState
import co.anitrend.navigation.drawer.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val coreModule = module {
	
}

private val presenterModule = module {
	scope<BottomDrawerContent> {
		scoped {
			DrawerPresenter(
				context = androidContext(),
				settings = get()
			)
		}
	}
}

private val viewModelModule = module {
	viewModel {
		val settings = get<Settings>()
		BottomDrawerViewModel(
			accountState = AccountState(
				settings = settings,
				useCase = get()
			),
			navigationState = NavigationState(),
			authenticatedKey = androidContext().getString(R.string.settings_is_authenticated),
			settings = settings
		)
	}
}

private val fragmentModule = module {
	fragment {
		BottomDrawerContent(
			navigationAdapter = NavigationAdapter(
				resources = androidContext().resources,
				stateConfiguration = get()
			),
			accountAdapter = AccountAdapter(
				resources = androidContext().resources,
				stateConfiguration = get()
			),
		)
	}
}

private val featureModule = module {
	factory<NavigationDrawerRouter.Provider> {
		FeatureProvider()
	}
}

internal val moduleHelper = DynamicFeatureModuleHelper(
	listOf(coreModule, presenterModule, viewModelModule, fragmentModule, featureModule)
) 
