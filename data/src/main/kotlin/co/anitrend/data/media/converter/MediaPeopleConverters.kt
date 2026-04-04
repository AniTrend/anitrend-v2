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
package co.anitrend.data.media.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.media.model.edge.MediaEdge
import co.anitrend.data.shared.model.SharedImageModel
import co.anitrend.data.shared.model.SharedNameModel
import co.anitrend.data.staff.model.StaffModel
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName
import co.anitrend.domain.media.entity.MediaPerson

internal class MediaCharacterEdgeConverter(
    override val fromType: (MediaEdge.Character) -> MediaPerson.Character = ::transform,
    override val toType: (MediaPerson.Character) -> MediaEdge.Character = { throw NotImplementedError() },
) : SupportConverter<MediaEdge.Character, MediaPerson.Character>() {
    private companion object : ISupportTransformer<MediaEdge.Character, MediaPerson.Character> {
        private fun SharedImageModel?.toCoverImage() =
            this?.let {
                CoverImage(
                    large = it.large,
                    medium = it.medium,
                )
            }

        private fun SharedNameModel?.toCoverName() =
            this?.let {
                CoverName(
                    middle = it.middle,
                    alternativeSpoiler = it.alternativeSpoiler.orEmpty(),
                    alternative = it.alternative.orEmpty(),
                    first = it.first,
                    full = it.full,
                    last = it.last,
                    native = it.native,
                    userPreferred = it.userPreferred,
                )
            }

        private fun StaffModel.Core.toVoiceActor() =
            MediaPerson.VoiceActor(
                dubGroup = null,
                roleNotes = null,
                language = language,
                image = image.toCoverImage(),
                name = name.toCoverName(),
                siteUrl = siteUrl,
                id = id,
            )

        override fun transform(source: MediaEdge.Character) =
            MediaPerson.Character(
                role = source.characterRole,
                mediaRoleName = source.name,
                voiceActors =
                    source.voiceActorRoles.orEmpty().map { actorRole ->
                        actorRole.voiceActor.toVoiceActor().copy(
                            dubGroup = actorRole.dubGroup,
                            roleNotes = actorRole.roleNotes,
                        )
                    },
                image = source.node?.image.toCoverImage(),
                name = source.node?.name.toCoverName(),
                siteUrl = source.node?.siteUrl,
                id = source.node?.id ?: source.id,
            )
    }
}

internal class MediaStaffEdgeConverter(
    override val fromType: (MediaEdge.Staff) -> MediaPerson.Staff = ::transform,
    override val toType: (MediaPerson.Staff) -> MediaEdge.Staff = { throw NotImplementedError() },
) : SupportConverter<MediaEdge.Staff, MediaPerson.Staff>() {
    private companion object : ISupportTransformer<MediaEdge.Staff, MediaPerson.Staff> {
        private fun SharedImageModel?.toCoverImage() =
            this?.let {
                CoverImage(
                    large = it.large,
                    medium = it.medium,
                )
            }

        private fun SharedNameModel?.toCoverName() =
            this?.let {
                CoverName(
                    middle = it.middle,
                    alternativeSpoiler = it.alternativeSpoiler.orEmpty(),
                    alternative = it.alternative.orEmpty(),
                    first = it.first,
                    full = it.full,
                    last = it.last,
                    native = it.native,
                    userPreferred = it.userPreferred,
                )
            }

        override fun transform(source: MediaEdge.Staff) =
            MediaPerson.Staff(
                role = source.staffRole,
                language = source.node?.language,
                image = source.node?.image.toCoverImage(),
                name = source.node?.name.toCoverName(),
                siteUrl = source.node?.siteUrl,
                id = source.node?.id ?: source.id,
            )
    }
}
