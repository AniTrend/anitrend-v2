/*
 * Copyright (C) 2021 AniTrend
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
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.navigation.model.common.IParam
import co.anitrend.profile.R
import co.anitrend.profile.component.viewmodel.ProfileViewModel
import co.anitrend.profile.databinding.ProfileContentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileContent(
    private val stateLayoutConfig: StateLayoutConfig,
    override val inflateLayout: Int = R.layout.profile_content,
) : AniTrendContent<ProfileContentBinding>() {
    private val viewModel by viewModel<ProfileViewModel>()

    private val param by argument<ProfileRouter.ProfileParam>(
        key = nameOf<ProfileRouter.ProfileParam>()
    )

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
        super.initializeComponents(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                param?.let(viewModel::invoke)
            }
        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
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
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileContentBinding.bind(view)
        requireBinding().root.setContent {
            AniTrendTheme3 {
                ContentWrapper<IParam>(
                    stateFlow = viewModelState().loadState,
                    config = stateLayoutConfig,
                    onClick = viewModelState()::retry,
                ) {
                    Column {
                    }
                }
            }
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
