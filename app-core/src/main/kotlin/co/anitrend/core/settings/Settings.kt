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
import androidx.core.content.edit
import co.anitrend.arch.extension.empty
import co.anitrend.arch.extension.preference.SupportPreference
import co.anitrend.core.settings.common.IConfigurationSettings
import co.anitrend.core.settings.common.privacy.IPrivacySettings
import co.anitrend.core.util.locale.AniTrendLocale
import co.anitrend.core.util.theme.AniTrendTheme

class Settings(context: Context) : SupportPreference(context),
    IConfigurationSettings, IPrivacySettings {

    var authenticatedUserId: Long = INVALID_USER_ID
        get() = sharedPreferences.getLong(AUTHENTICATED_USER, -1)
        set(value) {
            field = value
            sharedPreferences.edit {
                putLong(AUTHENTICATED_USER, value)
            }
        }

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

    companion object  {
        const val INVALID_USER_ID: Long = -1
        private const val updateChannel = "updateChannel"
        private const val AUTHENTICATED_USER = "_authenticatedUser"
    }
}