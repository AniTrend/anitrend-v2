/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.core.presenter

import android.content.Context
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.core.android.settings.Settings
import co.anitrend.data.auth.settings.IAuthenticationSettings

abstract class CorePresenter(
    context: Context,
    settings: Settings,
) : SupportPresenter<Settings>(context, settings) {
    /**
     * Checks the receiver is the current user
     */
    open fun isCurrentUser(userId: Long): Boolean {
        val authUserId = settings.authenticatedUserId.value
        if (authUserId == IAuthenticationSettings.INVALID_USER_ID) {
            return false
        }
        return authUserId == userId
    }
}
