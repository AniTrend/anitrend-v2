/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.recommendation.entity.connection

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.media.entity.connection.MediaConnectionPreviewEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "media_recommendation_connection",
    indices = [
        Index(value = ["media_id", "entry_id"], unique = true),
        Index(value = ["media_id", "sort_index"]),
    ],
)
@EntitySchema
internal data class MediaRecommendationConnectionEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "entry_id") val entryId: Long,
    @ColumnInfo(name = "rating") val rating: Int?,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "user_rating") val userRating: String?,
    @ColumnInfo(name = "sort_index") val sortIndex: Int,
    @Embedded(prefix = "target_") val target: MediaConnectionPreviewEntity,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long? = null,
) : IEntityId<Long?>
