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

package co.anitrend.app.navigation

import android.content.Context
import co.anitrend.app.ui.main.MainScreen
import co.anitrend.arch.extension.startNewActivity
import co.anitrend.core.navigation.INavigationController

internal class NavigationController : INavigationController {

    override fun navigateToMain(context: Context) {
        context.startNewActivity<MainScreen>()
    }

    override fun navigateToSettings(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}