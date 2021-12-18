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

package co.anitrend.medialist.editor.component.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.toMediaRequestImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.component.sheet.AniTrendBottomSheet
import co.anitrend.domain.media.entity.Media
import co.anitrend.medialist.editor.R
import co.anitrend.medialist.editor.component.sheet.viewmodel.MediaListEditorViewModel
import co.anitrend.medialist.editor.databinding.MediaListEditorContentBinding
import co.anitrend.navigation.MediaListEditorRouter
import coil.request.Disposable
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MediaListEditorContent(
    private val stateConfig: StateLayoutConfig,
    override val inflateLayout: Int = R.layout.media_list_editor_content
) : AniTrendBottomSheet<MediaListEditorContentBinding>() {

    private val disposables = mutableListOf<Disposable>()

    private val viewModel by viewModel<MediaListEditorViewModel>()

    private val param: MediaListEditorRouter.Param? by argument(MediaListEditorRouter.Param.KEY)
    
    private fun onFetchDataInitialize() {
        requireBinding().stateLayout.assureParamNotMissing(param) {
            viewModelState().invoke(requireNotNull(param))
        }
    }

    private fun onPostModelChange(media: Media) {
        disposables.add(
            requireBinding().mediaListBanner.using(
                media.image.toMediaRequestImage(RequestImage.Media.ImageType.BANNER),
                listOf(
                    RoundedCornersTransformation(topLeft = 6f.dp, topRight = 6f.dp),
                )
            )
        )
        disposables.add(
            requireBinding().mediaListPoster.using(
                media.image.toMediaRequestImage(RequestImage.Media.ImageType.POSTER),
                listOf(RoundedCornersTransformation(6f.dp))
            )
        )
        requireBinding().mediaStatusWidget.setBackgroundUsing(media.status)
        requireBinding().mediaTitle.text = media.title.userPreferred
        requireBinding().mediaSubTitleWidget.setUpSubTitle(media)
        requireBinding().mediaScheduleTitleWidget.setUpAiringSchedule(media)
    }

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
        lifecycleScope.launchWhenResumed {
            requireBinding().stateLayout.interactionFlow
                .debounce(resources.getInteger(R.integer.debounce_duration_short).toLong())
                .onEach {
                    viewModelState().retry()
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }
                .collect()
        }
        lifecycleScope.launchWhenStarted {
            onFetchDataInitialize()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, closeSheetOnBackPressed
        )
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            onPostModelChange(it)
        }
        viewModelState().loadState.observe(viewLifecycleOwner) {
            if (viewModelState().model.value == null) {
                requireBinding().stateLayout.loadStateFlow.value = it
            }
            else requireBinding().stateLayout.loadStateFlow.value = LoadState.Idle()
        }
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
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = MediaListEditorContentBinding.bind(requireNotNull(view))
        return view
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
        requireBinding().stateLayout.stateConfigFlow.value = stateConfig
    }

    /**
     * Called when the fragment is no longer in use. This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        disposables.forEach(Disposable::dispose)
        disposables.clear()
        super.onDestroy()
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}