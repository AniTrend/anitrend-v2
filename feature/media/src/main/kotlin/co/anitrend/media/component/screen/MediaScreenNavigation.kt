/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.media.component.screen

import co.anitrend.common.media.ui.controller.extensions.handleMediaItemNavigation
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.navigation.model.common.IParam

internal fun AniTrendScreen.handleMediaItemNavigation(
    param: IParam,
    settings: IUserSettings,
) {
    handleMediaItemNavigation(
        param = param,
        settings = settings,
    )
}
