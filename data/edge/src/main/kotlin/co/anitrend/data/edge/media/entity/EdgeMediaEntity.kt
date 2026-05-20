/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.media.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import co.anitrend.data.core.common.IEntityId
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.support.query.builder.annotation.EntitySchema

data class EdgeMediaTitleEntity(
    @ColumnInfo("canonical") val canonical: String?,
    @ColumnInfo("english") val english: String?,
    @ColumnInfo("harigana") val harigana: String?,
    @ColumnInfo("japanese") val japanese: String?,
    @ColumnInfo("romaji") val romaji: String?,
    @ColumnInfo("synonyms") val synonyms: List<String>?,
)

data class EdgeMediaCoverEntity(
    @ColumnInfo(name = "medium") val medium: String?,
    @ColumnInfo(name = "large") val large: String?,
    @ColumnInfo(name = "xlarge") val extraLarge: String?,
    @ColumnInfo(name = "color") val color: String?,
)

data class EdgeMediaExternalIdsEntity(
    @ColumnInfo(name = "ani_db") val aniDb: Long?,
    @ColumnInfo(name = "ani_list") val aniList: Long?,
    @ColumnInfo(name = "anime_planet") val animePlanet: String?,
    @ColumnInfo(name = "ani_search") val aniSearch: Long?,
    @ColumnInfo(name = "imdb") val imdb: String?,
    @ColumnInfo(name = "kitsu") val kitsu: Long?,
    @ColumnInfo(name = "live_chart") val liveChart: Long?,
    @ColumnInfo(name = "my_anime_list") val myAnimeList: Long?,
    @ColumnInfo(name = "notify") val notify: String?,
    @ColumnInfo(name = "shoboi") val shoboi: Long?,
    @ColumnInfo(name = "slug") val slug: String?,
    @ColumnInfo(name = "themoviedb") val tmdb: Long?,
    @ColumnInfo(name = "trakt") val trakt: Long?,
    @ColumnInfo(name = "tv_db") val tvDb: Long?,
    @ColumnInfo(name = "tv_maze") val tvMaze: Long?,
    @ColumnInfo(name = "tv_rage") val tvRage: String?,
)

data class EdgeMediaScheduleEpisodeEntity(
    @ColumnInfo(name = "id") val id: Long?,
    @ColumnInfo(name = "air_date") val airDate: Long?,
    @ColumnInfo(name = "episode_number") val episodeNumber: Int?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "production_code") val productionCode: String?,
    @ColumnInfo(name = "runtime") val runtime: Int?,
    @ColumnInfo(name = "season_number") val seasonNumber: Int?,
    @ColumnInfo(name = "tmdb_id") val tmdbId: Long?,
)

data class EdgeMediaScheduleEntity(
    @ColumnInfo(name = "first_air_date") val firstAirDate: Long?,
    @ColumnInfo(name = "last_air_date") val lastAirDate: Long?,
    @ColumnInfo(name = "next_episode_id") val nextEpisodeId: Long?,
    @ColumnInfo(name = "last_episode_id") val lastEpisodeId: Long?,
    @Embedded(prefix = "next_episode_detail_") val nextEpisode: EdgeMediaScheduleEpisodeEntity?,
    @Embedded(prefix = "last_episode_detail_") val lastEpisode: EdgeMediaScheduleEpisodeEntity?,
)

@Entity(
    tableName = "edge_media",
    primaryKeys = ["id"],
    indices = [
        Index(
            value = ["id_my_anime_list"],
            unique = true,
        ),
        Index(
            value = ["id_ani_list"],
            unique = true,
        ),
    ],
)
@EntitySchema
data class EdgeMediaEntity(
    @Embedded(prefix = "title_") val title: EdgeMediaTitleEntity,
    @Embedded(prefix = "cover_") val cover: EdgeMediaCoverEntity,
    @ColumnInfo(name = "banner") val banner: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "fanart") val fanart: String?,
    @ColumnInfo(name = "format") val format: String?,
    @ColumnInfo(name = "homepage") val homepage: String?,
    @ColumnInfo("aired_episodes") val airedEpisodes: Int?,
    @ColumnInfo(name = "broadcast") val broadcast: String?,
    @ColumnInfo("source") val source: String? = null,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "age_rating") val ageRating: String?,
    @ColumnInfo(name = "is_adult") val isAdult: Boolean?,
    @ColumnInfo(name = "kind") val kind: MediaType,
    @ColumnInfo(name = "chapters") val chapters: Int?,
    @ColumnInfo(name = "volumes") val volumes: Int?,
    @ColumnInfo(name = "more_info") val moreInfo: String?,
    @ColumnInfo(name = "published_from") val publishedFrom: Long?,
    @ColumnInfo(name = "published_to") val publishedTo: Long?,
    @Embedded(prefix = "id_") val externalIds: EdgeMediaExternalIdsEntity,
    @Embedded(prefix = "schedule_") val schedule: EdgeMediaScheduleEntity?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "id") override val id: String,
) : IEntityId<String>
