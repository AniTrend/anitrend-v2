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
package co.anitrend.domain.media.entity

import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName
import co.anitrend.domain.staff.enums.StaffLanguage

sealed class MediaPerson {
    abstract val image: CoverImage?
    abstract val name: CoverName?
    abstract val siteUrl: String?
    abstract val id: Long

    data class Character(
        val role: CharacterRole?,
        val mediaRoleName: String?,
        val voiceActors: List<VoiceActor>,
        override val image: CoverImage?,
        override val name: CoverName?,
        override val siteUrl: String?,
        override val id: Long,
    ) : MediaPerson()

    data class Staff(
        val role: String?,
        val language: StaffLanguage?,
        override val image: CoverImage?,
        override val name: CoverName?,
        override val siteUrl: String?,
        override val id: Long,
    ) : MediaPerson()

    data class VoiceActor(
        val dubGroup: String?,
        val roleNotes: String?,
        val language: StaffLanguage?,
        val image: CoverImage?,
        val name: CoverName?,
        val siteUrl: String?,
        val id: Long,
    )
}
