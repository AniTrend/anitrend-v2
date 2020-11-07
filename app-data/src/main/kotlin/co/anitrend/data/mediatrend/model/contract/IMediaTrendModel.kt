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

package co.anitrend.data.mediatrend.model.contract

/** [MediaTrend](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrend.doc.html)
 * Media trend contract
 *
 * @property averageScore A weighted average score of all the user's scores of the media
 * @property date The day the data was recorded (timestamp)
 * @property episode The episode number of the anime released on this day
 * @property inProgress The number of users with watching/reading the media
 * @property mediaId The id of the tag
 * @property popularity The number of users with the media on their list
 * @property releasing If the media was being released at this time
 * @property trending The amount of media activity on the day
 */
internal interface IMediaTrendModel {
    val averageScore: Int?
    val date: Long
    val episode: Int?
    val inProgress: Int?
    val mediaId: Long
    val popularity: Int?
    val releasing: Boolean
    val trending: Int
}