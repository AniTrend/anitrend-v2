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

package co.anitrend.data.favourite.model.remote

import co.anitrend.data.character.model.remote.connection.CharacterConnection
import co.anitrend.data.favourite.model.contract.IFavouriteModel
import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.staff.model.connection.StaffConnection
import co.anitrend.data.studio.model.connection.StudioConnection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Favourites](https://anilist.github.io/ApiV2-GraphQL-Docs/favourites.doc.html)
 * User's favourite anime, manga, characters, staff & studios
 */
@Serializable
internal data class FavouriteModel(
    @SerialName("anime") override val anime: MediaConnection?,
    @SerialName("characters") override val characters: CharacterConnection?,
    @SerialName("manga") override val manga: MediaConnection?,
    @SerialName("staff") override val staff: StaffConnection?,
    @SerialName("studios") override val studios: StudioConnection?
) : IFavouriteModel