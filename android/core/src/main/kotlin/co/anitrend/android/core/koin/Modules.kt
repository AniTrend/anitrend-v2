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
package co.anitrend.android.core.koin

import android.content.pm.ShortcutManager
import android.net.ConnectivityManager
import android.os.PowerManager
import androidx.core.app.NotificationManagerCompat
import co.anitrend.android.core.controller.power.AndroidPowerController
import co.anitrend.android.core.controller.power.contract.IPowerController
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.helpers.notification.NotificationHelper
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.settings.common.IConfigurationSettings
import co.anitrend.android.core.settings.common.locale.ILocaleSettings
import co.anitrend.android.core.settings.common.theme.IThemeSettings
import co.anitrend.android.core.settings.helper.config.ConfigurationHelper
import co.anitrend.android.core.settings.helper.config.contract.IConfigurationHelper
import co.anitrend.android.core.settings.helper.locale.LocaleHelper
import co.anitrend.android.core.settings.helper.locale.contract.ILocaleHelper
import co.anitrend.android.core.settings.helper.theme.ThemeHelper
import co.anitrend.android.core.settings.helper.theme.contract.IThemeHelper
import co.anitrend.android.core.shortcut.ShortcutController
import co.anitrend.android.core.shortcut.contract.IShortcutController
import co.anitrend.android.core.storage.StorageController
import co.anitrend.android.core.storage.contract.IStorageController
import co.anitrend.arch.extension.dispatchers.SupportDispatcher
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.preference.contract.ISupportPreference
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.settings.cache.ICacheSettings
import co.anitrend.data.settings.connectivity.IConnectivitySettings
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.developer.IDeveloperSettings
import co.anitrend.data.settings.feature.IFeatureFlagSetting
import co.anitrend.data.settings.notification.INotificationSettings
import co.anitrend.data.settings.power.IPowerSettings
import co.anitrend.data.settings.privacy.IPrivacySettings
import co.anitrend.data.settings.push.IUnifiedPushSettings
import co.anitrend.data.settings.refresh.IRefreshBehaviourSettings
import co.anitrend.data.settings.sort.ISortOrderSettings
import co.anitrend.data.settings.sync.ISyncSettings
import co.anitrend.data.user.settings.IUserSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.ocpsoft.prettytime.PrettyTime

private val coreModule =
    module {
        factory {
            Settings(
                context = androidContext(),
            )
        } binds
            arrayOf(
                ISupportPreference::class,
                IConfigurationSettings::class,
                ILocaleSettings::class,
                IThemeSettings::class,
                IAuthenticationSettings::class,
                IPrivacySettings::class,
                ISortOrderSettings::class,
                IRefreshBehaviourSettings::class,
                ICustomizationSettings::class,
                IPowerSettings::class,
                IConnectivitySettings::class,
                INotificationSettings::class,
                IUnifiedPushSettings::class,
                IUserSettings::class,
                ICacheSettings::class,
                ISyncSettings::class,
                IDeveloperSettings::class,
                IFeatureFlagSetting::class,
            )

        single<ISupportDispatcher> {
            SupportDispatcher()
        }

        single<IStorageController> {
            StorageController()
        }

        factory {
            AniTrendDateHelper()
        } bind AbstractSupportDateHelper::class

        single {
            /** TODO: On language preference change, destroy and recreate [PrettyTime] */
            val localeHelper = get<ILocaleHelper>()
            PrettyTime(localeHelper.locale)
        }

        factory {
            NotificationHelper(
                notificationManager =
                    NotificationManagerCompat
                        .from(androidContext()),
            )
        }
    }

private val configurationModule =
    module {
        single<ILocaleHelper> {
            LocaleHelper(
                settings = get(),
            )
        }

        single<IThemeHelper> {
            ThemeHelper(
                settings = get(),
            )
        }

        factory<IConfigurationHelper> {
            ConfigurationHelper(
                settings = get(),
                localeHelper = get(),
                themeHelper = get(),
            )
        }
    }

private val controllerModule =
    module {
        factory<IPowerController> {
            val context = androidContext()
            AndroidPowerController(
                context = context,
                powerManager = context.systemServiceOf<PowerManager>(),
                connectivityManager = context.systemServiceOf<ConnectivityManager>(),
                settings = get(),
            )
        }

        factory<IShortcutController> {
            val context = androidContext()
            ShortcutController(
                context = context,
                shortcutManager = context.systemServiceOf<ShortcutManager>(),
            )
        }
    }

val androidCoreModules =
    module {
        includes(coreModule, configurationModule, controllerModule)
    }

enum class MarkdownFlavour {
    STANDARD,
    ANILIST,
}
