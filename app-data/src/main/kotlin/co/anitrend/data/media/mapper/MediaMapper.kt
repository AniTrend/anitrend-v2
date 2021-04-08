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

import co.anitrend.data.airing.mapper.AiringMapper
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.android.extensions.runInTransaction
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.genre.mapper.GenreMapper
import co.anitrend.data.link.mapper.LinkMapper
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.media.model.container.MediaModelContainer
import co.anitrend.data.medialist.mapper.MediaListMapper
import co.anitrend.data.rank.mapper.RankMapper
import co.anitrend.data.tag.mapper.TagMapper
import co.anitrend.domain.media.entity.Media

internal sealed class MediaMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: MediaLocalSource
    protected abstract val converter: MediaModelConverter

    class Network(
        private val converter: MediaConverter
    ) : DefaultMapper<MediaModelContainer.Paged, List<Media>>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<Media>) {
            
        }
        
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
        private val mediaListMapper: MediaListMapper.Embed,
        private val airingMapper: AiringMapper.Embed,
        private val genreMapper: GenreMapper.Embed,
        private val tagMapper: TagMapper.Embed,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ) : MediaMapper<MediaModelContainer.Paged, List<MediaEntity>>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<MediaEntity>) {
            runInTransaction {
                localSource.upsert(data)
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
                airingMapper.persistEmbedded()
                mediaListMapper.persistEmbedded()
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: MediaModelContainer.Paged): List<MediaEntity> {
            tagMapper.onEmbedded(
                TagMapper.Embed.asItem(source.page.media)
            )
            genreMapper.onEmbedded(
                GenreMapper.Embed.asItem(source.page.media)
            )
            airingMapper.onEmbedded(
                source.page.media.mapNotNull {
                    it.nextAiringEpisode
                }
            )
            mediaListMapper.onEmbedded(
                source.page.media.mapNotNull(
                    MediaModel.Core::mediaListEntry
                )
            )
            return converter.convertFrom(source.page.media)
        }
    }

    class Detail(
        private val mediaListMapper: MediaListMapper.Embed,
        private val airingMapper: AiringMapper.Embed,
        private val genreMapper: GenreMapper.Embed,
        private val tagMapper: TagMapper.Embed,
        private val linkMapper: LinkMapper,
        private val rankMapper: RankMapper,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ) : MediaMapper<MediaModelContainer.Detail, MediaEntity>() {

        override suspend fun persist(data: MediaEntity) {
            runInTransaction {
                localSource.upsert(data)
                linkMapper.persistEmbedded()
                rankMapper.persistEmbedded()
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
                airingMapper.persistEmbedded()
                mediaListMapper.persistEmbedded()
            }
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
            tagMapper.onEmbedded(
                TagMapper.Embed.asItem(source.media)
            )
            genreMapper.onEmbedded(
                GenreMapper.Embed.asItem(source.media)
            )
            airingMapper.onEmbedded(source.media.nextAiringEpisode)
            mediaListMapper.onEmbedded(
                source.media.mediaListEntry
            )
            linkMapper.onEmbedded(
                source.media.externalLinks.map {
                    LinkMapper.Item(source.media.id, it)
                }
            )
            rankMapper.onEmbedded(
                source.media.rankings.map {
                    RankMapper.Item(source.media.id, it)
                }
            )
            return converter.convertFrom(source.media)
        }
    }

    class Embed(
        private val genreMapper: GenreMapper.Embed,
        private val tagMapper: TagMapper.Embed,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ): EmbedMapper<MediaModel, MediaEntity>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<MediaEntity>) {
            runInTransaction {
                super.persist(data)
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: List<MediaModel>): List<MediaEntity> {
            tagMapper.onEmbedded(
                TagMapper.Embed.asItem(source)
            )
            genreMapper.onEmbedded(
                GenreMapper.Embed.asItem(source)
            )
            return super.onResponseMapFrom(source)
        }
    }

    class EmbedWithAiring(
        private val airingMapper: AiringMapper.Embed,
        private val genreMapper: GenreMapper.Embed,
        private val tagMapper: TagMapper.Embed,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ): EmbedMapper<MediaModel, MediaEntity>() {

        override suspend fun persist(data: List<MediaEntity>) {
            runInTransaction {
                super.persist(data)
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
                airingMapper.persistEmbedded()
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: List<MediaModel>): List<MediaEntity> {
            tagMapper.onEmbedded(
                TagMapper.Embed.asItem(source)
            )
            genreMapper.onEmbedded(
                GenreMapper.Embed.asItem(source)
            )
            airingMapper.onEmbedded(
                source.mapNotNull {
                    it.nextAiringEpisode as? AiringScheduleModel
                }
            )
            return super.onResponseMapFrom(source)
        }
    }


    class EmbedWithMediaList(
        private val airingMapper: AiringMapper.Embed,
        private val mediaListMapper: MediaListMapper.Embed,
        private val genreMapper: GenreMapper.Embed,
        private val tagMapper: TagMapper.Embed,
        override val localSource: MediaLocalSource,
        override val converter: MediaModelConverter
    ): EmbedMapper<MediaModel, MediaEntity>() {

        override suspend fun persist(data: List<MediaEntity>) {
            runInTransaction {
                super.persist(data)
                tagMapper.persistEmbedded()
                genreMapper.persistEmbedded()
                airingMapper.persistEmbedded()
                mediaListMapper.persistEmbedded()
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: List<MediaModel>): List<MediaEntity> {
            tagMapper.onEmbedded(
                TagMapper.Embed.asItem(source)
            )
            genreMapper.onEmbedded(
                GenreMapper.Embed.asItem(source)
            )
            airingMapper.onEmbedded(
                source.mapNotNull {
                    it.nextAiringEpisode as? AiringScheduleModel
                }
            )
            mediaListMapper.onEmbedded(
                source.mapNotNull {
                    when (it) {
                        is MediaModel.Extended -> it.mediaListEntry
                        is MediaModel.Core -> it.mediaListEntry
                        else -> null
                    }
                }
            )
            return super.onResponseMapFrom(source)
        }
    }
}