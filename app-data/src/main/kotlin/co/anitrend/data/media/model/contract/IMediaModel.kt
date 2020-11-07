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

import co.anitrend.data.airing.model.contract.IAiringScheduleModel
import co.anitrend.data.arch.CountryCode
import co.anitrend.data.arch.common.model.date.contract.IFuzzyDateModel
import co.anitrend.data.medialist.model.contract.IMediaListModel
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.media.enums.*

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 *
 * @property id The id of the media
 * @property idMal The mal id of the media
 * @property bannerImage The banner image of the media
 * @property type The type of the media; anime or manga
 * @property format The format the media was released in
 * @property season The year & season the media was initially released in
 * @property status The current releasing status of the media
 * @property source Source type the media was adapted from.
 * @property meanScore Mean score of all the user's scores of the media
 * @property description Short description of the media's story and characters
 * @property averageScore A weighted average score of all the user's scores of the media
 * @property countryOfOrigin Where the media was created. (ISO 3166-1 alpha-2)
 * @property trending The amount of related activity in the past hour
 * @property popularity The number of users with the media on their list
 * @property synonyms Alternative titles of the media
 * @property favourites If the media is marked as favourite by the current authenticated user
 * @property hashTag Official Twitter hash tags for the media
 * @property siteUrl The url for the media page on the AniList website
 * @property startDate The first official release date of the media
 * @property endDate The last official release date of the media
 * @property episodes The amount of episodes the anime has when complete
 * @property duration The general length of each anime episode in minutes
 * @property chapters The amount of chapters the manga has when complete
 * @property volumes The amount of volumes the manga has when complete
 * @property isAdult If the media is intended only for 18+ adult audiences
 * @property isFavourite If the media is marked as favourite by the current authenticated user
 * @property updatedAt When the media's data was last updated
 * @property nextAiringEpisode The media's next episode airing schedule
 * @property mediaListEntry The authenticated user's media list entry for the media
 */
internal interface IMediaModel : Identity {
    val idMal: Long?
    val bannerImage: String?
    val type: MediaType
    val format: MediaFormat?
    val season: MediaSeason?
    val status: MediaStatus?
    val source: MediaSource?
    val meanScore: Int?
    val description: String?
    val averageScore: Int?
    val countryOfOrigin: CountryCode?
    val trending: Int?
    val popularity: Int?
    val synonyms: List<String>
    val favourites: Int
    val hashTag: String?
    val siteUrl: String
    val startDate: IFuzzyDateModel?
    val endDate: IFuzzyDateModel?
    val episodes: Int?
    val duration: Int?
    val chapters: Int?
    val volumes: Int?
    val isAdult: Boolean?
    val isFavourite: Boolean
    val updatedAt: Long?
    val nextAiringEpisode: IAiringScheduleModel?
    val mediaListEntry: IMediaListModel?
}