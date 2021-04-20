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

package co.anitrend.common.episode.ui.widget.summary.controller

import co.anitrend.core.android.controller.widget.WidgetController
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.episode.entity.Episode

internal class EpisodeSummaryController(
    private val entity: Episode
) : WidgetController() {

    private fun seasonNumber(): String {
        val season = entity.series.seriesSeason
        if (!season.isNullOrBlank())
            if (season.length < 2)
                return "0$season"
        return season ?: "--"
    }

    private fun episodeNumber(): String {
        val episode = entity.about.episodeNumber
        if (!episode.isNullOrBlank())
            if (episode.length < 2)
                return "0$episode"
        return episode ?: "00"
    }

    /**
     * @return formatted strings e.g.
     * > S01E02 • Special Ops Squad - Night Before the Counteroffensive (2)
     */
    fun createSummary() = "S${seasonNumber()}E${episodeNumber()} $CHARACTER_SEPARATOR ${entity.about.episodeTitle}"
}