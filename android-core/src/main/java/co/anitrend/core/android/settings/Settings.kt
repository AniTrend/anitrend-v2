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

package co.anitrend.core.android.settings

import android.content.Context
import co.anitrend.arch.extension.preference.*
import co.anitrend.arch.extension.preference.contract.ISupportPreference
import co.anitrend.arch.extension.settings.*
import co.anitrend.core.android.R
import co.anitrend.data.arch.settings.connectivity.IConnectivitySettings
import co.anitrend.data.arch.settings.power.IPowerSettings
import co.anitrend.core.android.settings.common.IConfigurationSettings
import co.anitrend.data.arch.settings.cache.ICacheSettings
import co.anitrend.core.android.settings.common.customize.ICustomizationSettings
import co.anitrend.core.android.settings.common.customize.common.PreferredViewMode
import co.anitrend.core.android.settings.common.locale.ILocaleSettings
import co.anitrend.core.android.settings.common.privacy.IPrivacySettings
import co.anitrend.core.android.settings.common.theme.IThemeSettings
import co.anitrend.core.android.settings.helper.locale.AniTrendLocale
import co.anitrend.core.android.settings.helper.theme.AniTrendTheme
import co.anitrend.data.arch.database.settings.IRefreshBehaviourSettings
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings.Companion.INVALID_USER_ID
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.enums.UserTitleLanguage

class Settings(context: Context) : SupportPreference(context),
    IConfigurationSettings, IPrivacySettings, IAuthenticationSettings,
    ISortOrderSettings, IRefreshBehaviourSettings, ICustomizationSettings,
    IPowerSettings, IConnectivitySettings, IUserSettings, ICacheSettings {

    override val locale = EnumSetting(
        key = R.string.settings_configuration_locale,
        default = AniTrendLocale.AUTOMATIC,
        resources = context.resources,
        preference = this
    )

    override val theme = EnumSetting(
        key = R.string.settings_configuration_theme,
        default = AniTrendTheme.SYSTEM,
        resources = context.resources,
        preference = this
    )

    override val isAnalyticsEnabled = BooleanSetting(
        key = R.string.settings_privacy_usage_analytics,
        default = false,
        resources = context.resources,
        preference = this
    )

    override val isCrashlyticsEnabled = BooleanSetting(
        key = R.string.settings_privacy_crash_analytics,
        default = true,
        resources = context.resources,
        preference = this
    )

    override val authenticatedUserId = LongSetting(
        key = R.string.settings_authentication_id,
        default = INVALID_USER_ID,
        resources = context.resources,
        preference = this
    )

    override val isAuthenticated = BooleanSetting(
        key = R.string.settings_is_authenticated,
        default = false,
        resources = context.resources,
        preference = this
    )

    override val sortOrder = EnumSetting(
        key = R.string.settings_sort_order,
        default = SortOrder.DESC,
        resources = context.resources,
        preference = this
    )

    override val isNewInstallation = BooleanSetting(
        key = R.string.settings_is_new_installation,
        default = true,
        resources = context.resources,
        preference = this
    )

    override val versionCode = IntSetting(
        key = R.string.settings_version_code,
        default = 1,
        resources = context.resources,
        preference = this
    )

    override val clearDataOnSwipeRefresh = BooleanSetting(
        key = R.string.settings_clear_on_swipe_refresh,
        default = true,
        resources = context.resources,
        preference = this
    )

    override val preferredViewMode = EnumSetting(
        key = R.string.settings_view_mode_preferred,
        default = PreferredViewMode.COMFORTABLE,
        resources = context.resources,
        preference = this
    )

    override val isDataSaverOn = BooleanSetting(
        key = R.string.settings_data_saver,
        default = false,
        resources = context.resources,
        preference = this
    )

    override val isPowerSaverOn = BooleanSetting(
        key = R.string.settings_power_saver,
        default = false,
        resources = context.resources,
        preference = this
    )

    override val titleLanguage = EnumSetting(
        key = R.string.settings_user_title_language,
        default = UserTitleLanguage.ROMAJI,
        resources = context.resources,
        preference = this
    )

    override val scoreFormat = EnumSetting(
        key = R.string.settings_user_score_format,
        default = ScoreFormat.POINT_10_DECIMAL,
        resources = context.resources,
        preference = this
    )

    override val cacheUsageRatio = FloatSetting(
        key = R.string.settings_cache_usage_ratio,
        default = ICacheSettings.MINIMUM_CACHE_LIMIT,
        resources = context.resources,
        preference = this
    )

    companion object {
        /**
         * Binding types for [Settings]
         */
        internal val BINDINGS = arrayOf(
            ISupportPreference::class, IConfigurationSettings::class,
            ILocaleSettings::class, IThemeSettings::class,
            IAuthenticationSettings::class, IPrivacySettings::class,
            ISortOrderSettings::class, IRefreshBehaviourSettings::class,
            ICustomizationSettings::class, IPowerSettings::class,
            IConnectivitySettings::class, IUserSettings::class,
            ICacheSettings::class
        )
    }
}