package co.anitrend.core.component

import androidx.lifecycle.liveData
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.R

object FeatureUnavailable {
    val loadState = liveData<LoadState> { emit(LoadState.Error(details = NotImplementedError("Feature is currently unavailable"))) }
    val config = StateLayoutConfig(
        loadingDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
        errorDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
        loadingMessage = R.string.label_text_loading,
    )
}

object FeatureReady {
    val loadState = liveData<LoadState> { emit(LoadState.Idle()) }
    val config = StateLayoutConfig(
        loadingDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
        errorDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
        loadingMessage = R.string.label_text_loading,
    )
}
