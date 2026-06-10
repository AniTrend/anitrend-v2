/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.about.provider

import android.content.Context
import android.content.Intent
import co.anitrend.about.component.screen.AboutScreen
import co.anitrend.navigation.AboutRouter
import co.anitrend.navigation.nav3.AboutNavKey
import co.anitrend.navigation.nav3.Nav3AwareProvider
import co.anitrend.navigation.nav3.NavigationDispatcher
import timber.log.Timber

internal class FeatureProvider(
    private val nav3Dispatcher: NavigationDispatcher?,
) : AboutRouter.Provider,
    Nav3AwareProvider {
    override fun activity(context: Context?) = Intent(context, AboutScreen::class.java)

    override fun navigateViaNav3(): Boolean {
        val dispatcher = nav3Dispatcher ?: return false
        Timber.d("Navigating to About via Nav3")
        dispatcher.navigate(AboutNavKey)
        return true
    }
}
