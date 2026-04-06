/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.common.episode.ui.compose

import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.episode.entity.Episode

internal val PREVIEW_EPISODE =
    Episode(
        id = 1L,
        title = "S01E02 • Special Ops Squad - Night Before the Counteroffensive (2)",
        guid = "",
        mediaId = 642191L,
        description =
            "After the Inquiry Eren is assigned to the Survey Team's special operations squad, known as" +
                " \"Squad Levi.\" The Squad is composed of the best troops the survey team has, but they're all very strange people. " +
                "With a major mission 30 days away, Eren hears from Hanji about the experiments on the titans from Trost.",
        subtitles = emptyList(),
        series =
            Episode.Series(
                seriesTitle = "Attack on Titan",
                seriesPublisher = "Funimation",
                seriesSeason = "S01",
                keywords = emptyList(),
                rating = "",
            ),
        thumbnail = CoverImage(large = "", medium = ""),
        availability = Episode.Availability(freeTime = 0, premiumTime = 0),
        about =
            Episode.About(
                episodeDuration = "23:46",
                episodeTitle = "Special Ops Squad",
                episodeNumber = "E02",
            ),
    )
