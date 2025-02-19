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
package co.anitrend.media.discover.filter.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.android.views.compose.composable
import co.anitrend.core.component.sheet.compose.AniTrendSheetComposition
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.genre.model.GenreParam
import co.anitrend.domain.tag.model.TagParam
import co.anitrend.media.discover.filter.component.compose.MediaFilterSheetScreen
import co.anitrend.media.discover.filter.component.viewmodel.genre.GenreViewModel
import co.anitrend.media.discover.filter.component.viewmodel.tag.TagViewModel
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.extensions.nameOf
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class MediaFilterContent(
    private val dateHelper: AbstractSupportDateHelper,
) : AniTrendSheetComposition() {
    private val param by argument(
        key = nameOf<MediaDiscoverRouter.MediaDiscoverParam>(),
        default = MediaDiscoverRouter::MediaDiscoverParam,
    )

    private val genreViewModel by viewModel<GenreViewModel>()
    private val tagViewModel by viewModel<TagViewModel>()

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            closeSheetOnBackPressed,
        )
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
    ): View =
        composable(requireActivity()) {
            AniTrendTheme3 {
                Surface {
                    LaunchedEffect(Unit) {
                        genreViewModel(GenreParam(SortOrder.DESC))
                        tagViewModel(TagParam(SortOrder.ASC))
                    }
                    MediaFilterSheetScreen(
                        dateHelper = dateHelper,
                        param = param,
                        onParamChange = {
                            childFragmentManager.setFragmentResult(
                                MediaDiscoverFilterRouter.RESULT_LISTENER_KEY,
                                it.asBundle(),
                            )
                        },
                        onDismiss = { dismiss() },
                    )
                }
            }
        }
}
