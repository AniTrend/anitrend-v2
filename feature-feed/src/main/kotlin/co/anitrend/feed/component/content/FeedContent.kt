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
package co.anitrend.feed.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.FeatureUnavailable
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.feed.R
import co.anitrend.feed.component.viewmodel.FeedViewModel
import co.anitrend.feed.databinding.FeedContentBinding
import co.anitrend.navigation.model.common.IParam
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedContent(
    override val inflateLayout: Int = R.layout.feed_content,
) : AniTrendContent<FeedContentBinding>() {
    private val viewModel by viewModel<FeedViewModel>()

    private fun onFetchDataInitialize() {
        // TODO: Implement functionality
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and
     * non-graphical fragments can return null. This will be called between
     * [onCreate] & [onActivityCreated].
     *
     * A default View can be returned by calling [Fragment] in your
     * constructor. Otherwise, this method returns null.
     *
     * It is recommended to __only__ inflate the layout in this method and move
     * logic that operates on the returned View to [onViewCreated].
     *
     * If you return a View from here, you will later be called in [onDestroyView]
     * when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     * attached to. The fragment should not add the view itself, but this can be used to generate
     * the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = FeedContentBinding.bind(requireNotNull(view))
        requireBinding().root.setContent {
            AniTrendTheme3 {
                ContentWrapper<IParam>(
                    stateFlow = FeatureUnavailable.loadState,
                    config = FeatureUnavailable.config,
                    onClick = {},
                ) {}
            }
        }
        return view
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
