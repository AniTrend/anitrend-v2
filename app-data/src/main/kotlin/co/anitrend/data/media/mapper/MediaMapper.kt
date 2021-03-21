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

package co.anitrend.data.media.mapper

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.airing.mapper.AiringMapper
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.arch.database.wrapper.SourceEntityWrapper
import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.railway.OutCome
import co.anitrend.data.genre.mapper.GenreMapper
import co.anitrend.data.link.datasource.LinkLocalSource
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.media.model.container.MediaModelContainer
import co.anitrend.data.rank.datasource.RankLocalSource
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.tag.mapper.TagMapper
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal sealed class MediaMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: MediaLocalSource
    protected abstract val converter: MediaModelConverter

    class Network(
        private val converter: MediaConverter
    ) : DefaultMapper<MediaModelContainer.Paged, List<Media>>() {
        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: MediaModelContainer.Paged
        ) = converter.convertFrom(source.page.media)
    }

    class Paged(
        private val airingMapper: AiringMapper.Collection,
        private val genreMapper: GenreMapper,
        private val tagMapper: TagMapper,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ) : MediaMapper<MediaModelContainer.Paged, List<MediaEntity>>() {

        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: List<MediaEntity>): OutCome<Nothing?> {
            return runCatching {
                localSource.upsert(data)
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
                airingMapper.persistEmbedded()
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: MediaModelContainer.Paged): List<MediaEntity> {
            tagMapper.onEmbedded(source.page.media)
            genreMapper.onEmbedded(source.page.media)
            airingMapper.onEmbedded(source.page.media)
            return converter.convertFrom(source.page.media)
        }
    }

    class Collection(
        private val genreMapper: GenreMapper,
        private val tagMapper: TagMapper,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ) : MediaMapper<List<MediaModel.Core>, List<MediaEntity>>() {

        private var media: List<MediaEntity>? = null

        suspend fun persistEmbedded() {
            persistChanges(media.orEmpty())
            media = null
        }

        suspend fun onEmbedded(source: List<AiringScheduleModel.Extended>) {
            val models = source.mapNotNull(AiringScheduleModel.Extended::media)
            media = onResponseMapFrom(models)
        }
        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: List<MediaEntity>): OutCome<Nothing?> {
            return runCatching {
                localSource.upsert(data)
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: List<MediaModel.Core>
        ): List<MediaEntity> {
            tagMapper.onEmbedded(source)
            genreMapper.onEmbedded(source)
            return converter.convertFrom(source)
        }
    }

    class Detail(
        private val airingMapper: AiringMapper.Collection,
        private val genreMapper: GenreMapper,
        private val tagMapper: TagMapper,
        private val rankLocalSource: RankLocalSource,
        private val linkLocalSource: LinkLocalSource,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ) : MediaMapper<MediaModelContainer.Detail, MediaEntity>() {

        private var linkWrapper: SourceEntityWrapper<LinkEntity>? = null
        private var rankWrapper: SourceEntityWrapper<RankEntity>? = null

        private fun saveEmbeddedRanks(source: MediaModel.Extended) {
            val connections = source.rankings.map {
                RankEntity(
                    mediaId = source.id,
                    allTime = it.allTime,
                    context = it.context,
                    format = it.format,
                    rank = it.rank,
                    season = it.season,
                    type = it.type,
                    year = it.year,
                    id = it.id,
                )
            }
            rankWrapper = SourceEntityWrapper.Many(rankLocalSource, connections)
        }

        private fun saveEmbeddedLinks(source: MediaModel.Extended) {
            val connections = source.externalLinks.map {
                LinkEntity(
                    mediaId = source.id,
                    site = it.site,
                    url = it.url,
                    id = it.id,
                )
            }
            linkWrapper = SourceEntityWrapper.Many(linkLocalSource, connections)
        }

        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: MediaEntity): OutCome<Nothing?> {
            return runCatching {
                localSource.upsertWithAttributes(data, linkWrapper, rankWrapper)
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
                airingMapper.persistEmbedded()
                linkWrapper = null
                rankWrapper = null
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: MediaModelContainer.Detail
        ): MediaEntity {
            tagMapper.onEmbedded(listOf(source.media))
            genreMapper.onEmbedded(listOf(source.media))
            airingMapper.onEmbedded(listOf(source.media))
            saveEmbeddedLinks(source.media)
            saveEmbeddedRanks(source.media)
            return converter.convertFrom(source.media)
        }
    }
}