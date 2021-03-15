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

package co.anitrend.data.media.model.query

import co.anitrend.data.arch.common.model.graph.IGraphPayload
import co.anitrend.domain.media.model.MediaParam

internal sealed class MediaQuery : IGraphPayload {

    data class Detail(
        val param: MediaParam.Detail
    ) : MediaQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "type" to param.type,
            "scoreFormat" to param.scoreFormat
        )
    }

    data class Find(
        val param: MediaParam.Find
    ) : MediaQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "averageScore" to param.averageScore,
            "averageScore_greater" to param.averageScore_greater,
            "averageScore_lesser" to param.averageScore_lesser,
            "averageScore_not" to param.averageScore_not,
            "chapters" to param.chapters,
            "chapters_greater" to param.chapters_greater,
            "chapters_lesser" to param.chapters_lesser,
            "countryOfOrigin" to param.countryOfOrigin,
            "duration" to param.duration,
            "duration_greater" to param.duration_greater,
            "duration_lesser" to param.duration_lesser,
            "endDate" to param.endDate,
            "endDate_greater" to param.endDate_greater,
            "endDate_lesser" to param.endDate_lesser,
            "endDate_like" to param.endDate_like,
            "episodes" to param.episodes,
            "episodes_greater" to param.episodes_greater,
            "episodes_lesser" to param.episodes_lesser,
            "format" to param.format,
            "format_in" to param.format_in,
            "format_not" to param.format_not,
            "format_not_in" to param.format_not_in,
            "genre" to param.genre,
            "genre_in" to param.genre_in,
            "genre_not_in" to param.genre_not_in,
            "id" to param.id,
            "idMal" to param.idMal,
            "idMal_in" to param.idMal_in,
            "idMal_not" to param.idMal_not,
            "idMal_not_in" to param.idMal_not_in,
            "id_in" to param.id_in,
            "id_not" to param.id_not,
            "id_not_in" to param.id_not_in,
            "isAdult" to param.isAdult,
            "licensedBy" to param.licensedBy,
            "licensedBy_in" to param.licensedBy_in,
            "minimumTagRank" to param.minimumTagRank,
            "onList" to param.onList,
            "popularity" to param.popularity,
            "popularity_greater" to param.popularity_greater,
            "popularity_lesser" to param.popularity_lesser,
            "popularity_not" to param.popularity_not,
            "search" to param.search,
            "season" to param.season,
            "seasonYear" to param.seasonYear,
            "sort" to param.sort,
            "source" to param.source,
            "source_in" to param.source_in,
            "startDate" to param.startDate,
            "startDate_greater" to param.startDate_greater,
            "startDate_lesser" to param.startDate_lesser,
            "startDate_like" to param.startDate_like,
            "status" to param.status,
            "status_in" to param.status_in,
            "status_not" to param.status_not,
            "status_not_in" to param.status_not_in,
            "tag" to param.tag,
            "tagCategory" to param.tagCategory,
            "tagCategory_in" to param.tagCategory_in,
            "tagCategory_not_in" to param.tagCategory_not_in,
            "tag_in" to param.tag_in,
            "tag_not_in" to param.tag_not_in,
            "type" to param.type,
            "volumes" to param.volumes,
            "volumes_greater" to param.volumes_greater,
            "volumes_lesser" to param.volumes_lesser
        )
    }
}
