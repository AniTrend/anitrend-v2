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

package co.anitrend.data.airing.entity.filter

import co.anitrend.data.airing.entity.AiringScheduleEntitySchema
import co.anitrend.data.arch.database.filter.FilterQueryBuilder
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.media.entity.MediaEntitySchema
import co.anitrend.data.medialist.entity.MediaListEntitySchema
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.support.query.builder.core.criteria.extensions.*
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.from.extentions.innerJoin
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.leftJoin
import co.anitrend.support.query.builder.dsl.whereAnd
import org.threeten.bp.Instant
import java.util.*

internal sealed class AiringQueryFilter<T> : FilterQueryBuilder<T>() {
    
    class Paged(
        private val authentication: IAuthenticationSettings
    ) : AiringQueryFilter<AiringParam.Find>() {

        private val mediaTable = MediaEntitySchema.tableName.asTable()
        private val airingTable = AiringScheduleEntitySchema.tableName.asTable()
        private val mediaListTable = MediaListEntitySchema.tableName.asTable()

        private fun airingSelection(filter: AiringParam.Find) {
            filter.airingAt?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.airingAt.asColumn(airingTable) equal it
                }
            }
            filter.airingAt_greater?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.airingAt.asColumn(airingTable) greaterThan  it
                }
            }
            filter.airingAt_lesser?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.airingAt.asColumn(airingTable) lesserThan  it
                }
            }
            filter.notYetAired?.also {
                val currentTime = Instant.now().epochSecond / 1000
                requireBuilder() whereAnd {
                    if (it)
                        AiringScheduleEntitySchema.airingAt.asColumn(airingTable) greaterThan currentTime
                    else
                        AiringScheduleEntitySchema.airingAt.asColumn(airingTable) lesserThan currentTime
                }
            }
        }

        private fun episodeSelection(filter: AiringParam.Find) {
            filter.episode?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.episode.asColumn(airingTable) equal it
                }
            }
            filter.episode_not?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.episode.asColumn(airingTable) notEqual it
                }
            }
            filter.episode_in?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.episode.asColumn(airingTable) `in` it
                }
            }
            filter.episode_not_in?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.episode.asColumn(airingTable) notIn it
                }
            }
            filter.episode_greater?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.episode.asColumn(airingTable) greaterThan it
                }
            }
            filter.episode_lesser?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.episode.asColumn(airingTable) lesserThan it
                }
            }
        }

        private fun mediaSelection(filter: AiringParam.Find) {
            filter.mediaId?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.mediaId.asColumn(airingTable) equal it
                }
            }
            filter.mediaId_not?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.mediaId.asColumn(airingTable) notEqual it
                }
            }
            filter.mediaId_in?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.mediaId.asColumn(airingTable) `in` it
                }
            }
            filter.mediaId_not_in?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.mediaId.asColumn(airingTable) notIn it
                }
            }
        }

        private fun selection(filter: AiringParam.Find) {
            filter.id?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.id.asColumn(airingTable) equal it
                }
            }
            filter.id_not?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.id.asColumn(airingTable) notEqual it
                }
            }
            filter.id_in?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.id.asColumn(airingTable) notIn it
                }
            }
            filter.id_not_in?.also {
                requireBuilder() whereAnd {
                    AiringScheduleEntitySchema.id.asColumn(airingTable) `in` it
                }
            }
        }

        private fun mediaListSelection() {
            if (authentication.isAuthenticated.value) {
                val userId = authentication.authenticatedUserId.value
                requireBuilder() from {
                    leftJoin(mediaListTable) {
                        on(
                            MediaListEntitySchema.mediaId.asColumn(mediaListTable),
                            MediaEntitySchema.id.asColumn(mediaTable)
                        )
                    }
                } whereAnd {
                    MediaListEntitySchema.userId.asColumn(mediaListTable) equal userId
                }
            }
        }

        private fun order(filter: AiringParam.Find) {
            filter.sort?.forEach { sort ->
                when (sort.sortable) {
                    AiringSort.TIME -> requireBuilder().orderBy(
                        AiringScheduleEntitySchema.timeUntilAiring.asColumn(airingTable), sort.order
                    )
                    else -> {
                        val qualifier = sort.sortable.name.toLowerCase(Locale.ROOT)
                        requireBuilder().orderBy(qualifier.asColumn(airingTable), sort.order)
                    }
                }
            }
        }

        /**
         * Staring point of the query builder, that should make use of [requireBuilder]
         * to add query objections
         */
        override fun onBuildQuery(filter: AiringParam.Find) {
            requireBuilder() from mediaTable.innerJoin(airingTable) {
                on(
                    MediaEntitySchema.id.asColumn(mediaTable),
                    AiringScheduleEntitySchema.mediaId.asColumn(airingTable)
                )
            }
            order(filter)
            selection(filter)
            mediaSelection(filter)
            episodeSelection(filter)
            airingSelection(filter)
            mediaListSelection()
        }
    }
}