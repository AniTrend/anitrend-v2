package co.anitrend.about.component.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.navigation.model.common.IParam

internal class AboutViewModel : ViewModel() {
    val loadState: MutableLiveData<LoadState> = MutableLiveData(LoadState.Loading())

    operator fun invoke(param: IParam) {
        when (param) {
            is IParam.None -> loadState.postValue(LoadState.Idle())
            else -> Unit
        }
    }
}
