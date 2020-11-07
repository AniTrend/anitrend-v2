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

package co.anitrend.data.favourite.model.contract

import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.data.character.model.remote.CharacterModel
import co.anitrend.data.character.model.remote.edge.CharacterEdge
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.media.model.edge.MediaEdge
import co.anitrend.data.staff.model.remote.StaffModel
import co.anitrend.data.staff.model.remote.edge.StaffEdge
import co.anitrend.data.studio.model.remote.StudioModel
import co.anitrend.data.studio.model.remote.edge.StudioEdge


/** [Favourites](https://anilist.github.io/ApiV2-GraphQL-Docs/favourites.doc.html)
 * User's favourite anime, manga, characters, staff & studios
 */
internal interface IFavouriteModel {
    val anime: IEntityConnection<MediaEdge, MediaModel>?
    val characters: IEntityConnection<CharacterEdge, CharacterModel>?
    val manga: IEntityConnection<MediaEdge, MediaModel>?
    val staff: IEntityConnection<StaffEdge, StaffModel>?
    val studios: IEntityConnection<StudioEdge, StudioModel>?
}