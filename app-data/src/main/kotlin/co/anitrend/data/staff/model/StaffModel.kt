/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.staff.model

import co.anitrend.data.character.model.remote.connection.CharacterConnection
import co.anitrend.data.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.shared.model.SharedImageModel
import co.anitrend.data.shared.model.SharedNameModel
import co.anitrend.data.staff.model.contract.IStaffModel
import co.anitrend.domain.staff.enums.StaffLanguage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class StaffModel : IStaffModel {

    abstract val age: Int?
    abstract val dateOfBirth: FuzzyDateModel?
    abstract val dateOfDeath: FuzzyDateModel?
    abstract val gender: String?
    abstract val homeTown: String?
    abstract val bloodType: String?
    abstract val primaryOccupations: List<String>?
    abstract val yearsActive: List<Int>

    /**
     * @param dubGroup Used for grouping roles where multiple dubs exist for the same language.
     * Either dubbing company name or language variant.
     * @param roleNotes Notes regarding the VA's role for the character
     * @param voiceActor The voice actors of the character
     */
    @Serializable
    internal data class ActorRole(
        @SerialName("dubGroup") val dubGroup: String?,
        @SerialName("roleNotes") val roleNotes: String?,
        @SerialName("voiceActor") val voiceActor: Core
    )

    @Serializable
    internal data class Core(
        @SerialName("age") override val age: Int?,
        @SerialName("dateOfBirth") override val dateOfBirth: FuzzyDateModel?,
        @SerialName("dateOfDeath") override val dateOfDeath: FuzzyDateModel?,
        @SerialName("gender") override val gender: String?,
        @SerialName("homeTown") override val homeTown: String?,
        @SerialName("bloodType") override val bloodType: String?,
        @SerialName("primaryOccupations") override val primaryOccupations: List<String>?,
        @SerialName("yearsActive") override val yearsActive: List<Int>,
        @SerialName("description") override val description: String?,
        @SerialName("favourites") override val favourites: Int,
        @SerialName("image") override val image: SharedImageModel?,
        @SerialName("isFavourite") override val isFavourite: Boolean,
        @SerialName("isFavouriteBlocked") override val isFavouriteBlocked: Boolean,
        @SerialName("languageV2") override val language: StaffLanguage?,
        @SerialName("name") override val name: SharedNameModel?,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("id") override val id: Long
    ) : StaffModel()

    /**
     * @param character Characters voiced by the actor
     * @param staffMedia Media where the staff member has a production role
     */
    @Serializable
    internal data class Extended(
        @SerialName("character") val character: CharacterConnection?,
        @SerialName("staffMedia") val staffMedia: MediaConnection?,
        @SerialName("age") override val age: Int?,
        @SerialName("dateOfBirth") override val dateOfBirth: FuzzyDateModel?,
        @SerialName("dateOfDeath") override val dateOfDeath: FuzzyDateModel?,
        @SerialName("gender") override val gender: String?,
        @SerialName("homeTown") override val homeTown: String?,
        @SerialName("bloodType") override val bloodType: String?,
        @SerialName("primaryOccupations") override val primaryOccupations: List<String>?,
        @SerialName("yearsActive") override val yearsActive: List<Int>,
        @SerialName("description") override val description: String?,
        @SerialName("favourites") override val favourites: Int,
        @SerialName("image") override val image: SharedImageModel?,
        @SerialName("isFavourite") override val isFavourite: Boolean,
        @SerialName("isFavouriteBlocked") override val isFavouriteBlocked: Boolean,
        @SerialName("languageV2") override val language: StaffLanguage?,
        @SerialName("name") override val name: SharedNameModel?,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("id") override val id: Long
    ) : StaffModel()
}