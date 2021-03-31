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

package co.anitrend.data.media.entity.filter

import co.anitrend.data.arch.database.filter.FilterQueryBuilder
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.genre.entity.connection.GenreConnectionEntitySchema
import co.anitrend.data.link.entity.LinkEntitySchema
import co.anitrend.data.media.entity.MediaEntitySchema
import co.anitrend.data.medialist.entity.MediaListEntitySchema
import co.anitrend.data.tag.entity.TagEntitySchema
import co.anitrend.data.tag.entity.connection.TagConnectionEntitySchema
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.support.query.builder.core.criteria.Criteria
import co.anitrend.support.query.builder.core.criteria.extensions.*
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.from.extentions.innerJoin
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.innerJoin
import co.anitrend.support.query.builder.dsl.whereAnd
import java.util.*

internal sealed class MediaQueryFilter<T> : FilterQueryBuilder<T>() {

    class Paged(
        private val authentication: IAuthenticationSettings
    ) : MediaQueryFilter<MediaParam.Find>() {

        private val mediaTable = MediaEntitySchema.tableName.asTable()
        private val mediaListTable = MediaListEntitySchema.tableName.asTable()
        private val linksTable = LinkEntitySchema.tableName.asTable()
        private val tagTable = TagEntitySchema.tableName.asTable()
        private val tagConnectionTable = TagEntitySchema.tableName.asTable()
        private val genreConnectionTable = GenreConnectionEntitySchema.tableName.asTable()

        private fun averageScoreSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.averageScore.asColumn(mediaTable)
            filter.averageScore?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.averageScore_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.averageScore_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
            filter.averageScore_not?.also {
                requireBuilder() whereAnd {
                    column notEqual it
                }
            }
        }

