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

package co.anitrend.core.android.koin

import android.content.pm.ShortcutManager
import android.net.ConnectivityManager
import android.os.PowerManager
import co.anitrend.arch.extension.dispatchers.SupportDispatcher
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.util.date.SupportDateHelper
import co.anitrend.arch.extension.util.date.contract.ISupportDateHelper
import co.anitrend.core.android.controller.AndroidPowerController
import co.anitrend.core.android.controller.contract.IPowerController
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.android.settings.helper.config.ConfigurationHelper
import co.anitrend.core.android.settings.helper.config.contract.IConfigurationHelper
import co.anitrend.core.android.settings.helper.locale.LocaleHelper
import co.anitrend.core.android.settings.helper.locale.contract.ILocaleHelper
import co.anitrend.core.android.settings.helper.theme.ThemeHelper
import co.anitrend.core.android.settings.helper.theme.contract.IThemeHelper
import co.anitrend.core.android.shortcut.ShortcutController
import co.anitrend.core.android.shortcut.contract.IShortcutController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.ocpsoft.prettytime.PrettyTime

private val coreModule = module {
    factory {
        Settings(
            context = androidContext()
        )
    } binds (Settings.BINDINGS)

    single<ISupportDispatcher> {
        SupportDispatcher()
    }

    factory<ISupportDateHelper> {
        SupportDateHelper()
    } bind SupportDateHelper::class

    single {
        /** TODO: On language preference change, destroy and recreate [PrettyTime] */
        val localeHelper = get<ILocaleHelper>()
        PrettyTime(localeHelper.locale)
    }
}

private val configurationModule = module {
    single<ILocaleHelper> {
        LocaleHelper(
            settings = get()
        )
    }

    single<IThemeHelper> {
        ThemeHelper(
            settings = get()
        )
    }

    factory<IConfigurationHelper> {
        ConfigurationHelper(
            settings = get(),
            localeHelper = get(),
            themeHelper = get()
        )
    }
}

private val controllerModule = module {
    factory<IPowerController> {
        val context = androidContext()
        AndroidPowerController(
            context = context,
            powerManager = context.systemServiceOf<PowerManager>(),
            connectivityManager = context.systemServiceOf<ConnectivityManager>(),
            connectivitySettings = get(),
            powerSettings = get()
        )
    }

    factory<IShortcutController> {
        val context = androidContext()
        val manager = context.systemServiceOf<ShortcutManager>()
        ShortcutController(
            context = context,
            shortcutManager = manager
        )
    }
}

val androidCoreModules = listOf(coreModule, configurationModule, controllerModule)