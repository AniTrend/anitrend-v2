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

package co.anitrend.data.model.query.media

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.usecase.airing.attributes.AiringSort

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
data class AiringScheduleQuery(
    val id: Int? = null,
    val mediaId: Int? = null,
    val episode: Int? = null,
    val airingAt: Int? = null,
    val notYetAired: Boolean? = null,
    val id_not: Int? = null,
    val id_in: List<Int>? = null,
    val id_not_in: List<Int>? = null,
    val mediaId_not: Int? = null,
    val mediaId_in: List<Int>? = null,
    val mediaId_not_in: List<Int>? = null,
    val episode_not: Int? = null,
    val episode_in: List<Int>? = null,
    val episode_not_in: List<Int>? = null,
    val episode_greater: Int? = null,
    val episode_lesser: Int? = null,
    val airingAt_greater: Int? = null,
    val airingAt_lesser: Int? = null,
    val sort: List<AiringSort>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "mediaId" to mediaId,
        "episode" to episode,
        "airingAt" to airingAt,
        "notYetAired" to notYetAired,
        "id_not" to id_not,
        "id_in" to id_in,
        "id_not_in" to id_not_in,
        "mediaId_not" to mediaId_not,
        "mediaId_in" to mediaId_in,
        "mediaId_not_in" to mediaId_not_in,
        "episode_not" to episode_not,
        "episode_in" to episode_in,
        "episode_not_in" to episode_not_in,
        "episode_greater" to episode_greater,
        "episode_lesser" to episode_lesser,
        "airingAt_greater" to airingAt_greater,
        "airingAt_lesser" to airingAt_lesser,
        "sort" to sort
    )
}