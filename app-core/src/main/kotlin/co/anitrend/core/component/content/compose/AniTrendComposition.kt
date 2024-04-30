package co.anitrend.core.component.content.compose

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.extension.network.contract.ISupportConnectivity
import co.anitrend.arch.extension.network.model.ConnectivityState
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.core.ui.inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.component.KoinScopeComponent
import timber.log.Timber

abstract class AniTrendComposition : SupportFragment(), AndroidScopeComponent, KoinScopeComponent {

    override val scope by fragmentScope()

    private val connectivity by inject<ISupportConnectivity>()

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * **N.B.** Calling super of this will register a connectivity change listener, so only
     * call `super.initializeComponents` if you require this behavior
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                connectivity.connectivityStateFlow
                    .onEach { state ->
                        Timber.v("Connectivity state changed: $state")
                        if (state == ConnectivityState.Connected) viewModelState()?.retry()
                    }
                    .catch { cause ->
                        Timber.w(cause, "While collecting connectivity state")
                    }
                    .collect()
            }
        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {}

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): AniTrendViewModelState<*>? = null
}
