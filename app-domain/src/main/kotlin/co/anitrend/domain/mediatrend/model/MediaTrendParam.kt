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

package co.anitrend.domain.mediatrend.model

import co.anitrend.domain.common.DateInt
import co.anitrend.domain.media.enums.MediaTrendSort

sealed class MediaTrendParam {

    /** [MediaTrend query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
     *
     * @param mediaId Filter by the media id
     * @param date Filter by date
     * @param trending Filter by trending amount
     * @param averageScore Filter by score
     * @param popularity Filter by popularity
     * @param episode Filter by episode number
     * @param releasing Filter to stats recorded while the media was releasing
     * @param mediaId_not Filter by the media id
     * @param mediaId_in Filter by the media id
     * @param mediaId_not_in Filter by the media id
     * @param date_greater Filter by date
     * @param date_lesser Filter by date
     * @param trending_greater Filter by trending amount
     * @param trending_lesser Filter by trending amount
     * @param trending_not Filter by trending amount
     * @param averageScore_greater Filter by score
     * @param averageScore_lesser Filter by score
     * @param averageScore_not Filter by score
     * @param popularity_greater Filter by popularity
     * @param popularity_lesser Filter by popularity
     * @param popularity_not Filter by popularity
     * @param episode_greater Filter by episode number
     * @param episode_lesser Filter by episode number
     * @param episode_not Filter by episode number
     * @param sort The order the results will be returned in
     */
    data class Find(
        val mediaId: Int? = null,
        val date: DateInt? = null,
        val trending: Int? = null,
        val averageScore: Int? = null,
        val popularity: Int? = null,
        val episode: Int? = null,
        val releasing: Boolean? = null,
        val mediaId_not: Int? = null,
        val mediaId_in: List<Int>? = null,
        val mediaId_not_in: List<Int>? = null,
        val date_greater: DateInt? = null,
        val date_lesser: DateInt? = null,
        val trending_greater: Int? = null,
        val trending_lesser: Int? = null,
        val trending_not: Int? = null,
        val averageScore_greater: Int? = null,
        val averageScore_lesser: Int? = null,
        val averageScore_not: Int? = null,
        val popularity_greater: Int? = null,
        val popularity_lesser: Int? = null,
        val popularity_not: Int? = null,
        val episode_greater: Int? = null,
        val episode_lesser: Int? = null,
        val episode_not: Int? = null,
        val sort: List<MediaTrendSort>? = null
    ): MediaTrendParam() {
        infix fun builder(param: Find.() -> Unit): Find {
            this.param()
            return this
        }
    }
}
