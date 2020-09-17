/*
 * Copyright (C) 2019  AniTrend
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

import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.media.enums.MediaSource

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 *
 * @property id The id of the media
 * @property averageScore A weighted average score of all the user's scores of the media
 * @property bannerImage The banner image of the media
 * @property chapters The amount of chapters the manga has when complete
 * @property countryOfOrigin Where the media was created. (ISO 3166-1 alpha-2)
 * @property coverImage The cover images of the media
 * @property description Short description of the media's story and characters
 * @property duration The general length of each anime episode in minutes
 * @property endDate The last official release date of the media
 * @property episodes The amount of episodes the anime has when complete
 * @property externalLinks External links to another site related to the media
 * @property favourites The amount of user's who have favourited the media
 * @property format The format the media was released in
 * @property genres The genres of the media
 * @property hashtag Official Twitter hashtags for the media
 * @property idMal The mal id of the media
 * @property isAdult If the media is intended only for 18+ adult audiences
 * @property isFavourite If the media is marked as favourite by the current authenticated user
 * @property isLicensed If the media is officially licensed or a self-published doujin release
 * @property isLocked Locked media may not be added to lists our favorited. This may be due to the entry pending for deletion or other reasons
 * @property meanScore Mean score of all the user's scores of the media
 * @property nextAiringEpisode The media's next episode airing schedule
 * @property popularity The number of users with the media on their list
 * @property rankings The ranking of the media in a particular time span and format compared to other media
 * @property season The year & season the media was initially released in
 * @property siteUrl The url for the media page on the AniList website
 * @property source Source type the media was adapted from
 * @property startDate The first official release date of the media
 * @property status The current releasing status of the media
 * @property synonyms Alternative titles of the media
 * @property tags List of tags that describes elements and themes of the media
 * @property title The official titles of the media in various languages
 * @property trailer Media trailer or advertisement
 * @property trending The amount of related activity in the past hour
 * @property type The type of the media; anime or manga
 * @property updatedAt When the media's data was last updated
 * @property volumes The amount of volumes the manga has when complete
 */
internal interface IMediaModelExtended : IMediaModelCore {
    val countryOfOrigin: CountryCode?
    val description: String?
    val favourites: Int
    val externalLinks: List<MediaModelExtended.ExternalLink>?
    val genres: List<String>?
    val hashTag: String?
    val idMal: Long?
    val isLicensed: Boolean?
    val isLocked: Boolean?
    val popularity: Int?
    val rankings: List<MediaModelExtended.Rank>?
    val siteUrl: String?
    val source: MediaSource?
    val synonyms: List<String>?
    val tags: List<MediaModelExtended.Tag>?
    val trailer: MediaModelExtended.Trailer?
    val trending: Int?
    val updatedAt: Long?
}