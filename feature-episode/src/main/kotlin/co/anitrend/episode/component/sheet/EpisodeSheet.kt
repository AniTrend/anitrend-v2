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
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.ext.argument
import co.anitrend.core.android.components.sheet.SheetBehaviourCallback
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.component.sheet.AniTrendBottomSheet
import co.anitrend.core.extensions.stackTrace
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import co.anitrend.episode.R
import co.anitrend.episode.component.sheet.viewmodel.EpisodeSheetViewModel
import co.anitrend.episode.databinding.EpisodeSheetBinding
import co.anitrend.episode.presenter.EpisodePresenter
import co.anitrend.navigation.EpisodeRouter
import coil.request.Disposable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.noties.markwon.Markwon
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodeSheet(
    private val markwon: Markwon,
    private val presenter: EpisodePresenter,
    override val inflateLayout: Int = R.layout.episode_sheet
) : AniTrendBottomSheet<EpisodeSheetBinding>() {

    private val behavior: BottomSheetBehavior<ConstraintLayout> by lazy(UNSAFE) {
        BottomSheetBehavior.from(requireBinding().container)
    }

    private val viewModel by viewModel<EpisodeSheetViewModel>()

    private val param: EpisodeRouter.Param? by argument(EpisodeRouter.Param.KEY)

    private var disposable: Disposable? = null

    private val closeSheetOnBackPressed =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                dismiss()
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
        lifecycleScope.launchWhenResumed {
            viewModelState().invoke(
                EpisodeParam.Detail(
                    requireNotNull(param?.id)
                )
            )
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
        binding = EpisodeSheetBinding.bind(view)
        BetterLinkMovementMethod.linkify(Linkify.ALL, activity)
            .setOnLinkClickListener { target, url ->
                runCatching {
                    presenter.handleViewIntent(target, url)
                }.stackTrace(moduleTag)
                true
            }

        behavior.addBottomSheetCallback(bottomSheetCallback)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun onPostModelChange(episode: Episode) {
        disposable = requireBinding().episodeThumbnail.using(episode.thumbnail)
        requireBinding().episodeTitle.createSummary(episode)
        requireBinding().episodeDuration.text = episode.about.episodeDuration
        requireBinding().episodePublisher.text = episode.series.seriesPublisher
        requireBinding().episodeThumbnail.setOnClickListener {
            presenter.handleViewIntent(it, episode.guid)
        }
        requireBinding().episodePublisher.setOnClickListener {
            Toast.makeText(context, "Open search to find studio", Toast.LENGTH_LONG).show()
        }
        requireBinding().episodeDownload.setOnClickListener {
            Toast.makeText(context, "Pending feature.. might not make it to prod..", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Called when the fragment is no longer in use. This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        BetterLinkMovementMethod.linkify(Linkify.ALL, activity)
            .setOnLinkClickListener(null)
        binding?.episodePublisher?.setOnClickListener(null)
        binding?.episodeThumbnail?.setOnClickListener(null)
        binding?.episodeDownload?.setOnClickListener(null)
        disposable?.dispose()
        disposable = null
        super.onDestroy()
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}