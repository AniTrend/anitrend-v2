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
package co.anitrend.data.media.source.contract

import androidx.paging.PagingData
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow

internal class MediaPeopleSource {
    abstract class Characters {
        abstract operator fun invoke(param: MediaParam.Characters): Flow<PagingData<MediaPerson.Character>>
    }

    abstract class Staff {
        abstract operator fun invoke(param: MediaParam.Staff): Flow<PagingData<MediaPerson.Staff>>
    }
}
