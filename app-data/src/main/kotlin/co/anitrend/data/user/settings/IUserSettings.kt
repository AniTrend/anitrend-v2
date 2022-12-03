/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.user.settings

import co.anitrend.arch.extension.settings.contract.AbstractSetting
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.enums.UserTitleLanguage

interface IUserSettings : IAuthenticationSettings {
    val titleLanguage: AbstractSetting<UserTitleLanguage>
    val scoreFormat: AbstractSetting<ScoreFormat>

    /**
     * Invalidates authentication settings state
     */
    fun invalidateSettings() {
        scoreFormat.value = DEFAULT_SCORE_FORMAT
        titleLanguage.value = DEFAULT_TITLE_LANGUAGE
    }

    companion object {
        val DEFAULT_SCORE_FORMAT = ScoreFormat.POINT_10_DECIMAL
        val DEFAULT_TITLE_LANGUAGE = UserTitleLanguage.ROMAJI
    }
}