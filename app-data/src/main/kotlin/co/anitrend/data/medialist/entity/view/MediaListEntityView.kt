/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.medialist.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.customlist.entity.CustomListEntity
import co.anitrend.data.customscore.entity.CustomScoreEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.user.entity.UserEntity

internal sealed class MediaListEntityView {
    abstract val mediaList: MediaListEntity
    abstract val customList: List<CustomListEntity>
    abstract val customScore: List<CustomScoreEntity>

    internal data class Core(
        @Embedded override val mediaList: MediaListEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_list_id"
        )
        override val customList: List<CustomListEntity> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_list_id"
        )
        override val customScore: List<CustomScoreEntity> = emptyList()
    ) : MediaListEntityView()

    internal data class WithMedia(
        @Relation(
            parentColumn = "media_id",
            entityColumn = "id"
        )
        val media: MediaEntity,
        @Embedded override val mediaList: MediaListEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_list_id"
        )
        override val customList: List<CustomListEntity> = emptyList(),
        @Relation(
            parentColumn = "id",
            entityColumn = "media_list_id"
        )
        override val customScore: List<CustomScoreEntity> = emptyList()
    ) : MediaListEntityView()
}