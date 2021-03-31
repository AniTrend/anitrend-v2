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

package co.anitrend.media.discover.filter.component.content

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.argument
import co.anitrend.common.genre.databinding.GenreItemBinding
import co.anitrend.core.android.views.text.TextDrawable
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.genre.model.GenreParam
import co.anitrend.media.discover.filter.R
import co.anitrend.media.discover.filter.component.viewmodel.genre.GenreViewModel
import co.anitrend.media.discover.filter.databinding.MediaDiscoverFilterGenreBinding
import co.anitrend.media.discover.filter.extensions.withChip
import co.anitrend.navigation.MediaDiscoverRouter
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class GenreContent(
    override val inflateLayout: Int = R.layout.media_discover_filter_genre
) : AniTrendContent<MediaDiscoverFilterGenreBinding>() {

    private val param by argument(
        MediaDiscoverRouter.Param.KEY,
        MediaDiscoverRouter.Param()
    )

    private val viewModel by viewModel<GenreViewModel>()

    private fun bindModelToViews(entity: Genre): Chip {
        val binding = GenreItemBinding.inflate(
            layoutInflater,
            requireBinding().genresChipGroup,
            false
        )
        val context = binding.root.context
        binding.genre.text = entity.name
        binding.genre.chipIcon = TextDrawable(context, entity.emoji)
        return binding.genre
    }


    private fun initializeViewsWithOptions(genres: List<Genre>) {
        genres.forEach {
            requireBinding().genresChipGroup.addView(
                bindModelToViews(it).withChip {
                    isChecked = when {
                        param.genre != null ->
                            param.genre == it.name
                        param.genre_in != null ->
                            param.genre_in?.contains(it.name) == true
                        else -> false
                    }
                }
            )
        }
    }

    private fun onFetchDataInitialize() {
        if (requireBinding().genresChipGroup.childCount < 1) {
            val param = GenreParam(SortOrder.ASC)
            viewModelState().invoke(param)
        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            initializeViewsWithOptions(it)
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
        binding = MediaDiscoverFilterGenreBinding.bind(view)
        onFetchDataInitialize()
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
