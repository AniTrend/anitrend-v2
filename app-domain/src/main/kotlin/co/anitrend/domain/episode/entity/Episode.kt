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

package co.anitrend.domain.episode.entity

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.common.entity.shared.CoverImage

data class Episode(
    override val id: Long,
    val title: String,
    val guid: String,
    val mediaId: Long,
    val description: String?,
    val subtitles: List<String>,
    val series: Series,
    val thumbnail: CoverImage,
    val availability: Availability,
    val about: About
) : IEntity {

    data class Availability(
        val freeTime: Long,
        val premiumTime: Long
    )

    data class About(
        val episodeDuration: String,
        val episodeTitle: String?,
        val episodeNumber: String?
    )

    data class Series(
        val seriesTitle: String,
        val seriesPublisher: String?,
        val seriesSeason: String?,
        val keywords: List<String>,
        val rating: String?
    )

    companion object {
        fun empty() = Episode(
            id = 0,
            title = "",
            guid = "",
            mediaId = 0,
            description = "",
            subtitles = emptyList(),
            series = Series(
                seriesTitle = "",
                seriesPublisher = "",
                seriesSeason = "",
                keywords = emptyList(),
                rating = "",
            ),
            thumbnail = CoverImage(large = null, medium = null),
            availability = Availability(freeTime = 0, premiumTime = 0),
            about = About(
                episodeDuration = "",
                episodeTitle = "",
                episodeNumber = ""
            )
        )
    }
}