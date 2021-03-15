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

package co.anitrend.common.episode.ui.widget.summary

import android.content.Context
import android.util.AttributeSet
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.episode.ui.widget.summary.controller.EpisodeSummaryController
import co.anitrend.domain.episode.entity.Episode
import com.google.android.material.textview.MaterialTextView

class EpisodeSummaryWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    fun createSummary(episode: Episode) {
        val controller =  EpisodeSummaryController(episode)
        text = controller.createSummary()
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (isInEditMode) {
            val episode = Episode.empty().copy(
                series = Episode.Series(
                    seriesTitle = "Attack on Titan",
                    seriesPublisher = "Funimation",
                    seriesSeason = "1",
                    keywords = emptyList(),
                    rating = "noadult",
                ),
                about = Episode.About(
                    episodeDuration = "",
                    episodeTitle = "Special Ops Squad - Night Before the Counteroffensive (2)",
                    episodeNumber = "15"
                )
            )
            createSummary(episode)
        }
    }
}