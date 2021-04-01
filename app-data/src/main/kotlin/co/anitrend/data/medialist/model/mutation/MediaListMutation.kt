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

package co.anitrend.data.medialist.model.mutation

import co.anitrend.data.common.model.date.query.FuzzyDateQuery
import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.medialist.model.MediaListParam

internal sealed class MediaListMutation : IGraphPayload {

    data class Delete(
        val param: MediaListParam.Delete
    ) : MediaListMutation() {
        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id
        )
    }

    data class Save(
        val param: MediaListParam.Save
    ) : MediaListMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "mediaId" to param.mediaId,
            "status" to param.status,
            "score" to param.score,
            "scoreRaw" to param.scoreRaw,
            "progress" to param.progress,
            "progressVolumes" to param.progressVolumes,
            "repeat" to param.repeat,
            "priority" to param.priority,
            "private" to param.private,
            "notes" to param.notes,
            "hiddenFromStatusLists" to param.hiddenFromStatusLists,
            "customLists" to param.customLists,
            "advancedScores" to param.advancedScores,
            "startedAt" to param.startedAt?.let {
                FuzzyDateQuery(it.year, it.month, it.day).toMap()
            },
            "completedAt" to param.completedAt?.let{
                FuzzyDateQuery(it.year, it.month, it.day).toMap()
            }
        )
    }

    data class UpdateMany(
        val param: MediaListParam.UpdateMany
    ) : MediaListMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "status" to param.status,
            "score" to param.score,
            "scoreRaw" to param.scoreRaw,
            "progress" to param.progress,
            "progressVolumes" to param.progressVolumes,
            "repeat" to param.repeat,
            "priority" to param.priority,
            "private" to param.private,
            "notes" to param.notes,
            "hiddenFromStatusLists" to param.hiddenFromStatusLists,
            "advancedScores" to param.advancedScores,
            "startedAt" to param.startedAt?.let {
                FuzzyDateQuery(it.year, it.month, it.day).toMap()
            },
            "completedAt" to param.completedAt?.let{
                FuzzyDateQuery(it.year, it.month, it.day).toMap()
            },
            "ids" to param.ids
        )
    }
}