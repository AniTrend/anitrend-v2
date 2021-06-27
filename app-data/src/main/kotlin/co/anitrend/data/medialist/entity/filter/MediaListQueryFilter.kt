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

package co.anitrend.data.medialist.entity.filter

import co.anitrend.data.android.filter.FilterQueryBuilder
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.customlist.entity.CustomListEntitySchema
import co.anitrend.data.media.entity.MediaEntitySchema
import co.anitrend.data.medialist.entity.MediaListEntitySchema
import co.anitrend.domain.medialist.enums.MediaListSort
import co.anitrend.domain.medialist.model.MediaListParam
import co.anitrend.support.query.builder.core.criteria.extensions.equal
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.from.extentions.innerJoin
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.whereAnd
import java.util.*

internal sealed class MediaListQueryFilter<T : MediaListParam.Entries> : FilterQueryBuilder<T>() {

    protected val customListTable = CustomListEntitySchema.tableName.asTable()
    protected val mediaListTable = MediaListEntitySchema.tableName.asTable()
    protected val mediaTable = MediaEntitySchema.tableName.asTable()

    protected abstract val authentication: IAuthenticationSettings

    protected open fun selection(filter: T) {
        requireBuilder() whereAnd {
            MediaListEntitySchema.mediaType.asColumn(mediaListTable) equal filter.type.name
        }

        filter.status?.run {
            requireBuilder() whereAnd {
                MediaListEntitySchema.status.asColumn(mediaListTable) equal name
            }
        }
    }

    protected fun order(filter: T) {
        filter.sort?.forEach { sort ->
            when (sort.sortable) {
                MediaListSort.ADDED_TIME ->
                    requireBuilder().orderBy(
                        MediaListEntitySchema.createdAt.asColumn(mediaListTable),
                        sort.order
                    )
                MediaListSort.FINISHED_ON ->
                    requireBuilder().orderBy(
                        MediaListEntitySchema.completedAt.asColumn(mediaListTable),
                        sort.order
                    )
                MediaListSort.MEDIA_POPULARITY ->
                    requireBuilder().orderBy(
                        MediaEntitySchema.popularity.asColumn(mediaTable),
                        sort.order
                    )
                MediaListSort.MEDIA_TITLE_ENGLISH ->
                    requireBuilder().orderBy(
                        MediaEntitySchema.titleEnglish.asColumn(mediaTable),
                        sort.order
                    )
                MediaListSort.MEDIA_TITLE_NATIVE ->
                    requireBuilder().orderBy(
                        MediaEntitySchema.titleOriginal.asColumn(mediaTable),
                        sort.order
                    )
                MediaListSort.MEDIA_TITLE_ROMAJI ->
                    requireBuilder().orderBy(
                        MediaEntitySchema.titleRomaji.asColumn(mediaTable),
                        sort.order
                    )
                MediaListSort.REPEAT ->
                    requireBuilder().orderBy(
                        MediaListEntitySchema.repeatCount.asColumn(mediaListTable),
                        sort.order
                    )
                MediaListSort.STARTED_ON ->
                    requireBuilder().orderBy(
                        MediaListEntitySchema.startedAt.asColumn(mediaListTable),
                        sort.order
                    )
                MediaListSort.UPDATED_TIME ->
                    requireBuilder().orderBy(
                        MediaListEntitySchema.updatedAt.asColumn(mediaListTable),
                        sort.order
                    )
                else -> {
                    val qualifier = sort.sortable.name.toLowerCase(Locale.ROOT)
                    requireBuilder().orderBy(qualifier.asColumn(mediaListTable), sort.order)
                }
            }
        }
    }

    /**
     * Staring point of the query builder, that should make use of [requireBuilder]
     * to add query objections
     */
    override fun onBuildQuery(filter: T) {
        requireBuilder() from (mediaTable).innerJoin(mediaListTable).on(
            MediaEntitySchema.id.asColumn(mediaTable).equal(
                MediaListEntitySchema.mediaId.asColumn(mediaListTable)
            )
        ) whereAnd {
            MediaListEntitySchema.userId.asColumn(mediaListTable).equal(filter.userId)
        }

        selection(filter)
        order(filter)
    }

    class Paged(
        override val authentication: IAuthenticationSettings
    ) : MediaListQueryFilter<MediaListParam.Paged>() {

        override fun selection(filter: MediaListParam.Paged) {
            super.selection(filter)
            filter.customListName?.also { listName ->
                requireBuilder().from {
                    innerJoin(customListTable).on(
                        CustomListEntitySchema.mediaListId.asColumn(customListTable).equal(
                            MediaListEntitySchema.id.asColumn(mediaListTable)
                        )
                    )
                } whereAnd {
                    CustomListEntitySchema.listName.asColumn(customListTable) equal listName
                } whereAnd {
                    CustomListEntitySchema.userId.asColumn(customListTable) equal filter.userId
                } whereAnd {
                    CustomListEntitySchema.enabled.asColumn(customListTable) equal true
                }
            }
        }
    }

    class Collection(
        override val authentication: IAuthenticationSettings
    ) : MediaListQueryFilter<MediaListParam.Collection>() {

        override fun selection(filter: MediaListParam.Collection) {
            super.selection(filter)
        }
    }

    class Entry : FilterQueryBuilder<MediaListParam.Entry>() {
        private val mediaListTable = MediaListEntitySchema.tableName.asTable()
        private val mediaTable = MediaEntitySchema.tableName.asTable()

        /**
         * Staring point of the query builder, that should make use of [requireBuilder]
         * to add query objections
         */
        override fun onBuildQuery(filter: MediaListParam.Entry) {
            requireBuilder() from (mediaTable).innerJoin(mediaListTable).on(
                MediaEntitySchema.id.asColumn(mediaTable).equal(
                    MediaListEntitySchema.mediaId.asColumn(mediaListTable)
                )
            ) whereAnd {
                MediaListEntitySchema.userId.asColumn(mediaListTable).equal(filter.userId)
            } whereAnd {
                MediaEntitySchema.id.asColumn(mediaTable).equal(filter.mediaId)
            }
        }
    }
}