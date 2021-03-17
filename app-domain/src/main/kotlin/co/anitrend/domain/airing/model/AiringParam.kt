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

package co.anitrend.domain.airing.model

import co.anitrend.domain.airing.enums.AiringSort

sealed class AiringParam {

    /** [AiringSchedule query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
     *
     * @param id Filter by the id of the airing schedule item
     * @param mediaId Filter by the id of associated media
     * @param episode Filter by the airing episode number
     * @param airingAt Filter by the time of airing
     * @param notYetAired Filter to episodes that haven't yet aired
     * @param id_not Filter by the id of the airing schedule item
     * @param id_in Filter by the id of the airing schedule item
     * @param id_not_in Filter by the id of the airing schedule item
     * @param mediaId_not Filter by the id of associated media
     * @param mediaId_in Filter by the id of associated media
     * @param mediaId_not_in Filter by the id of associated media
     * @param episode_not Filter by the airing episode number
     * @param episode_in Filter by the airing episode number
     * @param episode_not_in Filter by the airing episode number
     * @param episode_greater Filter by the airing episode number
     * @param episode_lesser Filter by the airing episode number
     * @param airingAt_greater Filter by the time of airing
     * @param airingAt_lesser Filter by the time of airing
     * @param sort The order the results will be returned in
     */
    data class Find(
        var id: Long? = null,
        var mediaId: Long? = null,
        var episode: Int? = null,
        var airingAt: Int? = null,
        var notYetAired: Boolean? = null,
        var id_not: Long? = null,
        var id_in: List<Long>? = null,
        var id_not_in: List<Long>? = null,
        var mediaId_not: Long? = null,
        var mediaId_in: List<Long>? = null,
        var mediaId_not_in: List<Long>? = null,
        var episode_not: Int? = null,
        var episode_in: List<Int>? = null,
        var episode_not_in: List<Int>? = null,
        var episode_greater: Int? = null,
        var episode_lesser: Int? = null,
        var airingAt_greater: Int? = null,
        var airingAt_lesser: Int? = null,
        var sort: List<AiringSort>? = null,
    ) : AiringParam() {
        infix fun builder(param: Find.() -> Unit): Find {
            this.param()
            return this
        }
    }
}
