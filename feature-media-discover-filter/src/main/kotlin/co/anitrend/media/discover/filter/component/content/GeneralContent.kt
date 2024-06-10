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
import co.anitrend.arch.extension.ext.capitalizeWords
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.core.android.extensions.createChipChoice
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.domain.media.enums.MediaCountry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaLicensor
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.media.discover.filter.R
import co.anitrend.media.discover.filter.databinding.MediaDiscoverFilterGeneralBinding
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.nameOf
import kotlinx.coroutines.launch

internal class GeneralContent(
    private val dateHelper: AbstractSupportDateHelper,
    override val inflateLayout: Int = R.layout.media_discover_filter_general
) : AniTrendContent<MediaDiscoverFilterGeneralBinding>() {

    private val param by argument(
        key = nameOf<MediaDiscoverRouter.MediaDiscoverParam>(),
        default = MediaDiscoverRouter::MediaDiscoverParam,
    )

    private fun bindModelToViews() {
        requireBinding().excludeAdultContent.isChecked = param.isAdult == true
        val seasonYear = param.seasonYear
        if (seasonYear != null)
            requireBinding().yearRangeSlider.setValues(seasonYear.toFloat())
        else requireBinding().yearRangeSlider.setValues(dateHelper.getCurrentYear().toFloat())
        when (param.onList) {
            true -> requireBinding().onListChipGroup.check(R.id.onListChipInclude)
            false -> requireBinding().onListChipGroup.check(R.id.onListChipExclude)
            else -> requireBinding().onListChipGroup.clearCheck()
        }
    }

    private fun initializeViewsWithOptions() {
        MediaStatus.entries.forEach {
            requireBinding().statusChipGroup.addView(
                requireContext().createChipChoice {
                    text = it.alias
                    isChecked = param.status_in?.contains(it) == true || param.status == it
                }
            )
        }
        MediaType.entries.forEach {
            requireBinding().mediaTypeChipGroup.addView(
                requireContext().createChipChoice {
                    text = it.alias
                    isChecked = param.type == it
                }
            )
        }
        MediaSeason.entries.forEach {
            requireBinding().seasonChipGroup.addView(
                requireContext().createChipChoice {
                    text = it.alias
                    isChecked = param.season == it
                }
            )
        }
        MediaFormat.entries.forEach {
            requireBinding().formatChipGroup.addView(
                requireContext().createChipChoice {
                    text = it.alias
                    isChecked = param.format_in?.contains(it) == true || param.format == it
                }
            )
        }
        MediaSource.entries.forEach {
            requireBinding().sourceChipGroup.addView(
                requireContext().createChipChoice {
                    text = it.alias
                    isChecked = param.source_in?.contains(it) == true || param.source == it
                }
            )
        }
        MediaCountry.entries.forEach {
            requireBinding().countryChipGroup.addView(
                requireContext().createChipChoice {
                    text = it.name.lowercase().capitalizeWords()
                    isChecked = param.countryOfOrigin == it.alias
                }
            )
        }
        MediaLicensor.entries.forEach {
            requireBinding().streamingChipGroup.addView(
                requireContext().createChipChoice {
                    text = it.title
                    isChecked = param.licensedBy_in?.contains(it) == true || param.licensedBy == it
                }
            )
        }
        requireBinding().yearRangeSlider.valueTo = dateHelper.getCurrentYear(2).toFloat()
        requireBinding().yearRangeSlider.valueFrom = 1970f
        requireBinding().yearRangeSlider.stepSize = 1f
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MediaDiscoverFilterGeneralBinding.bind(view)
        lifecycleScope.launch {
            initializeViewsWithOptions()
            bindModelToViews()
        }
    }
}
