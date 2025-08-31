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
package co.anitrend.settings.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.SettingsRouter
import co.anitrend.settings.component.builder.PreferenceBuilder
import co.anitrend.settings.component.content.log.viewmodel.LogViewModel
import co.anitrend.settings.component.content.notification.presenter.NotificationPresenter
import co.anitrend.settings.component.content.power.presenter.PowerPresenter
import co.anitrend.settings.component.content.privacy.presenter.PrivacyPresenter
import co.anitrend.settings.component.content.storage.presenter.StoragePresenter
import co.anitrend.settings.component.content.sync.presenter.SynchronizationPresenter
import co.anitrend.settings.component.content.task.viewmodel.TaskViewModel
import co.anitrend.settings.component.content.theme.presenter.ThemePresenter
import co.anitrend.settings.component.presenter.SettingsPresenter
import co.anitrend.settings.component.screen.SettingsScreen
import co.anitrend.settings.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val presenterModule =
    module {
        scope<SettingsScreen> {
            scoped {
                SettingsPresenter(
                    context = androidContext(),
                    settings = get(),
                    preferenceBuilder = PreferenceBuilder(),
                )
            }
        }

        // Presenter for synchronization screen
        factory {
            SynchronizationPresenter(
                context = androidContext(),
                settings = get(),
                preferenceBuilder = PreferenceBuilder(),
            )
        }

        // Presenter for privacy screen
        factory {
            PrivacyPresenter(
                context = androidContext(),
                settings = get(),
                preferenceBuilder = PreferenceBuilder(),
            )
        }

        // Presenter for power screen
        factory {
            PowerPresenter(
                context = androidContext(),
                settings = get(),
                preferenceBuilder = PreferenceBuilder(),
            )
        }

        // Presenter for storage screen
        factory {
            StoragePresenter(
                context = androidContext(),
                settings = get(),
                preferenceBuilder = PreferenceBuilder(),
                storageController = get(),
            )
        }

        // Presenter for theme screen
        factory {
            ThemePresenter(
                context = androidContext(),
                settings = get(),
                preferenceBuilder = PreferenceBuilder(),
            )
        }

        // Presenter for notification screen
        factory {
            NotificationPresenter(
                context = androidContext(),
                settings = get(),
                preferenceBuilder = PreferenceBuilder(),
            )
        }
    }

private val featureModule =
    module {
        factory<SettingsRouter.Provider> {
            FeatureProvider()
        }
    }

private val viewModelModule =
    module {
        viewModelOf(::TaskViewModel)
        viewModelOf(::LogViewModel)
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(presenterModule, featureModule, viewModelModule),
    )
