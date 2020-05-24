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
import co.anitrend.arch.extension.preference.*
import co.anitrend.arch.extension.preference.contract.ISupportSettings
import co.anitrend.core.R
import co.anitrend.core.settings.common.IConfigurationSettings
import co.anitrend.core.settings.common.customize.ICustomizationSettings
import co.anitrend.core.settings.common.customize.common.PreferredViewMode
import co.anitrend.core.settings.common.locale.ILocaleSettings
import co.anitrend.core.settings.common.privacy.IPrivacySettings
import co.anitrend.core.settings.common.theme.IThemeSettings
import co.anitrend.core.util.locale.AniTrendLocale
import co.anitrend.core.util.theme.AniTrendTheme
import co.anitrend.data.arch.database.settings.IRefreshBehaviourSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings.Companion.INVALID_USER_ID
import co.anitrend.data.arch.database.settings.ISortOrderSettings

class Settings(context: Context) : SupportSettings(context),
    IConfigurationSettings, IPrivacySettings, IAuthenticationSettings,
    ISortOrderSettings, IRefreshBehaviourSettings, ICustomizationSettings {

    override var locale by EnumPreference(
        R.string.settings_configuration_locale,
        AniTrendLocale.AUTOMATIC,
        context.resources
    )

    override var theme by EnumPreference(
        R.string.settings_configuration_theme,
        AniTrendTheme.SYSTEM,
        context.resources
    )

    override var isAnalyticsEnabled by BooleanPreference(
        R.string.settings_privacy_usage_analytics,
        false,
        context.resources
    )

    override var isCrashlyticsEnabled by BooleanPreference(
        R.string.settings_privacy_crash_analytics,
        true,
        context.resources
    )

    override var authenticatedUserId by LongPreference(
        R.string.settings_authentication_id,
        INVALID_USER_ID,
        context.resources
    )

    override var isAuthenticated by BooleanPreference(
        R.string.settings_is_authenticated,
        false,
        context.resources
    )

    override var isSortOrderDescending by BooleanPreference(
        R.string.settings_is_sort_order_desc,
        false,
        context.resources
    )

    override var isNewInstallation by BooleanPreference(
        R.string.settings_is_new_installation,
        true,
        context.resources
    )

    override var versionCode by IntPreference(
        R.string.settings_version_code,
        1,
        context.resources
    )

    override var clearDataOnSwipeRefresh by BooleanPreference(
        R.string.settings_clear_on_swipe_refresh,
        true,
        context.resources
    )

    override var preferredViewMode by EnumPreference(
        R.string.settings_view_mode_preferred,
        PreferredViewMode.GRID_LIST,
        context.resources
    )

    companion object {

        /**
         * Binding types for [Settings]
         */
        internal val BINDINGS = arrayOf(
            ISupportSettings::class, IConfigurationSettings::class,
            ILocaleSettings::class, IThemeSettings::class,
            IAuthenticationSettings::class, IPrivacySettings::class,
            ISortOrderSettings::class, IRefreshBehaviourSettings::class,
            ICustomizationSettings::class
        )
    }
}