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
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.connection.MediaCharacterConnectionEntity
import co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity
import co.anitrend.data.media.model.container.MediaPeopleModelContainer

internal sealed class MediaPeopleMapper<S, D> : DefaultMapper<S, D>() {
    class Characters(
        private val localSource: MediaLocalSource,
        private val converter: MediaCharacterEdgeConverter,
    ) : MediaPeopleMapper<MediaPeopleModelContainer.Characters, List<MediaCharacterConnectionEntity>>() {
        private var mediaId: Long = 0L
        private var sortIndexOffset: Int = 0

        suspend fun onRequest(
            mediaId: Long,
            page: Int,
        ) {
            this.mediaId = mediaId
            sortIndexOffset =
                if (page <= 1) {
                    0
                } else {
                    localSource.mediaCharactersCount(mediaId)
                }
        }

        override suspend fun persist(data: List<MediaCharacterConnectionEntity>) {
            if (sortIndexOffset == 0) {
                localSource.clearMediaCharactersByMediaId(mediaId)
            }

            if (data.isEmpty()) {
                return
            }

            localSource.upsertMediaCharacters(data)
        }

        override suspend fun onResponseMapFrom(source: MediaPeopleModelContainer.Characters) =
            converter
                .convertFrom(
                    source.media
                        ?.characters
                        ?.edges
                        .orEmpty(),
                ).mapIndexed { index, character ->
                    val voiceActor = character.voiceActors.firstOrNull()

                    MediaCharacterConnectionEntity(
                        mediaId = mediaId,
                        characterId = character.id,
                        role = character.role,
                        mediaRoleName = character.mediaRoleName,
                        sortIndex = sortIndexOffset + index,
                        imageLarge = character.image?.large,
                        imageMedium = character.image?.medium,
                        nameFirst = character.name?.first,
                        nameFull = character.name?.full,
                        nameLast = character.name?.last,
                        nameMiddle = character.name?.middle,
                        nameNative = character.name?.native,
                        nameUserPreferred = character.name?.userPreferred,
                        nameAlternative = character.name?.alternative.orEmpty(),
                        nameAlternativeSpoiler = character.name?.alternativeSpoiler.orEmpty(),
                        siteUrl = character.siteUrl,
                        voiceActorId = voiceActor?.id,
                        voiceActorNameFull = voiceActor?.name?.full,
                        voiceActorNameUserPreferred = voiceActor?.name?.userPreferred,
                    )
                }
    }

    class Staff(
        private val localSource: MediaLocalSource,
        private val converter: MediaStaffEdgeConverter,
    ) : MediaPeopleMapper<MediaPeopleModelContainer.Staff, List<MediaStaffConnectionEntity>>() {
        private var mediaId: Long = 0L
        private var sortIndexOffset: Int = 0

        suspend fun onRequest(
            mediaId: Long,
            page: Int,
        ) {
            this.mediaId = mediaId
            sortIndexOffset =
                if (page <= 1) {
                    0
                } else {
                    localSource.mediaStaffCount(mediaId)
                }
        }

        override suspend fun persist(data: List<MediaStaffConnectionEntity>) {
            if (sortIndexOffset == 0) {
                localSource.clearMediaStaffByMediaId(mediaId)
            }

            if (data.isEmpty()) {
                return
            }

            localSource.upsertMediaStaff(data)
        }

        override suspend fun onResponseMapFrom(source: MediaPeopleModelContainer.Staff) =
            converter
                .convertFrom(
                    source.media
                        ?.staff
                        ?.edges
                        .orEmpty(),
                ).mapIndexed { index, staff ->
                    MediaStaffConnectionEntity(
                        mediaId = mediaId,
                        staffId = staff.id,
                        role = staff.role,
                        language = staff.language,
                        sortIndex = sortIndexOffset + index,
                        imageLarge = staff.image?.large,
                        imageMedium = staff.image?.medium,
                        nameFirst = staff.name?.first,
                        nameFull = staff.name?.full,
                        nameLast = staff.name?.last,
                        nameMiddle = staff.name?.middle,
                        nameNative = staff.name?.native,
                        nameUserPreferred = staff.name?.userPreferred,
                        nameAlternative = staff.name?.alternative.orEmpty(),
                        nameAlternativeSpoiler = staff.name?.alternativeSpoiler.orEmpty(),
                        siteUrl = staff.siteUrl,
                    )
                }
    }
}
