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
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.auth.R
import co.anitrend.auth.component.viewmodel.AuthViewModel
import co.anitrend.auth.databinding.AuthContentBinding
import co.anitrend.auth.presenter.AuthPresenter
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.core.ui.inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class AuthContent(
    private val stateLayoutConfig: StateLayoutConfig,
    override val inflateLayout: Int = R.layout.auth_content
) : AniTrendContent<AuthContentBinding>() {

    private val presenter by inject<AuthPresenter>()

    private val viewModel by sharedViewModel<AuthViewModel>()

    private val resetNetworkStateOnBackPress =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val stateFlow = binding?.stateLayout?.loadStateFlow
                if (stateFlow?.value is LoadState.Loading || stateFlow?.value is LoadState.Error)
                    stateFlow.value = LoadState.Idle()
                else
                    activity?.finish()
            }
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
        lifecycleScope.launchWhenResumed {
            requireBinding().stateLayout.interactionFlow.filterNotNull()
                .debounce(resources.getInteger(R.integer.debounce_duration_short).toLong())
                .onEach {
                    viewModelState().retry()
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }
                .collect()
        }
        lifecycleScope.launchWhenResumed {
            viewModel.state.authenticationFlow
                .onEach { state ->
                    presenter.onStateChange(
                        state,
                        viewModelState(),
                        requireBinding().stateLayout,
                    )
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }
                .collect()
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
        viewModelState().model.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                presenter.scheduleAuthenticationBasedTasks()
                activity?.finish()
            } else {
                // TODO: Inform the user auth was successful but account could not be fetched
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AuthContentBinding.bind(view)
        requireBinding().stateLayout.stateConfigFlow.value = stateLayoutConfig
        requireBinding().anonymousControls.anonymousAccount.setOnClickListener {
            lifecycleScope.launch {
                presenter.useAnonymousAccount(requireActivity())
            }
        }
        requireBinding().authorizationControls.authorizationIssues.setOnClickListener {
            presenter.authorizationIssues(requireActivity())
        }
        requireBinding().authorizationControls.authorizationUseAniList.setOnClickListener {
            presenter.authorizeWithAniList(requireActivity(), viewModelState())
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