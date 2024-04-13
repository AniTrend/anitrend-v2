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

package co.anitrend.episode.component.sheet

import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.extension.ext.argument
import co.anitrend.core.android.components.sheet.action.contract.OnSlideAction
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.component.sheet.AniTrendBottomSheet
import co.anitrend.core.extensions.handleViewIntent
import co.anitrend.core.extensions.stackTrace
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import co.anitrend.episode.R
import co.anitrend.episode.component.sheet.viewmodel.EpisodeSheetViewModel
import co.anitrend.episode.databinding.EpisodeSheetBinding
import co.anitrend.navigation.EpisodeRouter
import coil.request.Disposable
import io.noties.markwon.Markwon
import kotlinx.coroutines.launch
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodeSheet(
    private val markwon: Markwon,
    override val inflateLayout: Int = R.layout.episode_sheet
) : AniTrendBottomSheet<EpisodeSheetBinding>() {

    private val viewModel by viewModel<EpisodeSheetViewModel>()

    private val param: EpisodeRouter.EpisodeParam? by argument(EpisodeRouter.EpisodeParam.KEY)

    private var disposable: Disposable? = null

    private val shapeTransformationAction = object : OnSlideAction {
        /**
         * Called when the bottom sheet's [slideOffset] is changed. [slideOffset] will always be a
         * value between -1.0 and 1.0. -1.0 is equal to [BottomSheetBehavior.STATE_HIDDEN], 0.0
         * is equal to [BottomSheetBehavior.STATE_HALF_EXPANDED] and 1.0 is equal to
         * [BottomSheetBehavior.STATE_EXPANDED].
         */
        override fun onSlide(sheet: View, slideOffset: Float) {

        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            onPostModelChange(it)
            viewModel.buildHtml(it, requireContext())
        }
        viewModel.model.observe(viewLifecycleOwner) {
            markwon.setMarkdown(requireBinding().episodeDescription, it)
        }
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this, closeSheetOnBackPressed
        )
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModelState().invoke(
                    EpisodeParam.Detail(
                        requireNotNull(param?.id),
                    ),
                )
            }
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null. This will be called between
     * [onCreate] and [onViewCreated].
     *
     * A default View can be returned by calling [Fragment] in your
     * constructor. Otherwise, this method returns null.
     *
     * It is recommended to **only** inflate the layout in this method and move
     * logic that operates on the returned View to [onViewCreated].
     *
     * If you return a View from here, you will later be called in
     * [onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
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
        binding = EpisodeSheetBinding.bind(requireNotNull(view))
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
        BetterLinkMovementMethod.linkify(Linkify.ALL, activity)
            .setOnLinkClickListener { target, url ->
                runCatching {
                    target.handleViewIntent(url)
                }.stackTrace()
                true
            }
    }

    private fun onPostModelChange(episode: Episode) {
        disposable = requireBinding().episodeThumbnail.using(episode.thumbnail)
        requireBinding().episodeTitle.createSummary(episode)
        requireBinding().episodeDuration.text = episode.about.episodeDuration
        requireBinding().episodePublisher.text = episode.series.seriesPublisher
        requireBinding().episodePlay.setOnClickListener {
            it.handleViewIntent(episode.guid)
        }
        requireBinding().episodePublisher.setOnClickListener {
            Toast.makeText(context, "Open search to find studio", Toast.LENGTH_LONG).show()
        }
        requireBinding().episodeDownload.setOnClickListener {
            Toast.makeText(context, "Pending feature.. might not make it to prod..", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to [Activity.onResume] of the containing
     * Activity's lifecycle.
     */
    override fun onResume() {
        super.onResume()
        bottomSheetCallback.addOnSlideAction(
            shapeTransformationAction
        )
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to [Activity.onPause] of the containing
     * Activity's lifecycle.
     */
    override fun onPause() {
        bottomSheetCallback.removeOnSlideAction(
            shapeTransformationAction
        )
        super.onPause()
    }

    /**
     * Called when the fragment is no longer in use. This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        BetterLinkMovementMethod.linkify(Linkify.ALL, activity)
            .setOnLinkClickListener(null)
        binding?.episodePlay?.setOnClickListener(null)
        binding?.episodePublisher?.setOnClickListener(null)
        binding?.episodeDownload?.setOnClickListener(null)
        disposable?.dispose()
        disposable = null
        super.onDestroy()
    }
}
