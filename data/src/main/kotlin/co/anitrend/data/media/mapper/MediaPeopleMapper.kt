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
package co.anitrend.data.media.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.converter.MediaCharacterEdgeConverter
import co.anitrend.data.media.converter.MediaStaffEdgeConverter
import co.anitrend.data.media.model.container.MediaPeopleModelContainer
import co.anitrend.domain.media.entity.MediaPerson

internal sealed class MediaPeopleMapper<S, D> : DefaultMapper<S, D>() {
    class Characters(
        private val converter: MediaCharacterEdgeConverter,
    ) : MediaPeopleMapper<MediaPeopleModelContainer.Characters, List<MediaPerson.Character>>() {
        override suspend fun persist(data: List<MediaPerson.Character>) {
        }

        override suspend fun onResponseMapFrom(source: MediaPeopleModelContainer.Characters) =
            converter.convertFrom(
                source.media
                    ?.characters
                    ?.edges
                    .orEmpty(),
            )
    }

    class Staff(
        private val converter: MediaStaffEdgeConverter,
    ) : MediaPeopleMapper<MediaPeopleModelContainer.Staff, List<MediaPerson.Staff>>() {
        override suspend fun persist(data: List<MediaPerson.Staff>) {
        }

        override suspend fun onResponseMapFrom(source: MediaPeopleModelContainer.Staff) =
            converter.convertFrom(
                source.media
                    ?.staff
                    ?.edges
                    .orEmpty(),
            )
    }
}
