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
import co.anitrend.core.R
import co.anitrend.core.settings.common.IConfigurationSettings
import co.anitrend.core.settings.common.privacy.IPrivacySettings
import co.anitrend.core.util.locale.AniTrendLocale
import co.anitrend.core.util.theme.AniTrendTheme
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings.Companion.INVALID_USER_ID
import co.anitrend.data.settings.ISortOrderSettings

class Settings(context: Context) : SupportPreference(context),
    IConfigurationSettings, IPrivacySettings, IAuthenticationSettings,
    ISortOrderSettings {

    override var locale: AniTrendLocale
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override var theme: AniTrendTheme
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override var isAnalyticsEnabled: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override var isCrashlyticsEnabled: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

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
        private fun Settings.stringOf(@StringRes resource: Int) =
            context.getString(resource)

        /**
         * Binding types [Settings]
         */
        internal val settingsBindings = arrayOf(
            SupportPreference::class, IConfigurationSettings::class,
            IPrivacySettings::class, IAuthenticationSettings::class,
            ISortOrderSettings::class
        )
    }
}