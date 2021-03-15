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

package co.anitrend.data.tag.mapper

import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.railway.OutCome
import co.anitrend.data.arch.railway.extension.evaluate
import co.anitrend.data.arch.railway.extension.otherwise
import co.anitrend.data.arch.railway.extension.then
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.tag.converter.TagModelConverter
import co.anitrend.data.tag.datasource.local.TagLocalSource
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.entity.connection.TagConnectionEntity
import co.anitrend.data.tag.model.remote.TagContainerModel

internal class TagMapper(
    private val localSource: TagLocalSource,
    private val converter: TagModelConverter
) : DefaultMapper<TagContainerModel, List<TagEntity>>() {

    private var connections: List<TagConnectionEntity>? = null

    suspend fun persistEmbedded() {
        connections?.also {
            localSource.upsertConnections(it)
        }
        connections = null
    }

    fun onEmbedded(source: List<MediaModel>) {
        connections = source.flatMap { media ->
            media.tags?.map {
                TagConnectionEntity(
                    rank = it.rank ?: 0,
                    isMediaSpoiler = it.isMediaSpoiler ?: false,
                    mediaId = media.id,
                    tagId = it.id,
                )
            }.orEmpty()
        }
    }

    override suspend fun persistChanges(data: List<TagEntity>): OutCome<Nothing?> {
        return runCatching {
            localSource.upsert(data)
            OutCome.Pass(null)
        }.getOrElse { OutCome.Fail(listOf(it)) }
    }

    /**
     * Inserts the given object into the implemented room database
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<TagEntity>) {
        mappedData evaluate
                ::checkValidity then
                ::persistChanges otherwise
                ::handleException
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: TagContainerModel
    ) = converter.convertFrom(
        source.mediaTagCollection
    )
}