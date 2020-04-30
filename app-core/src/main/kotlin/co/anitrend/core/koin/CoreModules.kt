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

package co.anitrend.core.koin

import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import co.anitrend.core.R
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.settings.Settings
import co.anitrend.core.util.config.ConfigurationUtil
import co.anitrend.core.util.locale.LocaleHelper
import co.anitrend.core.util.theme.ThemeHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

private val coreModule = module {
    factory {
        Settings(
            context = androidContext()
        )
    } binds(Settings.BINDINGS)
    single {
        SupportStateLayoutConfiguration(
            loadingDrawable = R.drawable.ic_anitrend_notification_logo,
            errorDrawable = R.drawable.ic_support_empty_state,
            loadingMessage = R.string.label_text_loading,
            retryAction = R.string.label_text_action_retry
        )
    }
    factory {
        ConfigurationUtil(
            settings = get(),
            localeHelper = get(),
            themeHelper = get()
        )
    }
    single {
        SupportDispatchers()
    }
}

private val configurationModule = module {
    single {
        LocaleHelper(
            settings = get()
        )
    }
    single {
        ThemeHelper(
            settings = get()
        )
    }
}

private val presenterModule = module {
    factory {
        CorePresenter(
            androidContext(),
            settings = get()
        )
    }
}


val coreModules = listOf(
    coreModule, configurationModule, presenterModule
)