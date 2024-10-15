/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.auth.koin

import androidx.browser.customtabs.CustomTabsIntent
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.auth.component.content.AuthContent
import co.anitrend.auth.component.screen.AuthScreen
import co.anitrend.auth.component.viewmodel.AuthViewModel
import co.anitrend.auth.component.viewmodel.state.AuthState
import co.anitrend.auth.presenter.AuthPresenter
import co.anitrend.auth.provider.FeatureProvider
import co.anitrend.core.R
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.AuthRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentModule =
    module {
        scope<AuthScreen> {
            fragment {
                AuthContent(
                    stateLayoutConfig =
                        StateLayoutConfig(
                            errorDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
                            loadingMessage = R.string.label_text_loading,
                            retryAction = R.string.label_text_action_retry,
                        ),
                )
            }
        }
    }

private val viewModelModule =
    module {
        viewModel {
            AuthViewModel(
                state = AuthState(get()),
            )
        }
    }

private val presenterModule =
    module {
        scope<AuthContent> {
            scoped {
                AuthPresenter(
                    context = androidContext(),
                    settings = get(),
                    clientId = getProperty("aniListClientId"),
                    customTabs = get<CustomTabsIntent.Builder>().build(),
                )
            }
        }
    }

private val featureModule =
    module {
        factory<AuthRouter.Provider> {
            FeatureProvider()
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(fragmentModule, viewModelModule, presenterModule, featureModule),
    )
