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
package co.anitrend.deeplink.component.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.deeplink.exception.DeepLinkException
import co.anitrend.navigation.DeepLinkRouter
import timber.log.Timber

class DeepLinkViewModel : ViewModel() {
    val loadState: MutableLiveData<LoadState> = MutableLiveData(LoadState.Loading())

    operator fun invoke(uri: Uri?): Intent? =
        when (uri) {
            null -> {
                Timber.w(DeepLinkException.MissingIntentData())
                loadState.postValue(LoadState.Error(DeepLinkException.MissingIntentData()))
                null
            }
            else -> {
                val intent = DeepLinkRouter.forMatchingIntent(uri.toString())
                if (intent == null) {
                    Timber.w(DeepLinkException.InvalidScreenIntent())
                    loadState.postValue(LoadState.Error(DeepLinkException.InvalidScreenIntent()))
                } else {
                    loadState.postValue(LoadState.Success())
                }
                intent
            }
        }
}
