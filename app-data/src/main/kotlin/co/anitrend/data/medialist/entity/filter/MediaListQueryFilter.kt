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
import co.anitrend.data.media.entity.MediaEntitySchema
import co.anitrend.data.medialist.entity.MediaListEntitySchema
import co.anitrend.domain.medialist.enums.MediaListSort
import co.anitrend.domain.medialist.model.MediaListParam
import co.anitrend.support.query.builder.core.criteria.extensions.equal
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.from.extentions.innerJoin
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import java.util.*

internal sealed class MediaListQueryFilter<T : MediaListParam.Entries> : FilterQueryBuilder<T>() {
    protected val mediaListTable = MediaListEntitySchema.tableName.asTable()
    protected val mediaTable = MediaEntitySchema.tableName.asTable()

    protected abstract val authentication: IAuthenticationSettings

    protected open fun selection(filter: T) {

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
        requireBuilder() from mediaListTable innerJoin(mediaTable) on
                MediaEntitySchema.id.asColumn(mediaTable).equal(
                    MediaListEntitySchema.mediaId.asColumn(mediaListTable)
                )

        selection(filter)
        order(filter)
    }

    class Paged(
        override val authentication: IAuthenticationSettings
    ) : MediaListQueryFilter<MediaListParam.Paged>() {

        override fun selection(filter: MediaListParam.Paged) {
            super.selection(filter)
        }
    }

    class Collection(
        override val authentication: IAuthenticationSettings
    ) : MediaListQueryFilter<MediaListParam.Collection>() {

        override fun selection(filter: MediaListParam.Collection) {
            super.selection(filter)
        }
    }
}