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

package co.anitrend.data.character.model.remote

import co.anitrend.data.character.model.contract.ICharacterModel
import co.anitrend.data.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.shared.model.SharedImageModel
import co.anitrend.data.shared.model.SharedNameModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Character](https://anilist.github.io/ApiV2-GraphQL-Docs/character.doc.html)
 * A character that features in an anime or manga
 */
@Serializable
internal sealed class CharacterModel : ICharacterModel {

    abstract val age: Int?
    abstract val dateOfBirth: FuzzyDateModel?
    abstract val bloodType: String?
    abstract val gender: String?

    @Serializable
    internal data class Core(
        @SerialName("age") override val age: Int? = null,
        @SerialName("dateOfBirth") override val dateOfBirth: FuzzyDateModel? = null,
        @SerialName("gender") override val gender: String? = null,
        @SerialName("bloodType") override val bloodType: String? = null,
        @SerialName("description") override val description: String? = null,
        @SerialName("favourites") override val favourites: Int? = null,
        @SerialName("image") override val image: SharedImageModel? = null,
        @SerialName("isFavourite") override val isFavourite: Boolean,
        @SerialName("isFavouriteBlocked") override val isFavouriteBlocked: Boolean,
        @SerialName("name") override val name: SharedNameModel? = null,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("id") override val id: Long,
    ) : CharacterModel()

    @Serializable
    internal data class Extended(
        @SerialName("media") val media: MediaConnection.Character? = null,
        @SerialName("age") override val age: Int? = null,
        @SerialName("dateOfBirth") override val dateOfBirth: FuzzyDateModel? = null,
        @SerialName("gender") override val gender: String? = null,
        @SerialName("bloodType") override val bloodType: String? = null,
        @SerialName("description") override val description: String? = null,
        @SerialName("favourites") override val favourites: Int? = null,
        @SerialName("image") override val image: SharedImageModel? = null,
        @SerialName("isFavourite") override val isFavourite: Boolean,
        @SerialName("isFavouriteBlocked") override val isFavouriteBlocked: Boolean,
        @SerialName("name") override val name: SharedNameModel? = null,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("id") override val id: Long,
    ) : CharacterModel()
}