        private fun chapterSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.chapters.asColumn(mediaTable)
            filter.chapters?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.chapters_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.chapters_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
        }

        private fun durationSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.duration.asColumn(mediaTable)
            filter.duration?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.duration_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.duration_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
        }

        private fun endDateSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.endDate.asColumn(mediaTable)
            filter.endDate?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.endDate_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.endDate_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
            filter.endDate_like?.also {
                requireBuilder() whereAnd {
                    // specifying operator manually because the infix applies the % for us
                    Criteria.Operator(column, Criteria.Operator.Type.LIKE, it)
                }
            }
        }

        private fun genreSelection(filter: MediaParam.Find) {
            val column = GenreConnectionEntitySchema.genre.asColumn(genreConnectionTable)
            if (filter.genre != null || filter.genre_in != null || filter.genre_not_in != null)
                requireBuilder() from {
                    innerJoin(genreConnectionTable).on(
                        GenreConnectionEntitySchema.mediaId.asColumn(genreConnectionTable),
                        MediaEntitySchema.id.asColumn(mediaTable)
                    )
                }

            filter.genre?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.genre_in?.also {
                requireBuilder() whereAnd {
                    column `in` it
                }
            }
            filter.genre_not_in?.also {
                requireBuilder() whereAnd {
                    column notIn it
                }
            }
        }

        private fun episodeSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.episodes.asColumn(mediaTable)
            filter.episodes?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.episodes_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.episodes_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
        }

        private fun formatSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.format.asColumn(mediaTable)
            filter.format?.also {
                requireBuilder() whereAnd {
                    column equal it.name
                }
            }
            filter.format_in?.also {
                requireBuilder() whereAnd {
                    column `in` it.map(MediaFormat::name)
                }
            }
            filter.format_not?.also {
                requireBuilder() whereAnd {
                    column equal it.name
                }
            }
            filter.format_not_in?.also {
                requireBuilder() whereAnd {
                    column notIn it.map(MediaFormat::name)
                }
            }
        }

        private fun idSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.id.asColumn(mediaTable)
            filter.id?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.id_in?.also {
                requireBuilder() whereAnd {
                    column `in` it
                }
            }
            filter.id_not?.also {
                requireBuilder() whereAnd {
                    column notEqual it
                }
            }
            filter.id_not_in?.also {
                requireBuilder() whereAnd {
                    column notIn it
                }
            }
        }

        private fun licenseSelection(filter: MediaParam.Find) {
            if (filter.licensedBy != null || filter.licensedBy_in != null)
                requireBuilder() from {
                    innerJoin(linksTable) {
                        on(
                            LinkEntitySchema.mediaId.asColumn(linksTable),
                            MediaEntitySchema.id.asColumn(mediaTable)
                        )
                    }
                } whereAnd {
                    MediaEntitySchema.isLicensed.asColumn(mediaTable).equal(true)
                }
            filter.licensedBy?.also {
                requireBuilder() whereAnd {
                    LinkEntitySchema.site.asColumn(linksTable).like(it.title.toString())
                }
            }
            filter.licensedBy_in?.also {
                requireBuilder() whereAnd {
                    val inCriteria = it.map(MediaLicensor::title)
                    LinkEntitySchema.site.asColumn(linksTable).`in`(inCriteria)
                }
            }
        }

        private fun mediaListSelection(filter: MediaParam.Find) {
            filter.onList?.also {
                if (authentication.isAuthenticated.value) {
                    val userId = authentication.authenticatedUserId.value
                    requireBuilder() from {
                        innerJoin(mediaListTable) {
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
        }

        private fun popularitySelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.popularity
            filter.popularity?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.popularity_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.popularity_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
            filter.popularity_not?.also {
                requireBuilder() whereAnd {
                    column notEqual it
                }
            }
        }

        private fun seasonSelection(filter: MediaParam.Find) {
            filter.season?.also {
                requireBuilder() whereAnd {
                    MediaEntitySchema.season equal it.name
                }
            }
            filter.seasonYear?.also {
                requireBuilder() whereAnd {
                    MediaEntitySchema.startDate startsWith it.toString()
                }
            }
        }

        private fun searchSelection(filter: MediaParam.Find) {
            filter.search?.also {
                requireBuilder() whereAnd {
                    MediaEntitySchema.titleUser_preferred.match(it) or
                            MediaEntitySchema.titleEnglish.match(it) or
                            MediaEntitySchema.titleOriginal.match(it) or
                            MediaEntitySchema.titleRomaji.match(it)
                }
            }
        }

        private fun sourceSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.source.asColumn()
            filter.source?.also {
                requireBuilder() whereAnd {
                    column equal it.name
                }
            }
            filter.source_in?.also {
                requireBuilder() whereAnd {
                    column `in` it.map(MediaSource::name)
                }
            }
        }

        private fun startDateSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.startDate.asColumn(mediaTable)
            filter.startDate?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.startDate_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.startDate_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
            filter.startDate_like?.also {
                requireBuilder() whereAnd {
                    // specifying operator manually because the infix applies the % for us
                    Criteria.Operator(column, Criteria.Operator.Type.LIKE, it)
                }
            }
        }

        private fun statusSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.status.asColumn(mediaTable)
            filter.status?.also {
                requireBuilder() whereAnd {
                    column equal it.name
                }
            }
            filter.status_in?.also {
                requireBuilder() whereAnd {
                    column `in` it.map(MediaStatus::name)
                }
            }
            filter.status_not?.also {
                requireBuilder() whereAnd {
                    column notEqual it.name
                }
            }
            filter.status_not_in?.also {
                requireBuilder() whereAnd {
                    column notIn it.map(MediaStatus::name)
                }
            }
        }

        private fun tagSelection(filter: MediaParam.Find) {
            fun joinTagTablesIfRequired() {
                if (filter.minimumTagRank != null || filter.tagCategory != null ||
                    filter.tagCategory_in != null || filter.tagCategory_not_in != null ||
                    filter.tag != null || filter.tag_in != null || filter.tag_not_in != null
                ) {
                    requireBuilder() from {
                        innerJoin(tagConnectionTable) {
                            on(
                                TagConnectionEntitySchema.mediaId.asColumn(
                                    tagConnectionTable
                                ),
                                MediaEntitySchema.id.asColumn(mediaTable)
                            )
                        }.innerJoin(tagTable) {
                            on(
                                TagEntitySchema.id.asColumn(
                                    tagTable
                                ),
                                TagConnectionEntitySchema.tagId.asColumn(
                                    tagConnectionTable
                                )
                            )
                        }
                    }
                }
            }

            joinTagTablesIfRequired()
            filter.minimumTagRank?.also {
                requireBuilder() whereAnd {
                    TagConnectionEntitySchema.rank.asColumn(
                        tagConnectionTable
                    ) greaterThan it
                }
            }
            filter.tag?.also {
                requireBuilder() whereAnd {
                    TagEntitySchema.name.asColumn(
                        tagTable
                    ) equal it
                }
            }
            filter.tagCategory?.also {
                requireBuilder() whereAnd {
                    TagEntitySchema.category.asColumn(
                        tagTable
                    ) equal it
                }
            }
            filter.tagCategory_in?.also {
                requireBuilder() whereAnd {
                    TagEntitySchema.category.asColumn(
                        tagTable
                    ) `in` it
                }
            }
            filter.tagCategory_not_in?.also {
                requireBuilder() whereAnd {
                    TagEntitySchema.category.asColumn(
                        tagTable
                    ) notIn it
                }
            }
            filter.tag_in?.also {
                requireBuilder() whereAnd {
                    TagEntitySchema.name.asColumn(
                        tagTable
                    ) `in` it
                }
            }
            filter.tag_not_in?.also {
                requireBuilder() whereAnd {
                    TagEntitySchema.name.asColumn(
                        tagTable
                    ) notIn it
                }
            }
        }

        private fun volumeSelection(filter: MediaParam.Find) {
            val column = MediaEntitySchema.volumes.asColumn()
            filter.volumes?.also {
                requireBuilder() whereAnd {
                    column equal it
                }
            }
            filter.volumes_greater?.also {
                requireBuilder() whereAnd {
                    column greaterThan it
                }
            }
            filter.volumes_lesser?.also {
                requireBuilder() whereAnd {
                    column lesserThan it
                }
            }
        }

        private fun selection(filter: MediaParam.Find) {
            filter.isAdult?.also {
                requireBuilder() whereAnd  {
                    MediaEntitySchema.isAdult equal it
                }
            }
            filter.type?.also {
                requireBuilder() whereAnd {
                    MediaEntitySchema.type equal it.name
                }
            }
            filter.countryOfOrigin?.also {
                requireBuilder() whereAnd {
                    MediaEntitySchema.countryOfOrigin equal it
                }
            }
        }

        private fun order(filter: MediaParam.Find) {
            filter.sort?.forEach { sort ->
                when (sort.sortable) {
                    MediaSort.SEARCH_MATCH -> {
                        requireBuilder()
                            .orderBy(MediaEntitySchema.titleUser_preferred.asColumn(mediaTable), sort.order)
                            .orderBy(MediaEntitySchema.titleEnglish.asColumn(mediaTable), sort.order)
                            .orderBy(MediaEntitySchema.titleOriginal.asColumn(mediaTable), sort.order)
                            .orderBy(MediaEntitySchema.titleRomaji.asColumn(mediaTable), sort.order)
                    }
                    else -> {
                        val qualifier = sort.sortable.name.toLowerCase(Locale.ROOT)
                        requireBuilder().orderBy(qualifier.asColumn(mediaTable), sort.order)
                    }
                }
            }
        }

        /**
         * Staring point of the query builder, that should make use of [requireBuilder]
         * to add query objections
         */
        override fun onBuildQuery(filter: MediaParam.Find) {
            requireBuilder() from mediaTable
            selection(filter)
            order(filter)
            averageScoreSelection(filter)
            chapterSelection(filter)
            durationSelection(filter)
            endDateSelection(filter)
            episodeSelection(filter)
            formatSelection(filter)
            genreSelection(filter)
            idSelection(filter)
            licenseSelection(filter)
            mediaListSelection(filter)
            popularitySelection(filter)
            searchSelection(filter)
            seasonSelection(filter)
            sourceSelection(filter)
            startDateSelection(filter)
            statusSelection(filter)
            tagSelection(filter)
            volumeSelection(filter)
        }
    }
}