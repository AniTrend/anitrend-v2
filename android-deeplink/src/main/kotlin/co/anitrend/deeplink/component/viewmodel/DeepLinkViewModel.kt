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

    operator fun invoke(uri: Uri?): Intent? = when (uri) {
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
