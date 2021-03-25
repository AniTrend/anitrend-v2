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

package co.anitrend.data.jikan.entity

import androidx.room.*
import co.anitrend.data.jikan.entity.contract.JikanEntityAttribute
import co.anitrend.data.shared.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "jikan",
    primaryKeys = ["id"]
)
@EntitySchema
internal data class JikanEntity(
    @Embedded(prefix = "title_") val title: Title,
    @ColumnInfo(name = "info") val info: String?,
    @ColumnInfo(name = "volumes") val volumes: Int? = null,
    @ColumnInfo(name = "chapters") val chapters: Int? = null,
    @ColumnInfo(name = "source") val source: String? = null,
    @ColumnInfo(name = "rating") val rating: String? = null,
    @ColumnInfo(name = "episodes") val episodes: Int? = null,
    @ColumnInfo(name = "duration") val duration: String? = null,
    @ColumnInfo(name = "premiered") val premiered: String? = null,
    @ColumnInfo(name = "broadcast") val broadcast: String? = null,
    @ColumnInfo(name = "trailer_url") val trailerUrl: String? = null,
    @ColumnInfo(name = "opening_themes") val openingThemes: List<String> = emptyList(),
    @ColumnInfo(name = "ending_themes") val endingThemes: List<String> = emptyList(),
    @ColumnInfo(name = "url") val url: String? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "releasing") val releasing: Boolean? = null,
    @ColumnInfo(name = "synopsis") val synopsis: String? = null,
    @ColumnInfo(name = "background") val background: String? = null,
    @ColumnInfo(name = "id") override val id: Long
) : Identity {

    data class Title(
        @ColumnInfo(name = "japanese") val japanese: String? = null,
        @ColumnInfo(name = "english") val english: String? = null,
        @ColumnInfo(name = "preferred") val preferred: String? = null,
        @ColumnInfo(name = "synonyms") val synonyms: List<String> = emptyList()
    )

    @Entity(
        tableName = "jikan_author",
        primaryKeys = ["id"],
        indices = [
            Index(value = ["jikan_id"])
        ],
        foreignKeys = [
            ForeignKey(
                entity = JikanEntity::class,
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE,
                childColumns = ["jikan_id"],
                parentColumns = ["id"],
                deferred = true
            )
        ]
    )
    @EntitySchema
    data class AuthorEntity(
        @ColumnInfo(name = "jikan_id") override val jikanId: Long,
        @ColumnInfo(name = "type") override val type: String?,
        @ColumnInfo(name = "name") override val name: String?,
        @ColumnInfo(name = "id") override val id: Long
    ) : JikanEntityAttribute

    @Entity(
        tableName = "jikan_producer",
        primaryKeys = ["id"],
        indices = [
            Index(value = ["jikan_id"])
        ],
        foreignKeys = [
            ForeignKey(
                entity = JikanEntity::class,
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE,
                childColumns = ["jikan_id"],
                parentColumns = ["id"],
                deferred = true
            )
        ]
    )
    @EntitySchema
    data class ProducerEntity(
        @ColumnInfo(name = "jikan_id") override val jikanId: Long,
        @ColumnInfo(name = "type") override val type: String?,
        @ColumnInfo(name = "name") override val name: String?,
        @ColumnInfo(name = "id") override val id: Long
    ) : JikanEntityAttribute

    @Entity(
        tableName = "jikan_licensor",
        primaryKeys = ["id"],
        indices = [
            Index(value = ["jikan_id"])
        ],
        foreignKeys = [
            ForeignKey(
                entity = JikanEntity::class,
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE,
                childColumns = ["jikan_id"],
                parentColumns = ["id"],
                deferred = true
            )
        ]
    )
    @EntitySchema
    data class LicensorEntity(
        @ColumnInfo(name = "jikan_id") override val jikanId: Long,
        @ColumnInfo(name = "type") override val type: String?,
        @ColumnInfo(name = "name") override val name: String?,
        @ColumnInfo(name = "id") override val id: Long
    ) : JikanEntityAttribute

    @Entity(
        tableName = "jikan_studio",
        primaryKeys = ["id"],
        indices = [
            Index(value = ["jikan_id"])
        ],
        foreignKeys = [
            ForeignKey(
                entity = JikanEntity::class,
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE,
                childColumns = ["jikan_id"],
                parentColumns = ["id"],
                deferred = true
            )
        ]
    )
    @EntitySchema
    data class StudioEntity(
        @ColumnInfo(name = "jikan_id") override val jikanId: Long,
        @ColumnInfo(name = "type") override val type: String?,
        @ColumnInfo(name = "name") override val name: String?,
        @ColumnInfo(name = "id") override val id: Long
    ) : JikanEntityAttribute
}
