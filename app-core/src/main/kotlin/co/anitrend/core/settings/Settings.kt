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

package co.anitrend.core.settings

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.content.edit
import co.anitrend.arch.extension.preference.SupportPreference
import co.anitrend.arch.extension.preference.contract.ISupportPreference
import co.anitrend.core.R
import co.anitrend.core.settings.common.IConfigurationSettings
import co.anitrend.core.settings.common.locale.ILocaleSettings
import co.anitrend.core.settings.common.privacy.IPrivacySettings
import co.anitrend.core.settings.common.theme.IThemeSettings
import co.anitrend.core.util.locale.AniTrendLocale
import co.anitrend.core.util.theme.AniTrendTheme
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings.Companion.INVALID_USER_ID
import co.anitrend.data.settings.ISortOrderSettings

class Settings(context: Context) : SupportPreference(context),
    IConfigurationSettings, IPrivacySettings, IAuthenticationSettings,
    ISortOrderSettings {

    override var locale: AniTrendLocale = AniTrendLocale.AUTOMATIC
        get() = AniTrendLocale.valueOf(
            sharedPreferences.getString(
                stringOf(R.string.settings_configuration_locale),
                null
            ) ?: AniTrendLocale.AUTOMATIC.name
        )
        set(value) {
            field = value
            sharedPreferences.edit {
                putString(
                    stringOf(R.string.settings_configuration_locale),
                    value.name
                )
            }
        }

    override var theme: AniTrendTheme = AniTrendTheme.SYSTEM
        get() = AniTrendTheme.valueOf(
            sharedPreferences.getString(
                stringOf(R.string.settings_configuration_theme),
                null
            ) ?: AniTrendTheme.SYSTEM.name
        )
        set(value) {
            field = value
            sharedPreferences.edit {
                putString(
                    stringOf(R.string.settings_configuration_theme),
                    value.name
                )
            }
        }

    override var isAnalyticsEnabled: Boolean = false
        get() = sharedPreferences.getBoolean(
            stringOf(R.string.settings_privacy_usage_analytics),
            false
        )
        set(value) {
            field = value
            sharedPreferences.edit {
                putBoolean(
                    stringOf(R.string.settings_privacy_usage_analytics),
                    value
                )
            }
        }

    override var isCrashlyticsEnabled: Boolean = true
        get() = sharedPreferences.getBoolean(
            stringOf(R.string.settings_privacy_crash_analytics),
            true
        )
        set(value) {
            field = value
            sharedPreferences.edit {
                putBoolean(
                    stringOf(R.string.settings_privacy_crash_analytics),
                    value
                )
            }
        }

    override var authenticatedUserId: Long = INVALID_USER_ID
        get() = sharedPreferences.getLong(
            stringOf(R.string.settings_authentication_id),
            INVALID_USER_ID
        )
        set(value) {
            field = value
            sharedPreferences.edit {
                putLong(
                    stringOf(R.string.settings_authentication_id),
                    value
                )
            }
        }

    override var isAuthenticated: Boolean = false
        get() = sharedPreferences.getBoolean(
            stringOf(R.string.settings_is_authenticated),
            false
        )
        set(value) {
            field = value
            sharedPreferences.edit {
                putBoolean(
                    stringOf(R.string.settings_is_authenticated),
                    value
                )
            }
        }

    override var isSortOrderDescending: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    companion object {
        private fun Settings.stringOf(
            @StringRes resource: Int
        ) = context.getString(resource)

        /**
         * Binding types for [Settings]
         */
        internal val BINDINGS = arrayOf(
            ISupportPreference::class, IConfigurationSettings::class,
            ILocaleSettings::class, IThemeSettings::class,
            IAuthenticationSettings::class, IPrivacySettings::class,
            ISortOrderSettings::class
        )
    }
}