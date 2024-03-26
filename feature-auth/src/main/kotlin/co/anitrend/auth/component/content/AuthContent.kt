/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.auth.component.content

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.auth.R
import co.anitrend.auth.component.viewmodel.AuthViewModel
import co.anitrend.auth.databinding.AuthContentBinding
import co.anitrend.auth.model.Authentication
import co.anitrend.auth.presenter.AuthPresenter
import co.anitrend.core.android.extensions.observeOnce
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.core.ui.inject
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class AuthContent(
    private val stateLayoutConfig: StateLayoutConfig,
    override val inflateLayout: Int = R.layout.auth_content
) : AniTrendContent<AuthContentBinding>() {

    private val presenter by inject<AuthPresenter>()

    private val viewModel by activityViewModel<AuthViewModel>()

    private val resetNetworkStateOnBackPress =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val stateFlow = binding?.stateLayout?.loadStateFlow
                if (stateFlow?.value is LoadState.Loading || stateFlow?.value is LoadState.Error)
                    stateFlow.value = LoadState.Idle()
                else
                    closeScreen()
            }
        }

    private fun closeScreen() {
        activity?.finish()
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        super.initializeComponents(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this, resetNetworkStateOnBackPress
        )
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                requireBinding().stateLayout.interactionFlow
                    .debounce(resources.getInteger(co.anitrend.core.android.R.integer.debounce_duration_short).toLong())
                    .onEach {
                        viewModelState().retry()
                    }
                    .catch { cause: Throwable ->
                        Timber.e(cause)
                    }
                    .collect()
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.authenticationFlow
                    .onEach { state ->
                        when (state) {
                            is Authentication.Authenticating -> {
                                requireBinding().stateLayout.loadStateFlow.value = LoadState.Loading()
                                viewModelState().invoke(state)
                            }
                            is Authentication.Error -> {
                                requireBinding().stateLayout.loadStateFlow.value =
                                    LoadState.Error(
                                        RequestError(
                                            topic = state.title,
                                            description = state.message,
                                        ),
                                    )
                            }
                            else -> {
                                Timber.v("Authentication flow state changed: $state")
                            }
                        }
                    }.catch { cause: Throwable ->
                        Timber.e(cause)
                    }.collect()
            }
        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().loadState.observe(viewLifecycleOwner) {
            requireBinding().stateLayout.loadStateFlow.value = it
        }
        viewModelState().model.observeOnce(viewLifecycleOwner) { user ->
            if (user != null) {
                presenter.runSignInWorker(user.id)
                activity?.finish()
            } else {
                Snackbar.make(
                    requireView(),
                    R.string.auth_failed_message,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(co.anitrend.core.R.string.label_text_action_ok) {
                    presenter.runSignOutWorker()
                    closeScreen()
                }.show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AuthContentBinding.bind(view)
        requireBinding().stateLayout.stateConfigFlow.value = stateLayoutConfig
        requireBinding().anonymousControls.anonymousAccount.setOnClickListener {
            presenter.runSignOutWorker()
            closeScreen()
        }
        requireBinding().authorizationControls.authorizationIssues.setOnClickListener {
            presenter.authorizationIssues(requireActivity())
        }
        requireBinding().authorizationControls.authorizationUseAniList.setOnClickListener {
            presenter.authorizeWithAniList(requireActivity())
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state

    /**
     * Called when the fragment is no longer in use. This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        binding?.anonymousControls?.anonymousAccount?.setOnClickListener(null)
        binding?.authorizationControls?.authorizationIssues?.setOnClickListener(null)
        binding?.authorizationControls?.authorizationUseAniList?.setOnClickListener(null)
        super.onDestroy()
    }
}
