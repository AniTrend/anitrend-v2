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
package co.anitrend.data.media.entity.connection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "media_character_connection",
    indices = [
        Index(value = ["media_id", "character_id"], unique = true),
        Index(value = ["media_id", "sort_index"]),
    ],
)
@EntitySchema
internal data class MediaCharacterConnectionEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "character_id") val characterId: Long,
    @ColumnInfo(name = "role") val role: CharacterRole?,
    @ColumnInfo(name = "media_role_name") val mediaRoleName: String?,
    @ColumnInfo(name = "sort_index") val sortIndex: Int,
    @ColumnInfo(name = "image_large") val imageLarge: String?,
    @ColumnInfo(name = "image_medium") val imageMedium: String?,
    @ColumnInfo(name = "name_first") val nameFirst: String?,
    @ColumnInfo(name = "name_full") val nameFull: String?,
    @ColumnInfo(name = "name_last") val nameLast: String?,
    @ColumnInfo(name = "name_middle") val nameMiddle: String?,
    @ColumnInfo(name = "name_native") val nameNative: String?,
    @ColumnInfo(name = "name_user_preferred") val nameUserPreferred: String?,
    @ColumnInfo(name = "name_alternative") val nameAlternative: List<String>,
    @ColumnInfo(name = "name_alternative_spoiler") val nameAlternativeSpoiler: List<String>,
    @ColumnInfo(name = "site_url") val siteUrl: String?,
    @ColumnInfo(name = "voice_actor_id") val voiceActorId: Long?,
    @ColumnInfo(name = "voice_actor_name_full") val voiceActorNameFull: String?,
    @ColumnInfo(name = "voice_actor_name_user_preferred") val voiceActorNameUserPreferred: String?,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long? = null,
) : IEntityId<Long?>
