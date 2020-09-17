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
import co.anitrend.data.staff.model.remote.connection.StaffConnection
import co.anitrend.data.studio.model.remote.connection.StudioConnection
import com.google.gson.annotations.SerializedName

/** [Favourites](https://anilist.github.io/ApiV2-GraphQL-Docs/favourites.doc.html)
 * User's favourite anime, manga, characters, staff & studios
 */
internal data class FavouriteModel(
    @SerializedName("anime") override val anime: MediaConnection?,
    @SerializedName("characters") override val characters: CharacterConnection?,
    @SerializedName("manga") override val manga: MediaConnection?,
    @SerializedName("staff") override val staff: StaffConnection?,
    @SerializedName("studios") override val studios: StudioConnection?
) : IFavouriteModel