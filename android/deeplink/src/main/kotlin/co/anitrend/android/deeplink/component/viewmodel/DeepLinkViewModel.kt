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
package co.anitrend.android.deeplink.component.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import co.anitrend.android.deeplink.component.presenter.SplashPresenter
import co.anitrend.android.deeplink.exception.DeepLinkException
import co.anitrend.navigation.DeepLinkRouter
import co.anitrend.navigation.nav3.AniTrendNavKey
import co.anitrend.navigation.nav3.DeepLinkMapper
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class DeepLinkViewModel : ViewModel() {
    val splashState: MutableStateFlow<SplashPresenter.State> = MutableStateFlow(SplashPresenter.State.RUNNING)
    var intentState: Intent? = null
        private set
    var navKey: AniTrendNavKey? = null
        private set

    operator fun invoke(uri: Uri?) {
        when (uri) {
            null -> {
                Timber.w(DeepLinkException.MissingIntentData())
            }
            else -> {
                val intent = DeepLinkRouter.forMatchingIntent(uri.toString())
                if (intent == null) {
                    Timber.w(DeepLinkException.InvalidScreenIntent())
                } else {
                    intentState = intent
                }
                navKey = DeepLinkMapper.resolve(uri)
            }
        }
    }
}
