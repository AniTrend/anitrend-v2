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

package co.anitrend.data.media.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.media.entity.MediaEntity

internal sealed class MediaEntityView {
    abstract val media: MediaEntity
    abstract val nextAiring: AiringScheduleEntity?

    internal data class WithAiring(
        @Embedded override val media: MediaEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val nextAiring: AiringScheduleEntity?
    ) : MediaEntityView()

    internal data class WithMediaList(
        @Embedded override val media: MediaEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "media_id"
        )
        override val nextAiring: AiringScheduleEntity?
    ) : MediaEntityView()
}