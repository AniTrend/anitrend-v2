/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.profile.component.content

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.navigation.ProfileRouter
import co.anitrend.profile.R
import co.anitrend.profile.component.viewmodel.ProfileViewModel
import co.anitrend.profile.databinding.ProfileContentBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ProfileContent(
    private val stateLayoutConfig: StateLayoutConfig,
    override val inflateLayout: Int = R.layout.profile_content
) : AniTrendContent<ProfileContentBinding>() {

    private val viewModel by viewModel<ProfileViewModel>()

    private val param: ProfileRouter.Param? by argument(ProfileRouter.Param.KEY)

    private fun onFetchDataInitialize() {
        requireBinding().stateLayout.assureParamNotMissing(param) {
            viewModelState().invoke(requireNotNull(param))
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                onFetchDataInitialize()
            }
        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            requireBinding().composeView.setContent {
                AniTrendTheme {

                }
            }
        }
        viewModelState().loadState.observe(viewLifecycleOwner) {
            requireBinding().stateLayout.loadStateFlow.value = it
        }
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.
     *
     * The fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileContentBinding.bind(view)
        requireBinding().stateLayout.stateConfigFlow.value = stateLayoutConfig
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
