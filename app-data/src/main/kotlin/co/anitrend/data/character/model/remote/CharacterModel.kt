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
import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.shared.model.SharedImage
import co.anitrend.data.shared.model.SharedName
import com.google.gson.annotations.SerializedName

/** [Character](https://anilist.github.io/ApiV2-GraphQL-Docs/character.doc.html)
 * A character that features in an anime or manga
 *
 * @param description A general description of the character
 */
internal class CharacterModel(
    val media: MediaConnection?,
    @SerializedName("description") override val description: String?,
    @SerializedName("favourites") override val favourites: Int?,
    @SerializedName("image") override val image: SharedImage?,
    @SerializedName("isFavourite") override val isFavourite: Boolean,
    @SerializedName("name") override val name: SharedName?,
    @SerializedName("siteUrl") override val siteUrl: String?,
    @SerializedName("updatedAt") override val updatedAt: Long?,
    @SerializedName("id") override val id: Long,
) : ICharacterModel