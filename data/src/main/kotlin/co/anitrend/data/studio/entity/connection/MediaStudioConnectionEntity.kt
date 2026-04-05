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
package co.anitrend.data.studio.entity.connection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "media_studio_connection",
    indices = [
        Index(value = ["media_id", "entry_id"], unique = true),
        Index(value = ["media_id", "sort_index"]),
    ],
)
@EntitySchema
internal data class MediaStudioConnectionEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "entry_id") val entryId: Long,
    @ColumnInfo(name = "studio_id") val studioId: Long,
    @ColumnInfo(name = "studio_name") val studioName: String,
    @ColumnInfo(name = "is_main") val isMain: Boolean,
    @ColumnInfo(name = "sort_index") val sortIndex: Int,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long? = null,
) : IEntityId<Long?>
