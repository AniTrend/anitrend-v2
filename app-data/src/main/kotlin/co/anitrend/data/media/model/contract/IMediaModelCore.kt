/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.media.model.contract

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.data.medialist.model.contract.IMediaListModelCore
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.enums.*

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 *
 * @property id The id of the media
 * @property averageScore A weighted average score of all the user's scores of the media
 * @property bannerImage The banner image of the media
 * @property chapters The amount of chapters the manga has when complete
 * @property coverImage The cover images of the media
 * @property duration The general length of each anime episode in minutes
 * @property endDate The last official release date of the media
 * @property episodes The amount of episodes the anime has when complete
 * @property format The format the media was released in
 * @property isAdult If the media is intended only for 18+ adult audiences
 * @property isFavourite If the media is marked as favourite by the current authenticated user
  * @property meanScore Mean score of all the user's scores of the media
 * @property nextAiringEpisode The media's next episode airing schedule
 * @property season The year & season the media was initially released in
 * @property startDate The first official release date of the media
 * @property status The current releasing status of the media
 * @property title The official titles of the media in various languages
 * @property type The type of the media; anime or manga
 * @property volumes The amount of volumes the manga has when complete
 */
internal interface IMediaModelCore : Identity {
    val title: MediaModelExtended.Title?
    val bannerImage: String?
    val coverImage: MediaModelExtended.CoverImage?
    val type: MediaType?
    val format: MediaFormat?
    val season: MediaSeason?
    val status: MediaStatus?
    val meanScore: Int?
    val averageScore: Int?
    val startDate: FuzzyDate?
    val endDate: FuzzyDate?
    val episodes: Int?
    val duration: Int?
    val chapters: Int?
    val volumes: Int?
    val isAdult: Boolean?
    val isFavourite: Boolean?
    val nextAiringEpisode: AiringScheduleModel?
    val mediaListEntry: IMediaListModelCore?
}