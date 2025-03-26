/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.android.navigation.drawer.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.core.koin.scope.AppScope
import co.anitrend.navigation.NavigationDrawerRouter
import co.anitrend.android.navigation.drawer.action.provider.viewmodel.NotificationProviderViewModel
import co.anitrend.android.navigation.drawer.adapter.AccountAdapter
import co.anitrend.android.navigation.drawer.adapter.NavigationAdapter
import co.anitrend.android.navigation.drawer.component.content.BottomDrawerContent
import co.anitrend.android.navigation.drawer.component.content.contract.INavigationDrawer
import co.anitrend.android.navigation.drawer.component.presenter.DrawerPresenter
import co.anitrend.android.navigation.drawer.component.viewmodel.AccountViewModel
import co.anitrend.android.navigation.drawer.component.viewmodel.NavigationViewModel
import co.anitrend.android.navigation.drawer.component.viewmodel.mapper.UsersToAccountsMapper
import co.anitrend.android.navigation.drawer.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

private val presenterModule =
    module {
        scope<BottomDrawerContent> {
            scoped {
                DrawerPresenter(
                    context = androidContext(),
                    settings = get(),
                )
            }
        }
    }

private val viewModelModule =
    module {
        viewModel {
            AccountViewModel(
                mapper =
                    UsersToAccountsMapper(
                        settings = get(),
                    ),
                interactor = get(),
            )
        }
        viewModel {
            NavigationViewModel(
                settings = get(),
                savedStateHandle = get(),
            )
        }
        viewModel {
            NotificationProviderViewModel(interactor = get())
        }
    }

private val fragmentModule =
    module {
        scope(AppScope.BOTTOM_NAV_DRAWER.qualifier) {
            fragment {
                BottomDrawerContent(
                    navigationAdapter =
                        NavigationAdapter(
                            resources = androidContext().resources,
                            stateConfiguration = get(),
                        ),
                    accountAdapter =
                        AccountAdapter(
                            resources = androidContext().resources,
                            stateConfiguration = get(),
                        ),
                )
            } bind INavigationDrawer::class
        }
    }

private val featureModule =
    module {
        factory<NavigationDrawerRouter.Provider> {
            FeatureProvider()
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(presenterModule, viewModelModule, fragmentModule, featureModule),
    )
