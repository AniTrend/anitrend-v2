/*
 * Copyright (C) 2025 AniTrend
 */
package co.anitrend.data.edge.media.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import co.anitrend.data.core.common.IEntityId
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_media",
    primaryKeys = ["id"],
    indices = [
        Index(
            value = ["id_mal"],
            unique = true,
        ),
        Index(
            value = ["id_anilist"],
            unique = true,
        ),
    ],
)
@EntitySchema
data class EdgeMediaEntity(
    @Embedded(prefix = "title_") val title: Title,
    @ColumnInfo(name = "format") val format: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "banner") val bannerImage: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "fanart") val fanart: String?,
    @ColumnInfo(name = "season") val season: String?,
    @ColumnInfo(name = "season_year") val seasonYear: Int?,
    @Embedded(prefix = "cover_") val cover: Cover,
    @ColumnInfo(name = "age_rating") val ageRating: String?,
    @ColumnInfo(name = "is_adult") val isAdult: Boolean?,
    @Embedded(prefix = "id_") val externalIds: ExternalIds,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "id") override val id: Long,
) : IEntityId<Long>

// Grouped sub-objects following codebase precedent
data class Title(
    @ColumnInfo(name = "romaji") val romaji: String?,
    @ColumnInfo(name = "english") val english: String?,
    @ColumnInfo(name = "native") val native: String?,
)

data class Cover(
    @ColumnInfo(name = "medium") val medium: String?,
    @ColumnInfo(name = "large") val large: String?,
    @ColumnInfo(name = "xlarge") val extraLarge: String?,
    @ColumnInfo(name = "color") val color: String?,
)

data class ExternalIds(
    @ColumnInfo(name = "anilist") val aniList: Int?,
    @ColumnInfo(name = "mal") val myAnimeList: Int?,
    @ColumnInfo(name = "notify") val notify: String?,
    @ColumnInfo(name = "trakt") val trakt: Int?,
    @ColumnInfo(name = "tvdb") val tvdb: Int?,
    @ColumnInfo(name = "tmdb") val tmdb: Int?,
)
