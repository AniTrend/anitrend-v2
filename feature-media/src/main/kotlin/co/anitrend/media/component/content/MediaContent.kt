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

package co.anitrend.media.component.content

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.media.R
import co.anitrend.media.component.compose.MediaDetailComponent
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.media.databinding.MediaContentBinding
import co.anitrend.navigation.MediaRouter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MediaContent(
    private val stateLayoutConfig: StateLayoutConfig,
    override val inflateLayout: Int = R.layout.media_content
) : AniTrendContent<MediaContentBinding>() {

    private val viewModel by viewModel<MediaViewModel>()

    private val param: MediaRouter.Param? by argument(MediaRouter.Param.KEY)

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
        viewModelState().loadState.observe(viewLifecycleOwner) {
            if (viewModelState().model.value == null)
                requireBinding().stateLayout.loadStateFlow.value = it
            else requireBinding().stateLayout.loadStateFlow.value = LoadState.Idle()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MediaContentBinding.bind(view)
        requireBinding().composeView.setContent {
            MediaDetailComponent(viewModelState())
        }
        requireBinding().stateLayout.stateConfigFlow.value = stateLayoutConfig
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
